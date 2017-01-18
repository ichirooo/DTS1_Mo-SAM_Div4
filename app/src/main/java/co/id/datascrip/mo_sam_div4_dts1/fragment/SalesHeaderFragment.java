package co.id.datascrip.mo_sam_div4_dts1.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.activity.Input_Order_Activity;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoEditText;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;
import co.id.datascrip.mo_sam_div4_dts1.function.Waktu;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;

/**
 * Created by benny_aziz on 02/16/2015.
 */
public class SalesHeaderFragment extends Fragment {

    public static RobotoEditText tx_syarat;
    RobotoTextView tx_promised_date;
    private RobotoEditText tx_no_order, tx_cust_code, tx_cust_name, tx_address1, tx_address2,
            tx_kota, tx_npwp, tx_kontak, tx_phone;
    private Spinner sp_tempo;

    private ArrayAdapter<String> tempo_adapter;
    private GregorianCalendar currentDate, promisedDate;
    private Kegiatan kegiatan = new Kegiatan();
    private SalesHeader sales_header = new SalesHeader();
    private DatePickerDialog.OnDateSetListener mSetDateListenerStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            promisedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth, 0, 0);
            if (!promisedDate.before(currentDate)) {
                Waktu.mYearPromised = year;
                Waktu.mMonthPromised = monthOfYear;
                Waktu.mDatePromised = dayOfMonth;
                tx_promised_date.setText(DateFormat.format("MMMM dd, yyyy", promisedDate).toString());
                setHeader();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sales_header_fragment, container, false);

        tx_no_order = (RobotoEditText) root.findViewById(R.id.shf_no_order);
        tx_cust_code = (RobotoEditText) root.findViewById(R.id.shf_cust_code);
        tx_cust_name = (RobotoEditText) root.findViewById(R.id.shf_cust_name);
        tx_address1 = (RobotoEditText) root.findViewById(R.id.shf_address_1);
        tx_address2 = (RobotoEditText) root.findViewById(R.id.shf_address_2);
        tx_kota = (RobotoEditText) root.findViewById(R.id.shf_kota);
        tx_kontak = (RobotoEditText) root.findViewById(R.id.shf_kontak);
        tx_promised_date = (RobotoTextView) root.findViewById(R.id.shf_promised_date);
        tx_syarat = (RobotoEditText) root.findViewById(R.id.shf_syarat);
        tx_npwp = (RobotoEditText) root.findViewById(R.id.shf_npwp);
        tx_phone = (RobotoEditText) root.findViewById(R.id.shf_phone);
        sp_tempo = (Spinner) root.findViewById(R.id.shf_tempo);

        new Waktu();
        currentDate = new GregorianCalendar(Waktu.mYear, Waktu.mMonth, Waktu.mDate, Waktu.mHour, Waktu.mMin);
        promisedDate = Waktu.datePromised;

        setAdapter();
        init();

        sp_tempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sales_header.setTempo(tempo_adapter.getItem(i));
                setHeader();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tx_promised_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog d = new DatePickerDialog(getActivity(), mSetDateListenerStart,
                        promisedDate.get(Calendar.YEAR), promisedDate.get(Calendar.MONTH), promisedDate.get(Calendar.DAY_OF_MONTH));
                d.show();
            }
        });
        return root;
    }

    private void setAdapter() {
        tempo_adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_checked, Global.terms);
        sp_tempo.setAdapter(tempo_adapter);

    }

    private void init() {
        kegiatan = Input_Order_Activity.kegiatan;
        sales_header = kegiatan.getSalesHeader();

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar c = Calendar.getInstance();
        if (sales_header.getPromisedDate() != null) {
            try {
                promisedDate.setTime(s.parse(sales_header.getPromisedDate()));
            } catch (ParseException e) {
                //Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ||
                    c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY ||
                    c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY ||
                    c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY ||
                    (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY))
                c.add(Calendar.DATE, 1);
            else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)
                c.add(Calendar.DATE, 3);
            else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
                c.add(Calendar.DATE, 2);
            promisedDate.setTime(c.getTime());
        }
        Waktu.datePromised = promisedDate;
        tx_promised_date.setText(DateFormat.format("MMMM dd, yyyy", promisedDate).toString());

        setData();
    }

    private void setData() {
        tx_no_order.setText(sales_header.getOrderNo());
        tx_syarat.setText(sales_header.getSyarat());
        tx_cust_code.setText(sales_header.getCustomer().getCode());
        tx_cust_name.setText(sales_header.getCustomer().getName());
        tx_address1.setText(sales_header.getCustomer().getAddress1());
        tx_address2.setText(sales_header.getCustomer().getAddress2());
        tx_kota.setText(sales_header.getCustomer().getCity());
        tx_kontak.setText(sales_header.getCustomer().getContact());
        tx_npwp.setText(sales_header.getCustomer().getNPWP());
        tx_phone.setText(sales_header.getCustomer().getPhone());
        if (!isEmpty(sales_header.getTempo()))
            sp_tempo.setSelection(tempo_adapter.getPosition(sales_header.getTempo()));
    }

    private boolean isEmpty(String teks) {
        if (teks == null)
            return true;
        else {
            if (teks.trim().equals(""))
                return true;
        }
        return false;
    }

    private void setHeader() {

        Customer customer = new Customer();

        customer.setCode(tx_cust_code.getText().toString());
        customer.setName(tx_cust_name.getText().toString());
        customer.setAddress1(tx_address1.getText().toString());
        customer.setAddress2(tx_address2.getText().toString());
        customer.setCity(tx_kota.getText().toString());
        customer.setContact(tx_kontak.getText().toString());
        customer.setNPWP(tx_npwp.getText().toString());
        customer.setPhone(tx_phone.getText().toString());

        sales_header.setSyarat(tx_syarat.getText().toString());
        sales_header.setPromisedDate(DateFormat.format("yyyy-MM-dd", promisedDate).toString());

        kegiatan.setSalesHeader(sales_header);

        Input_Order_Activity.kegiatan = kegiatan;
    }

    @Override
    public void onPause() {
        setHeader();
        super.onPause();
    }
}