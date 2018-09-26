package com.salamander.mo_sam_div4_dts1.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Sales_Order_Activity;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Sales_Header;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.util.CurrencyUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.RetroStatus;

/**
 * Created by benny_aziz on 04/26/2017.
 */

public class Fragment_Sales_Total extends Fragment {

    private Input_Sales_Order_Activity context;
    private EditText tx_subtotal, tx_potongan, tx_dpp, tx_ppn, tx_total_bayar, tx_notes;
    private Button bt_submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sales_total, container, false);
        context = (Input_Sales_Order_Activity) getActivity();
        setHasOptionsMenu(true);
        initView(rootView);
        return rootView;
    }

    private boolean validasi() {
        boolean isValid = true;
        String[] errorList = new String[5];
        int i = -1;
        if (Utils.isEmpty(context.getSalesHeader().getOrderNo())) {
            ++i;
            errorList[i] = "No. Order";
            isValid = false;
        }
        if (context.getSalesHeader().getCustomer() == null || Utils.isEmpty(context.getSalesHeader().getCustomer().getCode())) {
            ++i;
            errorList[i] = "Customer";
            isValid = false;
        }
        if (context.getSalesHeader().getPromisedDate().getDate().getTime() == 0) {
            ++i;
            errorList[i] = "Promised Delivery Date";
            isValid = false;
        }
        if (Utils.isEmpty(context.getSalesHeader().getTempo())) {
            ++i;
            errorList[i] = "Tempo Pembayaran";
            isValid = false;
        }
        if (Utils.isEmpty(context.getSalesHeader().getSyarat())) {
            ++i;
            errorList[i] = "Syarat Pembayaran";
            isValid = false;
        }
        if (!isValid) {
            String errorMsg = "";
            for (String error : errorList) {
                if (error != null)
                    errorMsg += "- " + error + "<br/>";
            }
            DialogUtils.showErrorNetwork(context, "Field berikut harus diisi :", errorMsg, false);
            return false;
        }
        return true;
    }

    private void initView(View rootView) {
        tx_subtotal = rootView.findViewById(R.id.tx_subtotal);
        tx_potongan = rootView.findViewById(R.id.tx_potongan);
        tx_dpp = rootView.findViewById(R.id.tx_dpp);
        tx_ppn = rootView.findViewById(R.id.tx_ppn);
        tx_total_bayar = rootView.findViewById(R.id.tx_total_bayar);
        tx_notes = rootView.findViewById(R.id.tx_notes);
        bt_submit = rootView.findViewById(R.id.bt_submit);

        if (context.getSalesHeader().isEditable())
            bt_submit.setVisibility(View.VISIBLE);
        else bt_submit.setVisibility(View.GONE);

        tx_potongan.addTextChangedListener(new CurrencyFormat(tx_potongan));
        tx_potongan.setOnFocusChangeListener(new LostFocus(tx_potongan));
        tx_notes.setOnFocusChangeListener(new LostFocus(tx_notes));

        setData();
        context.setOnItemChangedListener(new Input_Sales_Order_Activity.OnItemChanged() {
            @Override
            public void onItemChanged() {
                setData();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity(), getActivity().getCurrentFocus());
                if (validasi()) {
                    context.getSalesHeader().setNotes(tx_notes.getText().toString());
                    context.getSalesHeader().setPotongan(CurrencyUtils.getDouble(tx_potongan));
                    //context.setSalesHeader(new SalesHeaderSQLite(context).post(context.getSalesHeader()));
                    if (context.getSalesHeader() != null) {
                        new Proses_Sales_Header(context, "Sending Order...").post(context.getSalesHeader(), new Callbacks.OnCB() {
                            @Override
                            public void onCB(RetroStatus status) {
                                if (status.isSuccess()) {
                                    Toast.makeText(context, "Order berhasil disimpan", Toast.LENGTH_SHORT).show();
                                    getActivity().setResult(Activity.RESULT_OK);
                                    getActivity().finish();
                                } else {
                                    //new SalesHeaderSQLite(context).delete(context.getSalesHeader().getIDServer());
                                    DialogUtils.showErrorNetwork(context, null, status.getMessage(), false);
                                }
                            }
                        });
                    }
                }
            }
        });
        setEditable();
    }

    private void setEditable() {
        tx_potongan.setEnabled(context.getSalesHeader().isEditable());
        tx_potongan.setFocusable(context.getSalesHeader().isEditable());
        tx_notes.setEnabled(context.getSalesHeader().isEditable());
        tx_notes.setFocusable(context.getSalesHeader().isEditable());
    }

    private void setData() {
        if (context.getSalesHeader().getSubtotal() != 0) {
            tx_subtotal.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getSubtotal()));
            if (context.getSalesHeader().getPotongan() == 0)
                tx_potongan.setText("");
            else
                tx_potongan.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getPotongan()));
            tx_dpp.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getDPP()));
            tx_ppn.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getPPnDPP()));
            tx_total_bayar.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getTotal()));
        } else {
            tx_potongan.setText("");
            tx_dpp.setText("");
            tx_ppn.setText("");
            tx_total_bayar.setText("");
            tx_subtotal.setText("");
        }
        tx_notes.setText(context.getSalesHeader().getNotes());
    }

    private void calcTotal() {
        context.getSalesHeader().setPotongan(CurrencyUtils.getDouble(tx_potongan));
        tx_dpp.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getDPP()));
        tx_ppn.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getPPnDPP()));
        tx_total_bayar.setText(CurrencyUtils.formatNumber(context.getSalesHeader().getTotal()));
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
            calcTotal();
        }
    }

    private class LostFocus implements View.OnFocusChangeListener {

        private EditText editText;

        LostFocus(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (editText == tx_potongan) {
                if (!tx_subtotal.getText().toString().equals("") && !hasFocus) {
                    if (CurrencyUtils.getDouble(editText) == 0)
                        editText.setText("");
                    else
                        editText.setText(CurrencyUtils.formatNumber(CurrencyUtils.getDouble(editText)));
                    context.getSalesHeader().setPotongan(CurrencyUtils.getDouble(tx_potongan));
                }
            } else if (editText == tx_notes && !hasFocus) {
                context.getSalesHeader().setNotes(tx_notes.getText().toString());
            }
        }
    }
}
