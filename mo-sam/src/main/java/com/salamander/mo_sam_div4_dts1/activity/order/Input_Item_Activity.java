package com.salamander.mo_sam_div4_dts1.activity.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.ToolbarActivity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_Cari_Item;
import com.salamander.mo_sam_div4_dts1.fragment.Fragment_Sales_Line;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Sales_Header;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.util.CurrencyUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.RetroStatus;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;

public class Input_Item_Activity extends ToolbarActivity {

    private Activity context;
    private EditText tx_cari_item;
    private Button bt_cari_item;
    private EditText tx_item_code, tx_item_description, tx_item_price, tx_item_unit, tx_item_quantity, tx_item_discount_value, tx_item_discount_pct, tx_subtotal, tx_item_price_list_nav;
    private TextView bt_simpan, bt_batal;
    private CardView card_detail_item;
    private LinearLayout ll_cari_item;
    private boolean isEditable = true;

    private Item item;
    private int position = 0;
    private Menu optionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sales_line);
        initToolbar();
        context = this;
        if (getIntent().getBundleExtra("bundle") != null) {
            item = getIntent().getBundleExtra("bundle").getParcelable("item");
            position = getIntent().getBundleExtra("bundle").getInt("position");
            isEditable = getIntent().getBundleExtra("bundle").getBoolean("isEditable", true);
        }
        initView();
        setData();
    }

    private void setData() {
        if (item != null) {
            tx_item_code.setText(item.getCode());
            tx_item_description.setText(item.getDescription());
            tx_item_price.setText(CurrencyUtils.formatNumber(item.getPrice()));
            tx_item_price_list_nav.setText(CurrencyUtils.formatNumber(item.getPriceNAV()));
            tx_item_discount_value.setText(CurrencyUtils.formatNumber(item.getDiscountValue()));
            tx_item_discount_pct.setText(CurrencyUtils.formatNumber(item.getDiscountPct()));
            tx_item_quantity.setText(CurrencyUtils.formatNumber(item.getQuantity()));
            tx_item_unit.setText(item.getUnit());
            card_detail_item.setVisibility(View.VISIBLE);
        } else
            card_detail_item.setVisibility(View.GONE);
    }

    private void setEditable() {
        if (isEditable)
            ll_cari_item.setVisibility(View.VISIBLE);
        else ll_cari_item.setVisibility(View.GONE);

        tx_cari_item.setEnabled(isEditable);
        tx_item_price.setEnabled(isEditable);
        tx_item_quantity.setEnabled(isEditable);
        tx_item_discount_value.setEnabled(isEditable);
        tx_item_discount_pct.setEnabled(isEditable);

        tx_cari_item.setFocusable(isEditable);
        tx_item_price.setFocusable(isEditable);
        tx_item_quantity.setFocusable(isEditable);
        tx_item_discount_value.setFocusable(isEditable);
        tx_item_discount_pct.setFocusable(isEditable);
    }

    private void initToolbar() {
        if (getIntent().getIntExtra("requestCode", 0) == Fragment_Sales_Line.REQ_NEW_ITEM)
            initToolbar(R.string.input_item_title);
        else initToolbar(R.string.edit_item_title);
    }

    @Override
    public void onBackPressed() {
        if (isEditable && item != null) {
            String message = "Batalkan input item?";
            if (getIntent().getIntExtra("requestCode", 0) == Fragment_Sales_Line.REQ_EDIT_ITEM)
                message = "Batalkan edit item?";
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (isEditable) {
            getMenuInflater().inflate(R.menu.menu_input_item, menu);
            optionMenu = menu;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (optionMenu != null) {
            optionMenu.findItem(R.id.action_save_item).setVisible(isEditable);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            /*
            case R.id.action_delete_item:
                Utils.hideKeyboard(this);
                new AlertDialog.Builder(this)
                        .setMessage("Hapus item?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intentDelete = new Intent();
                                Bundle bundleDelete = new Bundle();
                                bundleDelete.putParcelable("item", null);
                                bundleDelete.putInt("position", position);
                                intentDelete.putExtra("bundle", bundleDelete);
                                setResult(RESULT_OK, intentDelete);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
                return true;
                */
            case R.id.action_save_item:
                saveData();
                Utils.hideKeyboard(this);
                if (item == null) {
                    DialogUtils.showErrorMessage(context, "Item harus diisi.", false);
                } else if (item.getQuantity() <= 0) {
                    DialogUtils.showErrorMessage(context, "Quantity harus diisi.", false);
                    tx_item_quantity.requestFocus();
                } else {
                    Intent intentSave = new Intent();
                    Bundle bundleSave = new Bundle();
                    bundleSave.putParcelable("item", item);
                    bundleSave.putInt("position", position);
                    intentSave.putExtra("bundle", bundleSave);
                    setResult(RESULT_OK, intentSave);
                    finish();
                }
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initView() {
        tx_cari_item = findViewById(R.id.tx_cari_item);
        bt_cari_item = findViewById(R.id.bt_cari_item);
        tx_item_code = findViewById(R.id.tx_item_code);
        tx_item_description = findViewById(R.id.tx_item_description);
        tx_item_price_list_nav = findViewById(R.id.tx_item_price_list_nav);
        tx_item_price = findViewById(R.id.tx_item_price);
        tx_item_unit = findViewById(R.id.tx_item_unit);
        tx_item_quantity = findViewById(R.id.tx_item_quantity);
        tx_item_discount_value = findViewById(R.id.tx_item_discount_value);
        tx_item_discount_pct = findViewById(R.id.tx_item_discount_pct);
        tx_subtotal = findViewById(R.id.tx_subtotal);
        bt_simpan = findViewById(R.id.bt_simpan);
        bt_batal = findViewById(R.id.bt_batal);
        card_detail_item = findViewById(R.id.card_detail_item);
        ll_cari_item = findViewById(R.id.ll_cari_item);
        bt_simpan.setVisibility(View.GONE);
        bt_batal.setVisibility(View.GONE);

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (item != null && getCurrentFocus() != tx_cari_item && isEditable) {
                    if (isOpen)
                        ll_cari_item.setVisibility(View.GONE);
                    else ll_cari_item.setVisibility(View.VISIBLE);
                }
            }
        });

        tx_item_price.addTextChangedListener(new CurrencyFormat(tx_item_price));
        tx_item_price.setOnFocusChangeListener(new LostFocus(tx_item_price));
        tx_item_quantity.addTextChangedListener(new CurrencyFormat(tx_item_quantity));
        tx_item_quantity.setOnFocusChangeListener(new LostFocus(tx_item_quantity));
        tx_item_discount_value.addTextChangedListener(new DiscountCalculator(tx_item_discount_value, tx_item_discount_pct));
        tx_item_discount_value.setOnFocusChangeListener(new LostFocus(tx_item_discount_value));
        tx_item_discount_pct.addTextChangedListener(new DiscountCalculator(tx_item_discount_pct, tx_item_discount_value));
        tx_item_discount_pct.setOnFocusChangeListener(new LostFocus(tx_item_discount_pct));

        tx_cari_item.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_GO)
                    bt_cari_item.performClick();
                return true;
            }
        });
        bt_cari_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tx_cari_item.requestFocus();
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert keyboard != null;
                keyboard.hideSoftInputFromWindow(tx_cari_item.getWindowToken(), 0);

                if (!TextUtils.isEmpty(tx_cari_item.getText().toString()))
                    new Proses_Sales_Header(context, "Searching...").findItem(tx_cari_item.getText().toString(), new Callbacks.OnCBFindItem() {
                        @Override
                        public void onCB(RetroStatus status, ArrayList<Item> list_item) {
                            if (status.isSuccess()) {
                                showDialog(list_item);
                            } else
                                DialogUtils.showErrorNetwork(context, null, status.getMessage(), false);
                        }
                    });
            }
        });
        bt_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setEditable();
    }

    private void showDialog(final ArrayList<Item> list_item) {
        Adapter_Cari_Item adapter = new Adapter_Cari_Item(this, R.layout.adapter_find_item, list_item);
        adapter.setSearchText(tx_cari_item.getText().toString());
        (new AlertDialog.Builder(this))
                .setTitle("Pilih Item : ")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        item = list_item.get(i);
                        setData();
                    }
                })
                .show();
    }

    private void calcSubTotal() {
        tx_subtotal.setText(CurrencyUtils.formatNumber(CurrencyUtils.getDouble(tx_item_price) * CurrencyUtils.getInt(tx_item_quantity) - CurrencyUtils.getDouble(tx_item_discount_value)));
    }

    public double countDiscOfPct() {
        return CurrencyUtils.getDouble(tx_item_price) * CurrencyUtils.getInt(tx_item_quantity) * (CurrencyUtils.getDouble(tx_item_discount_pct) / 100);
    }

    public double countDiscOfVal() {
        return CurrencyUtils.getDouble(tx_item_price) * CurrencyUtils.getInt(tx_item_quantity) == 0.0 ? 0.0 : ((CurrencyUtils.getDouble(tx_item_discount_value) / (CurrencyUtils.getDouble(tx_item_price) * CurrencyUtils.getInt(tx_item_quantity))) * 100);
    }

    private void saveData() {
        if (item != null) {
            item.setPrice(CurrencyUtils.getDouble(tx_item_price));
            item.setPriceNAV(CurrencyUtils.getDouble(tx_item_price_list_nav));
            item.setDiscountValue(CurrencyUtils.getDouble(tx_item_discount_value));
            item.setDiscountPct(CurrencyUtils.getDouble(tx_item_discount_pct));
            item.setQuantity(CurrencyUtils.getInt(tx_item_quantity));
        }
    }

    private class CurrencyFormat implements TextWatcher {

        private EditText editText;

        CurrencyFormat(EditText editText) {
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
                if (!editText.getText().toString().endsWith("."))
                    CurrencyUtils.FormatNumberDecimal(editable, editText);
            }
            calcSubTotal();
        }
    }

    private class DiscountCalculator implements TextWatcher {

        private EditText editText1, editText2;

        DiscountCalculator(EditText editText1, EditText editText2) {
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
                } else if (editable.toString().equals(".")) {
                    editText2.setText("");
                    editText1.append("0.");
                } else if (editable.toString().equals("0.")) {
                    CurrencyUtils.FormatNumberDecimal(editable, editText1);
                } else if (!editable.toString().endsWith(".")) {
                    CurrencyUtils.FormatNumberDecimal(editable, editText1);
                    if (editText1 == tx_item_discount_value)
                        editText2.setText(CurrencyUtils.formatNumber(countDiscOfVal()));
                    else
                        editText2.setText(CurrencyUtils.formatNumber(countDiscOfPct()));
                }
                calcSubTotal();
            }
        }
    }

    private class LostFocus implements View.OnFocusChangeListener {

        private EditText editText;

        LostFocus(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (!hasFocus) {
                editText.setText(CurrencyUtils.formatNumber(CurrencyUtils.getDouble(editText)));
            }
        }
    }
}