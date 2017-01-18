package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_AutoComplete_Item;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Find_Item_Adapter;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackSalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.custom.PopUpComment;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoButton;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoEditText;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;
import co.id.datascrip.mo_sam_div4_dts1.function.Hitung;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;
import co.id.datascrip.mo_sam_div4_dts1.object.Log_Feed;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Sales_Header;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.LogSQLite;

public class Input_Item_Activity extends AppCompatActivity {

    private RobotoEditText txPriceList, txQty, txUnit, txDiscountPct, txDiscount, txSubtotal;
    private AutoCompleteTextView txDescription;
    private RobotoTextView txLastDate, txLastLog, txLastInputDate;
    private RobotoButton btFind, btOK, btCancel, btShowLog;
    private LinearLayout linear_logs;
    private Context context;
    private Hitung h = new Hitung();
    private String currency;
    private Item item;
    private PopUpComment popUpComment;
    private Adapter_AutoComplete_Item adapter_autocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_item);
        context = this;
        initToolbar();

        txDescription = (AutoCompleteTextView) findViewById(R.id.iia_description);
        txPriceList = (RobotoEditText) findViewById(R.id.iia_pricelist);
        txQty = (RobotoEditText) findViewById(R.id.iia_qty);
        txDiscountPct = (RobotoEditText) findViewById(R.id.iia_discount_pct);
        txDiscount = (RobotoEditText) findViewById(R.id.iia_discount_value);
        txUnit = (RobotoEditText) findViewById(R.id.iia_unit);
        txSubtotal = (RobotoEditText) findViewById(R.id.iia_subtotal);
        btFind = (RobotoButton) findViewById(R.id.iia_btFind);
        btOK = (RobotoButton) findViewById(R.id.iia_btOk);
        btCancel = (RobotoButton) findViewById(R.id.iia_btCancel);
        btShowLog = (RobotoButton) findViewById(R.id.iia_btShowReply);
        linear_logs = (LinearLayout) findViewById(R.id.iia_linear_log);
        txLastDate = (RobotoTextView) findViewById(R.id.iia_last_date_log);
        txLastLog = (RobotoTextView) findViewById(R.id.iia_last_text_log);
        txLastInputDate = (RobotoTextView) findViewById(R.id.iia_last_update_price);

        currency = getIntent().getStringExtra("currency");
        item = getIntent().getParcelableExtra("item");

        if (item != null) {
            setData(item);
            popUpComment = new PopUpComment(this, item);
        } else {
            item = new Item();
            linear_logs.setVisibility(View.GONE);
            txDescription.requestFocus();
        }

        adapter_autocomplete = new Adapter_AutoComplete_Item(this);
        txDescription.setAdapter(adapter_autocomplete);

        txDescription.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(txDescription.getWindowToken(), 0);
                item = adapter_autocomplete.getItem(position);
                txUnit.setText(item.getUnit());
                txDescription.setText(item.getDesc());
                new Process_Sales_Header(context).GetPrice(item.getCode(), new CallbackSalesHeader.CBGetPrice() {
                    @Override
                    public void onCB(boolean success, double price) {
                        if (success) {
                            txPriceList.setText(h.formatNumber(price));
                            txLastInputDate.setVisibility(View.GONE);
                        } else {
                            txPriceList.setText(h.formatNumber(item.getPrice()));
                            txLastInputDate.setText("Last Input Price : " + item.getInputDate());
                            txLastInputDate.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        btShowLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpComment.show(view);
                popUpComment.setOnDismissListener(new PopUpComment.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        setData(item);
                    }
                });
            }
        });

        txDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btFind.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        txPriceList.addTextChangedListener(new CurrencyFormat(txPriceList));
        txPriceList.setOnFocusChangeListener(new LostFocus(txPriceList));
        txQty.addTextChangedListener(new CurrencyFormat(txQty));
        txQty.setOnFocusChangeListener(new LostFocus(txQty));
        txDiscount.addTextChangedListener(new DiscountCalculator(txDiscount, txDiscountPct));
        txDiscount.setOnFocusChangeListener(new LostFocus(txDiscount));
        txDiscountPct.addTextChangedListener(new DiscountCalculator(txDiscountPct, txDiscount));
        txDiscountPct.setOnFocusChangeListener(new LostFocus(txDiscountPct));

        btFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Process_Sales_Header(context).Find_Item(currency, txDescription.getText().toString().trim(), new CallbackSalesHeader.CBFindItem() {
                    @Override
                    public void onCB(ArrayList<Item> items) {
                        if (items.size() > 0)
                            showDialog(items, txDescription.getText().toString());
                        else
                            Toast.makeText(context, "Item not found!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                item.setQuantity(h.getInt(txQty));
                item.setPrice(h.getDouble(txPriceList));
                item.setDiscount(h.getDouble(txDiscount));
                item.setDiscPct(h.getDouble(txDiscountPct));
                item.setSubtotal(h.getDouble(txSubtotal));
                item.setDesc(txDescription.getText().toString());
                item.setUnit(txUnit.getText().toString());
                item.setError(0);
                item.setNotes("");

                if (isValid()) {
                    Intent intent = new Intent();
                    intent.putExtra("item", item);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Sales Line");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean isValid() {
        if (txDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Item harus diisi!", Toast.LENGTH_SHORT).show();
            txDescription.requestFocus();
            return false;
        }
        if (h.getInt(txQty) == 0) {
            Toast.makeText(this, "Quantity tidak boleh 0", Toast.LENGTH_SHORT).show();
            txQty.requestFocus();
            return false;
        }
        return true;
    }

    private void showDialog(final ArrayList<Item> items, final String teks) {
        Find_Item_Adapter adapter = new Find_Item_Adapter(this, R.layout.adapter_find_item, items);
        adapter.setSearchText(teks);
        (new AlertDialog.Builder(this))
                .setTitle("Pilih Item : ")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item = items.get(i);
                        txDescription.setText(item.getDesc());
                        txUnit.setText(item.getUnit());
                        txPriceList.setText(h.formatNumber(item.getPrice()));
                        txLastInputDate.setVisibility(View.GONE);
                    }
                })
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData(Item item) {
        if (item != null) {
            txDescription.setText(item.getDesc());
            txUnit.setText(item.getUnit());
            txPriceList.setText(h.formatNumber(item.getPrice()));
            txDiscount.setText(h.formatNumber(item.getDiscount()));
            txDiscountPct.setText(h.formatNumber(item.getDiscPct()));
            txQty.setText(String.valueOf(item.getQuantity()));
            txSubtotal.setText(h.formatNumber(item.getSubtotal()));
            ArrayList<Log_Feed> logs = new LogSQLite(this).get(item.getID());
            if (logs != null && logs.size() > 0) {
                for (int i = 0; i < logs.size(); i++) {
                    if (logs.get(i).getFeedback().getBex().getEmpNo().equals(Global.getBEX(context).getEmpNo()))
                        continue;
                    else {
                        txLastDate.setText(logs.get(i).getDate());
                        txLastLog.setText(logs.get(i).getFeedback().getNote());
                        break;
                    }
                }
                linear_logs.setVisibility(View.VISIBLE);
            } else linear_logs.setVisibility(View.GONE);
        }
    }

    private class CurrencyFormat implements TextWatcher {

        private EditText editText;

        public CurrencyFormat(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.toString().equals("."))
                editText.setText("0.");
            else {
                h.FormatNumberDecimal(editable, editText);
                txSubtotal.setText(h.formatNumber(h.CalcSubTotal(txQty, txPriceList, txDiscount)));
            }
        }
    }

    private class DiscountCalculator implements TextWatcher {

        private EditText editText1, editText2;

        public DiscountCalculator(EditText editText1, EditText editText2) {
            this.editText1 = editText1;
            this.editText2 = editText2;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editText1.isFocused()) {
                if (editable.toString().equals("")) {
                    editText2.setText("");
                    txSubtotal.setText(h.formatNumber(h.CalcSubTotal(txQty, txPriceList, txDiscount)));
                } else if (editable.toString().equals(".")) {
                    editText1.setText("0.");
                    editText2.setText("");
                } else if (editable.toString().equals("0.")) {
                    h.FormatNumberDecimal(editable, editText1);
                    txSubtotal.setText(h.formatNumber(h.CalcSubTotal(txQty, txPriceList, txDiscount)));
                } else if (!editable.toString().endsWith(".")) {
                    h.FormatNumberDecimal(editable, editText1);
                    if (editText1 == txDiscount)
                        editText2.setText(h.formatNumber(h.countDiscOfVal(editText1, txPriceList)));
                    else
                        editText2.setText(h.formatNumber(h.countDiscOfPct(editText1, txPriceList)));
                    txSubtotal.setText(h.formatNumber(h.CalcSubTotal(txQty, txPriceList, txDiscount)));
                }
            }
        }
    }

    private class LostFocus implements View.OnFocusChangeListener {

        private EditText editText;

        public LostFocus(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                if (h.getDouble(editText) == 0)
                    editText.setText("0");
            }
        }
    }


}
