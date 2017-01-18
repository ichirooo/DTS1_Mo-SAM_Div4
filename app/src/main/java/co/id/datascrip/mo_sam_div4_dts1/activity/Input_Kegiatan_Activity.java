package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_AutoComplete_Customer;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Find_Customer_Adapter;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackCustomer.CBFind;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackKegiatan.CBPost;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackLocation;
import co.id.datascrip.mo_sam_div4_dts1.custom.spinner.NiceSpinner;
import co.id.datascrip.mo_sam_div4_dts1.function.Waktu;
import co.id.datascrip.mo_sam_div4_dts1.location.MapPosition;
import co.id.datascrip.mo_sam_div4_dts1.location.Proses.getLocation;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Customer;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.KegiatanSQLite;

public class Input_Kegiatan_Activity extends AppCompatActivity {

    private final int NEW_CUSTOMER_REQUEST = 10;

    private GoogleMap maps;

    private TextView txtDateStart, txtTimeStart, txtDateEnd, txtTimeEnd;
    private EditText txtSubject, txtCust;
    private NiceSpinner spTipeKeg, spJenisKeg;
    private Button btfind, btSaveKeg;
    private AutoCompleteTextView txtCustAuto;

    private Kegiatan kegiatan;
    private Customer customer;

    private Adapter_AutoComplete_Customer adapter_autocomplete;

