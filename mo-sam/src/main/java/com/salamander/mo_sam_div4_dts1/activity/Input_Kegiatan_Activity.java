package com.salamander.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.greysonparrelli.permiso.Permiso;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.customer.Select_Customer_Kegiatan_Activity;
import com.salamander.mo_sam_div4_dts1.font.RobotoTextView;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_base_module.util.Adapter_List_String;
import com.salamander.salamander_base_module.widget.SalamanderDialog;
import com.salamander.salamander_location.LocationActivity;
import com.salamander.salamander_logger.LoggingExceptionHandler;
import com.salamander.salamander_network.RetroStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Input_Kegiatan_Activity extends LocationActivity {

    private static final int REQUEST_SELECT_CUSTOMER = 1000;
    private static final int REQUEST_OPEN_SETTING = 2000;

    final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;

    final String DIALOG_TITLE = "Access Location";
    final String DIALOG_MESSAGE_RATIONALE = "Aplikasi membutuhkan permission untuk mengakses Location.\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED = "Aplikasi membutuhkan permission untuk mengakses Location.\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";
    private final String[] list_tipe_kegiatan = new String[]{"Kunjungan", "Korespondensi"};
    private final String[] list_jenis_kunjungan = new String[]{"Kunjungan Rutin", "Meeting", "Presentasi", "Demo Produk", "Negosiasi", "Diskusi"};
    private final String[] list_jenis_korespondensi = new String[]{"Email", "Telepon", "Fax", "Lainnya"};
    private Activity activity;
    private Button bt_simpan;
    private Kegiatan kegiatan = new Kegiatan();
    private KegiatanSQLite kegiatanSQLite;
    private Adapter_List_String array_adapter_tipe, array_adapter_jenis;
    private boolean isEditable;
    private RobotoTextView tx_sp_tipe_kegiatan, tx_sp_jenis_kegiatan, tx_dp_tgl_mulai_kegiatan, tx_dp_jam_mulai_kegiatan, tx_dp_tgl_selesai_kegiatan, tx_dp_jam_selesai_kegiatan;
    private EditText tx_subject;
    private LinearLayout ll_select_customer, ll_detail_customer;
    private RobotoTextView tv_customer_code, tv_customer_name, tv_customer_address, tv_customer_city;
    private DatePickerDialog.OnDateSetListener mKegiatanStartDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
            Date selectedDate = DateUtils.stringToDate(Tanggal.FORMAT_DATE, DateUtils.dateToString(Tanggal.FORMAT_DATE, calendar.getTime()));
            Date currentDate = DateUtils.stringToDate(Tanggal.FORMAT_DATE, DateUtils.dateToString(Tanggal.FORMAT_DATE, new Date()));
            if (selectedDate.before(currentDate)) {
                Toast.makeText(activity, "Tgl. Kegiatan tidak boleh sebelum tanggal hari ini.", Toast.LENGTH_SHORT).show();
            } else {
                kegiatan.getStartDate().setDate(calendar.getTime());
                tx_dp_tgl_mulai_kegiatan.setText(kegiatan.getStartDate().getTglString(Tanggal.FORMAT_UI, new Locale("id")));
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener mKegiatanStartTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            kegiatan.getStartDate().setTime(calendar.getTime());
            tx_dp_jam_mulai_kegiatan.setText(kegiatan.getStartDate().getTglString(Tanggal.FORMAT_TIME_NO_SECOND));
        }
    };
    private DatePickerDialog.OnDateSetListener mKegiatanEndDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (calendar.getTime().before(kegiatan.getStartDate().getDate()))
                Toast.makeText(activity, "Tgl. Selesai tidak boleh sebelum Tgl. Mulai.", Toast.LENGTH_SHORT).show();
            else {
                kegiatan.getEndDate().setDate(calendar.getTime());
                tx_dp_tgl_selesai_kegiatan.setText(kegiatan.getEndDate().getTglString(Tanggal.FORMAT_UI, new Locale("id")));
            }
        }
    };
    private TimePickerDialog.OnTimeSetListener mKegiatanEndTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            kegiatan.getEndDate().setTime(calendar.getTime());
            tx_dp_jam_selesai_kegiatan.setText(kegiatan.getEndDate().getTglString(Tanggal.FORMAT_TIME_NO_SECOND));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_input_kegiatan);
        activity = this;
        isEditable = getIntent().getBooleanExtra("isEditable", true);
        initToolbar();
        initKegiatan();
        initView();
    }

    private void initKegiatan() {
        kegiatan = getIntent().getParcelableExtra("kegiatan");
        kegiatanSQLite = new KegiatanSQLite(this);
        kegiatanSQLite.delete(0);
        if (kegiatan == null) {
            kegiatan = new Kegiatan();
            Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), 0);
            kegiatan.setStartDate(calendar.getTime());
            kegiatan.setEndDate(calendar.getTime());
            kegiatan.setSalesperson(App.getUser(this));
        }
        Permiso.getInstance().setActivity(this);
    }

    private void initView() {
        this.tx_sp_tipe_kegiatan = findViewById(R.id.tx_sp_tipe_kegiatan);
        this.tx_sp_jenis_kegiatan = findViewById(R.id.tx_sp_jenis_kegiatan);
        this.tx_dp_tgl_mulai_kegiatan = findViewById(R.id.tx_dp_tgl_mulai_kegiatan);
        this.tx_dp_jam_mulai_kegiatan = findViewById(R.id.tx_dp_jam_mulai_kegiatan);
        this.tx_dp_tgl_selesai_kegiatan = findViewById(R.id.tx_dp_tgl_selesai_kegiatan);
        this.tx_dp_jam_selesai_kegiatan = findViewById(R.id.tx_dp_jam_selesai_kegiatan);
        this.tx_subject = findViewById(R.id.tx_keterangan);
        this.ll_select_customer = findViewById(R.id.ll_select_customer);
        this.ll_detail_customer = findViewById(R.id.ll_detail_customer);
        this.tv_customer_code = findViewById(R.id.tv_customer_code);
        this.tv_customer_name = findViewById(R.id.tv_customer_name);
        this.tv_customer_address = findViewById(R.id.tv_customer_address);
        this.tv_customer_city = findViewById(R.id.tv_customer_city);
        bt_simpan = findViewById(R.id.bt_simpan);

        ll_select_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        if (isEditable) {
            array_adapter_tipe = new Adapter_List_String(this, list_tipe_kegiatan);
            array_adapter_jenis = new Adapter_List_String(this, list_jenis_kunjungan);

            tx_dp_tgl_mulai_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog(mKegiatanStartDateListener, kegiatan.getStartDate().getDate());
                }
            });
            tx_dp_jam_mulai_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialog(mKegiatanStartTimeListener, kegiatan.getStartDate().getDate());
                }
            });
            tx_dp_tgl_selesai_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog(mKegiatanEndDateListener, kegiatan.getEndDate().getDate());
                }
            });
            tx_dp_jam_selesai_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTimePickerDialog(mKegiatanEndTimeListener, kegiatan.getEndDate().getDate());
                }
            });
            tx_sp_jenis_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (tx_sp_tipe_kegiatan.getText().toString().toLowerCase().contains("pilih"))
                        new AlertDialog.Builder(activity)
                                .setMessage("Pilih Tipe Kegiatan dulu.")
                                .setPositiveButton("OK", null)
                                .show();
                    else {
                        if (tx_sp_tipe_kegiatan.getText().toString().toLowerCase().equals("kunjungan"))
                            array_adapter_jenis = new Adapter_List_String(activity, list_jenis_kunjungan);
                        else
                            array_adapter_jenis = new Adapter_List_String(activity, list_jenis_korespondensi);
                        App.setAdapter(activity, array_adapter_jenis, new App.OnPilih() {
                            @Override
                            public void onPilih(String selectedText) {
                                tx_sp_jenis_kegiatan.setText(selectedText);
                                kegiatan.setJenis(selectedText);
                            }
                        });
                    }
                }
            });
            tx_sp_tipe_kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    array_adapter_tipe = new Adapter_List_String(activity, list_tipe_kegiatan);
                    App.setAdapter(activity, array_adapter_tipe, new App.OnPilih() {
                        @Override
                        public void onPilih(String selectedText) {
                            if (Utils.isEmpty(kegiatan.getTipe()) || !kegiatan.getTipe().trim().toLowerCase().equals(selectedText.trim().toLowerCase())) {
                                kegiatan.setJenis(null);
                                tx_sp_jenis_kegiatan.setText(R.string.pilih_jenis_kegiatan_hint);
                            }
                            tx_sp_tipe_kegiatan.setText(selectedText);
                            kegiatan.setTipe(selectedText);
                        }
                    });
                }
            });
            bt_simpan.setVisibility(View.VISIBLE);
        } else {
            tx_dp_tgl_mulai_kegiatan.setOnClickListener(null);
            tx_dp_jam_mulai_kegiatan.setOnClickListener(null);
            tx_dp_tgl_selesai_kegiatan.setOnClickListener(null);
            tx_dp_jam_selesai_kegiatan.setOnClickListener(null);
            tx_sp_tipe_kegiatan.setOnClickListener(null);
            tx_sp_jenis_kegiatan.setOnClickListener(null);
            bt_simpan.setVisibility(View.GONE);
        }

        tx_dp_tgl_mulai_kegiatan.setText(kegiatan.getStartDate().getTglString(Tanggal.FORMAT_UI, new Locale("id")));
        tx_dp_jam_mulai_kegiatan.setText(kegiatan.getStartDate().getTglString(Tanggal.FORMAT_TIME_NO_SECOND));
        tx_dp_tgl_selesai_kegiatan.setText(kegiatan.getEndDate().getTglString(Tanggal.FORMAT_UI, new Locale("id")));
        tx_dp_jam_selesai_kegiatan.setText(kegiatan.getEndDate().getTglString(Tanggal.FORMAT_TIME_NO_SECOND));

        setCustomer();
        tx_subject.setEnabled(isEditable);
        tx_subject.setText(kegiatan.getSubject());
        if (Utils.isEmpty(kegiatan.getTipe()))
            tx_sp_tipe_kegiatan.setText(getString(R.string.pilih_tipe_kegiatan_hint));
        else tx_sp_tipe_kegiatan.setText(kegiatan.getTipe());
        if (Utils.isEmpty(kegiatan.getJenis()))
            tx_sp_jenis_kegiatan.setText(getString(R.string.pilih_jenis_kegiatan_hint));
        else tx_sp_jenis_kegiatan.setText(kegiatan.getJenis());

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_simpan.setEnabled(false);
                kegiatan.setSubject(tx_subject.getText().toString());
                if (isValid()) {
                    kegiatan.getSalesHeader().getCustomer().setBEX(App.getUser(activity).getEmpNo());
                    new Proses_Kegiatan(activity).post(kegiatan, new Callbacks.OnCBKegiatan() {
                        @Override
                        public void onCB(RetroStatus status, Kegiatan kegiatan) {
                            bt_simpan.setEnabled(true);
                            if (status.isSuccess()) {
                                Toast.makeText(activity, status.getMessage(), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else
                                DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                        }
                    });
                } else bt_simpan.setEnabled(true);
            }
        });
    }

    private void requestPermission() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    // Permission granted!
                    Intent intent = new Intent(activity, Select_Customer_Kegiatan_Activity.class);
                    intent.putExtra("customer", kegiatan.getSalesHeader().getCustomer());
                    intent.putExtra("isEditable", isEditable);
                    startActivityForResult(intent, REQUEST_SELECT_CUSTOMER);
                } else {
                    // Permission denied
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(PERMISSION_ACCESS_FINE_LOCATION))
                            showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE_DENIED);
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE, DIALOG_MESSAGE_RATIONALE, null, callback);
            }
        }, PERMISSION_ACCESS_FINE_LOCATION);
    }

    private void showAlertDialog(String title, String message) {
        new SalamanderDialog(this)
                .setDialogTitle(title)
                .setMessage(message)
                .setPositiveButton("Aktifkan Location Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAppSetting();
                    }
                })
                .show();
    }

    private void openAppSetting() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(i, REQUEST_OPEN_SETTING);
    }

    private boolean isValid() {
        if (Utils.isEmpty(kegiatan.getTipe())) {
            tx_sp_tipe_kegiatan.setError("Pilih Tipe Kegiatan");
            tx_sp_tipe_kegiatan.requestFocus();
            Toast.makeText(activity, "Pilih Tipe Kegiatan", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Utils.isEmpty(kegiatan.getJenis())) {
            tx_sp_jenis_kegiatan.setError("Pilih Jenis Kegiatan");
            Toast.makeText(activity, "Pilih Tipe Kegiatan", Toast.LENGTH_SHORT).show();
            tx_sp_jenis_kegiatan.requestFocus();
            return false;
        } else if (kegiatan.getStartDate().getDate().after(kegiatan.getEndDate().getDate())) {
            Toast.makeText(this, "Tgl. Mulai tidak boleh lebih dari Tgl. Selesai", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showDatePickerDialog(DatePickerDialog.OnDateSetListener mListener, Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog d = new DatePickerDialog(activity, mListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        d.show();
    }

    private void showTimePickerDialog(TimePickerDialog.OnTimeSetListener mListener, Date selectedDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        TimePickerDialog d = new TimePickerDialog(activity, mListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        d.show();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Input Kegiatan");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_CUSTOMER && resultCode == RESULT_OK) {
            kegiatan.getSalesHeader().setCustomer((Customer) data.getBundleExtra("bundle").getParcelable("customer"));
            LatLng position = data.getBundleExtra("bundle").getParcelable("position");
            if (position != null) {
                //kegiatan.setLatitude(position.latitude);
                //kegiatan.setLongitude(position.longitude);
            }
            setCustomer();
        } else if (requestCode == REQUEST_OPEN_SETTING) {
            requestPermission();
        } else if (requestCode == App.GPS_SETTING) {
            checkGPS();
        }
    }

    private void setCustomer() {
        if (!Utils.isEmpty(kegiatan.getSalesHeader().getCustomer().getCode())) {
            tv_customer_code.setText(kegiatan.getSalesHeader().getCustomer().getCode());
            tv_customer_name.setText(kegiatan.getSalesHeader().getCustomer().getName());
            tv_customer_address.setText(kegiatan.getSalesHeader().getCustomer().getAddress1());
            tv_customer_city.setText(kegiatan.getSalesHeader().getCustomer().getCity());
            ll_detail_customer.setVisibility(View.VISIBLE);
        }
    }

}
