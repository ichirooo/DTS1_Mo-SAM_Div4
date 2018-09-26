package com.salamander.mo_sam_div4_dts1.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Sales_Order_Activity;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Data;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.TermsOfPaymentSQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_base_module.util.Adapter_List_String;
import com.salamander.salamander_network.RetroStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Fragment_Sales_Header extends Fragment {

    public static final int REQUEST_SELECT_CUSTOMER = 1000;
    public TextView tx_dp_promised_delivery_date, tx_sp_tempo;
    public LinearLayout ll_select_customer, ll_detail_customer;
    private Input_Sales_Order_Activity context;
    private EditText tx_no_order;
    private TextInputEditText tx_ship_address_1, tx_ship_address_2, tx_city, tx_syarat_pembayaran, tx_npwp, tx_contact_person, tx_phone;
    private TextView tv_no_customer, tv_nama_customer, tv_alamat_1, tv_alamat_2, tv_kota;
    private DatePickerDialog.OnDateSetListener mPromisedDeliveryDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            GregorianCalendar pickedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0, 0);
            Date currentDate = DateUtils.stringToDate(Tanggal.FORMAT_DATE, DateUtils.dateToString(Tanggal.FORMAT_DATE, new Date()));
            if (pickedDate.before(currentDate)) {
                Toast.makeText(context, "Promised Delivery tidak boleh sebelum tanggal hari ini.", Toast.LENGTH_SHORT).show();
            } else {
                context.getSalesHeader().getPromisedDate().set(pickedDate.getTime());
                tx_dp_promised_delivery_date.setText(DateUtils.dateToString(Tanggal.FORMAT_UI, pickedDate.getTime(), new Locale("id")));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sales_header, container, false);
        context = (Input_Sales_Order_Activity) getActivity();
        setHasOptionsMenu(true);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        this.tx_no_order = rootView.findViewById(R.id.tx_no_order);
        this.tv_no_customer = rootView.findViewById(R.id.tv_customer_code);
        this.tv_nama_customer = rootView.findViewById(R.id.tv_customer_name);
        this.tv_alamat_1 = rootView.findViewById(R.id.tv_customer_bill_address_1);
        this.tv_alamat_2 = rootView.findViewById(R.id.tv_customer_bill_address_2);
        this.tv_kota = rootView.findViewById(R.id.tv_customer_city);
        this.tx_ship_address_1 = rootView.findViewById(R.id.tx_ship_address_1);
        this.tx_ship_address_2 = rootView.findViewById(R.id.tx_ship_address_2);
        this.tx_city = rootView.findViewById(R.id.tx_city);
        this.tx_syarat_pembayaran = rootView.findViewById(R.id.tx_syarat_pembayaran);
        this.tx_dp_promised_delivery_date = rootView.findViewById(R.id.tx_dp_promised_delivery_date);
        this.tx_sp_tempo = rootView.findViewById(R.id.tx_sp_tempo);
        this.tx_npwp = rootView.findViewById(R.id.tx_npwp);
        this.tx_contact_person = rootView.findViewById(R.id.tx_contact_person);
        this.tx_phone = rootView.findViewById(R.id.tx_phone);
        this.ll_select_customer = rootView.findViewById(R.id.ll_select_customer);
        this.ll_detail_customer = rootView.findViewById(R.id.ll_detail_customer);

        if (context.getSalesHeader().isEditable()) {
            tx_syarat_pembayaran.setOnFocusChangeListener(new LostFocus(tx_syarat_pembayaran));
            tx_dp_promised_delivery_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context.getSalesHeader().getPromisedDate().getDate().getTime() == 0)
                        showDatePickerDialog(mPromisedDeliveryDateListener, new Date());
                    else
                        showDatePickerDialog(mPromisedDeliveryDateListener, context.getSalesHeader().getPromisedDate().getDate());
                }
            });
            tx_sp_tempo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new TermsOfPaymentSQLite(context).getAll().size() == 0) {
                        new Proses_Data(context, true).getTermsOfPayment(new Callbacks.OnCB() {
                            @Override
                            public void onCB(RetroStatus status) {
                                if (status.isSuccess())
                                    spTempoClick();
                                else
                                    DialogUtils.showErrorNetwork(context, status.getMessage(), false);
                            }
                        });
                    } else spTempoClick();
                }
            });
        } else {
            tx_dp_promised_delivery_date.setOnClickListener(null);
            tx_sp_tempo.setOnClickListener(null);
            tx_no_order.setEnabled(false);
            tx_no_order.setFocusable(false);
            tx_syarat_pembayaran.setEnabled(false);
            tx_syarat_pembayaran.setFocusable(false);
            tx_ship_address_1.setEnabled(false);
            tx_ship_address_1.setFocusable(false);
            tx_ship_address_2.setEnabled(false);
            tx_ship_address_2.setFocusable(false);
            tx_city.setEnabled(false);
            tx_city.setFocusable(false);
            tx_npwp.setEnabled(false);
            tx_npwp.setFocusable(false);
            tx_contact_person.setEnabled(false);
            tx_contact_person.setFocusable(false);
            tx_phone.setEnabled(false);
            tx_phone.setFocusable(false);
        }

        setData();
        if (context.getSalesHeader().isEditable())
            tx_syarat_pembayaran.requestFocus();
    }

    private void spTempoClick() {
        Adapter_List_String adapter = new Adapter_List_String(context, new TermsOfPaymentSQLite(context).getAll());
        App.setAdapter(getActivity(), adapter, new App.OnPilih() {
            @Override
            public void onPilih(String selectedText) {
                context.getSalesHeader().setTempo(selectedText);
                tx_sp_tempo.setText(selectedText);
            }
        });
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener mListener, Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog d = new DatePickerDialog(context, mListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        d.show();
    }

    private void setData() {
        tx_no_order.setText(context.getSalesHeader().getOrderNo());
        tx_syarat_pembayaran.setText(context.getSalesHeader().getSyarat());
        if (context.getSalesHeader().getCustomer() != null) {
            tv_no_customer.setText(context.getSalesHeader().getCustomer().getCode());
            tv_nama_customer.setText(context.getSalesHeader().getCustomer().getName());
            tv_alamat_1.setText(context.getSalesHeader().getCustomer().getAddress1());
            tv_alamat_2.setText(context.getSalesHeader().getCustomer().getAddress2());
            tv_kota.setText(context.getSalesHeader().getCustomer().getCity());
            tx_ship_address_1.setText(context.getSalesHeader().getCustomer().getAddress1());
            tx_ship_address_2.setText(context.getSalesHeader().getCustomer().getAddress2());
            tx_city.setText(context.getSalesHeader().getCustomer().getCity());
            tx_npwp.setText(context.getSalesHeader().getCustomer().getNPWP());
            tx_contact_person.setText(context.getSalesHeader().getCustomer().getContactPerson());
            tx_phone.setText(context.getSalesHeader().getCustomer().getPhone());
            if (Utils.isEmpty(context.getSalesHeader().getCustomer().getAddress2()))
                tv_alamat_2.setVisibility(View.GONE);
            else tv_alamat_2.setVisibility(View.VISIBLE);
            ll_detail_customer.setVisibility(View.VISIBLE);
        }
        if (context.getSalesHeader().getPromisedDate().getDate().getTime() == 0)
            tx_dp_promised_delivery_date.setText(getString(R.string.pilih_promised_delivery_date_hint));
        else
            tx_dp_promised_delivery_date.setText(DateUtils.dateToString(Tanggal.FORMAT_UI, context.getSalesHeader().getPromisedDate(), new Locale("id")));
        if (Utils.isEmpty(context.getSalesHeader().getTempo()))
            tx_sp_tempo.setText(getString(R.string.pilih_tempo_hint));
        else
            tx_sp_tempo.setText(context.getSalesHeader().getTempo());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CUSTOMER && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getBundleExtra("bundle");
            if (bundle != null) {
                context.getSalesHeader().setCustomer((Customer) bundle.getParcelable("customer"));
                setData();
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
                if (editText == tx_syarat_pembayaran)
                    context.getSalesHeader().setSyarat(tx_syarat_pembayaran.getText().toString());
            }
        }
    }
}