    private GregorianCalendar dateStart, dateEnd, currentDate, startDateTime, endDateTime;
    private DatePickerDialog.OnDateSetListener mSetDateListenerStart = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dateStart = new GregorianCalendar(year, monthOfYear, dayOfMonth, Waktu.mHourStart, Waktu.mMinStart);
            if (!dateStart.before(currentDate)) {
                Waktu.mYearStart = year;
                Waktu.mMonthStart = monthOfYear;
                Waktu.mDateStart = dayOfMonth;
                setWaktuStart();
                if (!dateStart.before(endDateTime)) {
                    Waktu.mYearEnd = year;
                    Waktu.mMonthEnd = monthOfYear;
                    Waktu.mDateEnd = dayOfMonth;
                    setWaktuEnd();
                }
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener mSetTimeListenerStart = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            dateStart = new GregorianCalendar(Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart,
                    hourOfDay, minute);
            if (!dateStart.before(currentDate)) {
                Waktu.mHourStart = hourOfDay;
                Waktu.mMinStart = minute;
                setWaktuStart();
                if (!dateStart.before(endDateTime)) {
                    Waktu.mHourEnd = hourOfDay;
                    Waktu.mMinEnd = minute;
                    setWaktuEnd();
                }
            }
        }
    };
    private DatePickerDialog.OnDateSetListener mSetDateListenerEnd = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            dateEnd = new GregorianCalendar(year, monthOfYear, dayOfMonth, Waktu.mHourEnd, Waktu.mMinEnd);
            if (!dateEnd.before(Waktu.date) && !dateEnd.before(startDateTime)) {
                Waktu.mYearEnd = year;
                Waktu.mMonthEnd = monthOfYear;
                Waktu.mDateEnd = dayOfMonth;
                setWaktuEnd();
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener mSetTimeListenerEnd = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            dateEnd = new GregorianCalendar(Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd,
                    hourOfDay, minute);
            if (!dateEnd.before(startDateTime)) {
                Waktu.mHourEnd = hourOfDay;
                Waktu.mMinEnd = minute;
                setWaktuEnd();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_kegiatan);
        initToolbar();

        final Context context = this;
        txtDateStart = (TextView) findViewById(R.id.inp_date_start);
        txtTimeStart = (TextView) findViewById(R.id.inp_time_start);
        txtDateEnd = (TextView) findViewById(R.id.inp_date_end);
        txtTimeEnd = (TextView) findViewById(R.id.inp_time_end);
        txtSubject = (EditText) findViewById(R.id.txtSubject);
        txtCust = (EditText) findViewById(R.id.txtCustName);
        txtCustAuto = (AutoCompleteTextView) findViewById(R.id.txtCustNameAutoComplete);
        btSaveKeg = (Button) findViewById(R.id.btSimpanKeg);
        btfind = (Button) findViewById(R.id.btfind);
        spTipeKeg = (NiceSpinner) findViewById(R.id.txtTipeKeg);
        spJenisKeg = (NiceSpinner) findViewById(R.id.txtJenisKeg);

        maps = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap)).getMap();

        new Waktu();
        setWaktuStart();
        setWaktuEnd();

        currentDate = new GregorianCalendar(Waktu.mYear, Waktu.mMonth, Waktu.mDate, Waktu.mHour, Waktu.mMin);

        final String[] tipe = new String[]{"Kunjungan", "Korespondensi"};
        final String[] kun = new String[]{"Kunjungan Rutin", "Meeting", "Presentasi", "Demo Produk", "Negosiasi", "Diskusi"};
        final String[] kor = new String[]{"Email", "Telepon", "Fax", "Lainnya"};

        final List<String> tipe_ = new LinkedList<>(Arrays.asList("Kunjungan", "Korespondensi"));
        final List<String> kun_ = new LinkedList<>(Arrays.asList("Kunjungan Rutin", "Meeting", "Presentasi", "Demo Produk", "Negosiasi", "Diskusi"));
        final List<String> kor_ = new LinkedList<>(Arrays.asList("Email", "Telepon", "Fax", "Lainnya"));

        //spTipeKeg.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tipe));
        spTipeKeg.attachDataSource(tipe_);
        spJenisKeg.attachDataSource(kun_);

        setData();
        setEditable(getIntent().getBooleanExtra("canEdit", true));

        adapter_autocomplete = new Adapter_AutoComplete_Customer(this);
        txtCustAuto.setAdapter(adapter_autocomplete);

        txtCustAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(txtCustAuto.getWindowToken(), 0);
                customer = adapter_autocomplete.getItem(position);
                txtCustAuto.setText(customer.getName());
                if (customer.getLatitude() == 0 || customer.getLongitude() == 0)
                    getLocation();
                else
                    MapPosition.setPosition(maps, (new LatLng(customer.getLatitude(), customer.getLongitude())),
                            customer.getName());
            }
        });

        txtCustAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                btfind.setEnabled(!s.toString().trim().isEmpty());
            }
        });

        spTipeKeg.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                /*
                if (spTipeKeg.getAdapter().getItem(position).toString().equals("Kunjungan")) {
                    spJenisKeg.setAdapter(new ArrayAdapter<>(Input_Kegiatan_Activity.this, android.R.layout.simple_list_item_1, kun));
                } else
                    spJenisKeg.setAdapter(new ArrayAdapter<>(Input_Kegiatan_Activity.this, android.R.layout.simple_list_item_1, kor));
                kegiatan.setTipe(spTipeKeg.getItemAtPosition(position).toString());
*/
                if (spTipeKeg.getText().toString().equals("Kunjungan"))
                    //spJenisKeg.setAdapter(new ArrayAdapter<>(Input_Kegiatan_Activity.this, android.R.layout.simple_list_item_1, kun));
                    spJenisKeg.attachDataSource(kun_);
                else
                    //spJenisKeg.setAdapter(new ArrayAdapter<>(Input_Kegiatan_Activity.this, android.R.layout.simple_list_item_1, kor));
                    spJenisKeg.attachDataSource(kor_);
                kegiatan.setTipe(spTipeKeg.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spJenisKeg.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                //kegiatan.setJenis(spJenisKeg.getItemAtPosition(position).toString());
                kegiatan.setJenis(spJenisKeg.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        txtDateStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog d = new DatePickerDialog(Input_Kegiatan_Activity.this, mSetDateListenerStart,
                        Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart);
                d.show();
            }
        });
        txtTimeStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog tp = new TimePickerDialog(Input_Kegiatan_Activity.this, mSetTimeListenerStart,
                        Waktu.mHourStart, Waktu.mMinStart, true);
                tp.show();
            }
        });

        txtDateEnd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog d = new DatePickerDialog(Input_Kegiatan_Activity.this, mSetDateListenerEnd,
                        Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd);
                d.show();
            }
        });
        txtTimeEnd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog tp = new TimePickerDialog(Input_Kegiatan_Activity.this, mSetTimeListenerEnd,
                        Waktu.mHourEnd, Waktu.mMinEnd, true);
                tp.show();
            }
        });

        btSaveKeg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (validate()) {
                    new Process_Kegiatan(context).Post(buildKegiatan(), new CBPost() {
                        @Override
                        public void onCB(Kegiatan kegiatan) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
            }
        });

        btfind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(txtCustAuto.getWindowToken(), 0);
                new Process_Customer(context).Find(txtCustAuto.getText().toString(), new CBFind() {
                    @Override
                    public void onCB(ArrayList<Customer> customers) {
                        if (customers.size() > 0)
                            showDialog(customers);
                        else
                            Toast.makeText(Input_Kegiatan_Activity.this, "Customer not found!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        maps.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                MarkerOptions mo = new MarkerOptions();
                mo.position(point);
                maps.clear();
                maps.addMarker(mo);
                customer.setLatitude(point.latitude);
                customer.setLongitude(point.longitude);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean validate() {
        if (txtCustAuto.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Customer harus diisi!", Toast.LENGTH_SHORT).show();
            txtCustAuto.requestFocus();
            return false;
        }
        if (txtSubject.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Subject harus diisi!", Toast.LENGTH_SHORT).show();
            txtSubject.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.action_new_customer:
                Intent intentNewCustomer = new Intent(this, Input_Customer_Activity.class);
                intentNewCustomer.putExtra("cust_name", txtCustAuto.getText().toString());
                startActivityForResult(intentNewCustomer, NEW_CUSTOMER_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googlemap);
        if (f.isResumed()) {
            getSupportFragmentManager().beginTransaction().remove(f).commit();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input_kegiatan, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == NEW_CUSTOMER_REQUEST) {
            if (data.getParcelableExtra("customer") != null) {
                customer = data.getParcelableExtra("customer");
                kegiatan.getSalesHeader().setCustomer(customer);
                txtCustAuto.setText(customer.getName());
            }
        }
    }

    private void showDialog(final ArrayList<Customer> customers) {
        Find_Customer_Adapter adapter = new Find_Customer_Adapter(this, R.layout.adapter_find_customer, customers);
        (new AlertDialog.Builder(this))
                .setTitle("Pilih Customer : ")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        customer = customers.get(i);
                        txtCustAuto.setText(customer.getName());
                        if (customer.getLatitude() == 0 || customer.getLongitude() == 0)
                            getLocation();
                        else
                            MapPosition.setPosition(maps, (new LatLng(customer.getLatitude(), customer.getLongitude())),
                                    customer.getName());
                    }
                })
                .show();
    }

    private void setData() {
        // TODO Auto-generated method stub
        if (getIntent() != null)
            kegiatan = new KegiatanSQLite(this).get(getIntent().getIntExtra("id_kegiatan", -1));
        else
            kegiatan = null;
        if (kegiatan == null) {
            kegiatan = new Kegiatan();
            customer = new Customer();
            MapPosition.setCurrentPosition(maps);
            customer.setLatitude(MapPosition.getLatLng().latitude);
            customer.setLongitude(MapPosition.getLatLng().longitude);
            dateStart = new GregorianCalendar(Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart,
                    Waktu.mHourStart, Waktu.mMinStart);
            dateEnd = new GregorianCalendar(Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd,
                    Waktu.mHourEnd, Waktu.mMinEnd);
            startDateTime = new GregorianCalendar(Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart,
                    Waktu.mHourStart, Waktu.mMinStart);
            endDateTime = new GregorianCalendar(Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd,
                    Waktu.mHourEnd, Waktu.mMinEnd);
        } else {
            customer = kegiatan.getSalesHeader().getCustomer();
            txtCustAuto.setText(customer.getName());
            txtSubject.setText(kegiatan.getSubject());
            MapPosition.setPosition(maps, (new LatLng(customer.getLatitude(), customer.getLongitude())), customer.getName());
            spTipeKeg.setText(kegiatan.getTipe());
            spJenisKeg.setText(kegiatan.getJenis());
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date startdt = dateFormat.parse(kegiatan.getStartDate());
                Date enddt = dateFormat.parse(kegiatan.getEndDate());
                txtDateStart.setText(DateFormat.format("MMMM dd, yyyy", startdt).toString());
                txtDateEnd.setText(DateFormat.format("MMMM dd, yyyy", enddt).toString());
                txtTimeStart.setText(TimeToString(startdt));
                txtTimeEnd.setText(TimeToString(enddt));
                setDateStartEnd(startdt, enddt);
            } catch (ParseException e) {
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        kegiatan.setBEX(Global.getBEX(this));
    }

    private void setDateStartEnd(Date startdt, Date enddt) {
        // TODO Auto-generated method stub
        Calendar cStart = Calendar.getInstance();
        Calendar cEnd = Calendar.getInstance();
        cStart.setTime(startdt);
        cEnd.setTime(enddt);

        Waktu.mYearStart = cStart.get(Calendar.YEAR);
        Waktu.mMonthStart = cStart.get(Calendar.MONTH);
        Waktu.mDateStart = cStart.get(Calendar.DAY_OF_MONTH);
        Waktu.mHourStart = cStart.get(Calendar.HOUR_OF_DAY);
        Waktu.mMinStart = cStart.get(Calendar.MINUTE);

        Waktu.mYearEnd = cEnd.get(Calendar.YEAR);
        Waktu.mMonthEnd = cEnd.get(Calendar.MONTH);
        Waktu.mDateEnd = cEnd.get(Calendar.DAY_OF_MONTH);
        Waktu.mHourEnd = cEnd.get(Calendar.HOUR_OF_DAY);
        Waktu.mMinEnd = cEnd.get(Calendar.MINUTE);

        setWaktuStart();
        setWaktuEnd();
    }

    private void setWaktuStart() {
        // TODO Auto-generated method stub
        startDateTime = new GregorianCalendar(Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart, Waktu.mHourStart, Waktu.mMinStart);
        txtDateStart.setText(DateFormat.format("MMMM dd, yyyy", startDateTime).toString());
        txtTimeStart.setText(String.format("%02d", Waktu.mHourStart) + " : " + String.format("%02d", Waktu.mMinStart));
    }

    private void setWaktuEnd() {
        // TODO Auto-generated method stub
        endDateTime = new GregorianCalendar(Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd, Waktu.mHourEnd, Waktu.mMinEnd);
        txtDateEnd.setText(DateFormat.format("MMMM dd, yyyy", endDateTime).toString());
        txtTimeEnd.setText(String.format("%02d", Waktu.mHourEnd) + " : " + String.format("%02d", Waktu.mMinEnd));
    }

    private Kegiatan buildKegiatan() {

        Calendar ca = Calendar.getInstance();
        ca.set(Waktu.mYearStart, Waktu.mMonthStart, Waktu.mDateStart, Waktu.mHourStart, Waktu.mMinStart, 00);
        String tglstart = DateToString(ca);
        ca.set(Waktu.mYearEnd, Waktu.mMonthEnd, Waktu.mDateEnd, Waktu.mHourEnd, Waktu.mMinEnd, 00);
        String tglend = DateToString(ca);

        kegiatan.setTipe(spTipeKeg.getText().toString());
        kegiatan.setJenis(spJenisKeg.getText().toString());
        kegiatan.getSalesHeader().setCustomer(customer);
        kegiatan.setStartDate(tglstart);
        kegiatan.setEndDate(tglend);
        kegiatan.setSubject(txtSubject.getText().toString());

        return kegiatan;
    }

    private void getLocation() {
        getLocation task = new getLocation(this, new CallbackLocation.CBLocation() {

            @Override
            public void onCB(HashMap<String, String> locations) {
                // TODO Auto-generated method stub
                if (locations == null) {
                    Toast.makeText(Input_Kegiatan_Activity.this, "Location not found!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    MapPosition.setMapPosition(maps, locations, customer.getCity());
                    customer.setLatitude(Double.parseDouble(locations.get("lat")));
                    customer.setLongitude(Double.parseDouble(locations.get("lng")));
                }
            }
        });
        task.execute(customer.getCity());
    }

    private String DateToString(Calendar ca) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return s.format(ca.getTime());
    }

    private String TimeToString(Date date) {
        String time;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        time = String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.HOUR_OF_DAY)) +
                " : " + String.format(Locale.getDefault(), "%02d", calendar.get(Calendar.MINUTE));
        return time;
    }

    private void setEditable(boolean canEdit) {

        int visible;

        txtDateStart.setClickable(canEdit);
        txtTimeStart.setClickable(canEdit);
        txtDateEnd.setClickable(canEdit);
        txtTimeEnd.setClickable(canEdit);
        txtSubject.setFocusable(canEdit);
        txtCustAuto.setFocusable(canEdit);
        spTipeKeg.setClickable(canEdit);
        spJenisKeg.setClickable(canEdit);

        if (canEdit)
            visible = View.VISIBLE;
        else {
            maps.setOnMapClickListener(null);
            visible = View.GONE;
        }

        btSaveKeg.setVisibility(visible);
        btfind.setVisibility(visible);
    }
}
