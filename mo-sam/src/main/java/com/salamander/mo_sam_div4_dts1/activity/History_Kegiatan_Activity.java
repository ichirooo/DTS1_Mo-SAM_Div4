package com.salamander.mo_sam_div4_dts1.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Kegiatan;
import com.salamander.mo_sam_div4_dts1.custom.DialogGoCalendarKegiatan;
import com.salamander.mo_sam_div4_dts1.object.Holiday;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.HolidaySQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.RetroStatus;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class History_Kegiatan_Activity extends ToolbarActivity {

    private static final int REQUEST_CODE_EDIT_KEGIATAN = 11111;

    private Activity activity;
    private TextView tv_tanggal, tx_tidak_ada_kegiatan;
    private LinearLayout ll_tidak_ada_kegiatan;
    private RecyclerView rv_kegiatan;
    private Adapter_List_Kegiatan adapterListKegiatan;

    private Date selectedDate;
    private DialogGoCalendarKegiatan dialogGoCalendarKegiatan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_kegiatan);
        this.activity = this;
        selectedDate = new Date();
        initToolbar(getString(R.string.title_activity_history_kegiatan));
        initView();
    }

    private void initView() {
        tv_tanggal = findViewById(R.id.tv_tanggal);
        tx_tidak_ada_kegiatan = findViewById(R.id.tx_tidak_ada_kegiatan);
        ll_tidak_ada_kegiatan = findViewById(R.id.ll_tidak_ada_kegiatan);
        rv_kegiatan = findViewById(R.id.rv_kegiatan);
        tv_tanggal.setText(DateUtils.dateToString(Tanggal.FORMAT_UI, selectedDate, new Locale("id")));

        initDialogCalendar();
        tv_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogGoCalendarKegiatan.show();
            }
        });
    }

    private void initDialogCalendar() {
        dialogGoCalendarKegiatan = new DialogGoCalendarKegiatan(this);
        dialogGoCalendarKegiatan.setDate(new Date());
        dialogGoCalendarKegiatan.setOnOKClickListener(new DialogGoCalendarKegiatan.OnOKClickListener() {
            @Override
            public void onOKClickListener(Date selectDate) {
                if (selectDate != null) {
                    selectedDate.setTime(selectDate.getTime());
                    tv_tanggal.setText(DateUtils.dateToString(Tanggal.FORMAT_UI, selectedDate, new Locale("id")));
                    refreshAdapter();
                } else Toast.makeText(activity, "Silakan pilih tanggal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshAdapter() {
        adapterListKegiatan = new Adapter_List_Kegiatan(this, selectedDate);
        rv_kegiatan.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_kegiatan.setItemAnimator(new DefaultItemAnimator());
        rv_kegiatan.setAdapter(adapterListKegiatan);
        rv_kegiatan.setNestedScrollingEnabled(false);

        if (adapterListKegiatan.getItemCount() > 0) {
            rv_kegiatan.setVisibility(View.VISIBLE);
            ll_tidak_ada_kegiatan.setVisibility(View.GONE);
        } else {
            rv_kegiatan.setVisibility(View.GONE);
            ll_tidak_ada_kegiatan.setVisibility(View.VISIBLE);
            Holiday holiday = new HolidaySQLite(this).get(selectedDate);
            if (holiday == null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    tx_tidak_ada_kegiatan.setText("Libur hari Minggu");
                    tx_tidak_ada_kegiatan.setTextColor(ContextCompat.getColor(this, R.color.holiday));
                } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    tx_tidak_ada_kegiatan.setText("Libur hari Sabtu");
                    tx_tidak_ada_kegiatan.setTextColor(ContextCompat.getColor(this, R.color.holiday));
                } else {
                    tx_tidak_ada_kegiatan.setText("- Tidak ada kegiatan -");
                    tx_tidak_ada_kegiatan.setTextColor(ContextCompat.getColor(this, R.color.black));
                }
            } else {
                tx_tidak_ada_kegiatan.setText(holiday.getDescription());
                tx_tidak_ada_kegiatan.setTextColor(ContextCompat.getColor(this, R.color.holiday));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_KEGIATAN && resultCode == Activity.RESULT_OK)
            refreshAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history_kegiatan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync_kegiatan) {
            String startDate, endDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
            startDate = DateUtils.dateToString(Tanggal.FORMAT_DATE, calendar.getTime());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.getActualMaximum(Calendar.DATE));
            endDate = DateUtils.dateToString(Tanggal.FORMAT_DATE, calendar.getTime());
            refreshKegiatan(startDate, endDate);
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshKegiatan(String startDate, String endDate) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Proses_Kegiatan(this).syncKegiatan(startDate, endDate, new Callbacks.OnCB() {
            @Override
            public void onCB(RetroStatus status) {
                if (status.isSuccess()) {
                    initDialogCalendar();
                    refreshAdapter();
                } else
                    DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                progressDialog.dismiss();
            }
        });
    }
}
