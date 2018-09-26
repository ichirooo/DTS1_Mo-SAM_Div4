package com.salamander.mo_sam_div4_dts1.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.custom.CustomViewPager;
import com.salamander.mo_sam_div4_dts1.custom.TabsPagerAdapter;
import com.salamander.mo_sam_div4_dts1.fragment.SalesHeaderFragment;
import com.salamander.mo_sam_div4_dts1.fragment.SalesLineFragment;
import com.salamander.mo_sam_div4_dts1.fragment.SalesTotalFragment;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_logger.LoggingExceptionHandler;

public class Input_Order_Activity extends AppCompatActivity {

    public static CustomViewPager viewPager;

    public static Kegiatan kegiatan;
    public static boolean canEdit;
    public static String no_order;

    public static double SubTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_input_order);
        initToolbar();

        canEdit = getIntent().getBooleanExtra("canEdit", true);

        kegiatan = new KegiatanSQLite(this).get(getIntent().getIntExtra("id_kegiatan", 0));

        if (kegiatan.getSalesHeader().getOrderNo() == null) {
            kegiatan.getSalesHeader().setOrderNo(getIntent().getStringExtra("no_order"));
            kegiatan.getSalesHeader().setCurrency("IDR");
            if (App.defaultTerms != null && kegiatan.getSalesHeader().getTempo() == null)
                kegiatan.getSalesHeader().setTempo(App.defaultTerms);
        } else if (kegiatan.getSalesHeader().getOrderNo().contains("XXX")) {
            kegiatan.getSalesHeader().setOrderNo(getIntent().getStringExtra("no_order"));
            kegiatan.getSalesHeader().setCurrency("IDR");
            if (App.defaultTerms != null && kegiatan.getSalesHeader().getTempo() == null)
                kegiatan.getSalesHeader().setTempo(App.defaultTerms);
        }
        no_order = kegiatan.getSalesHeader().getOrderNo();

        //initialization
        viewPager = (CustomViewPager) findViewById(R.id.pager);
        setupViewPager();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SalesHeaderFragment(), "SALES HEADER");
        adapter.addFragment(new SalesLineFragment(), "SALES LINE");
        adapter.addFragment(new SalesTotalFragment(), "TOTAL");
        viewPager.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (canEdit)
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Cancel Input Order?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setResult(RESULT_CANCELED);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}