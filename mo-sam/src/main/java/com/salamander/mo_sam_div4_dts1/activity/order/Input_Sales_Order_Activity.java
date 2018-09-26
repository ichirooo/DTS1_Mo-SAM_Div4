package com.salamander.mo_sam_div4_dts1.activity.order;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.ToolbarActivity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_ViewPager_Sales_Order;
import com.salamander.mo_sam_div4_dts1.custom.CustomTabLayout.TabLayoutWithArrow;
import com.salamander.mo_sam_div4_dts1.fragment.Fragment_Sales_Header;
import com.salamander.mo_sam_div4_dts1.fragment.Fragment_Sales_Line;
import com.salamander.mo_sam_div4_dts1.fragment.Fragment_Sales_Total;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Sales_Header;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.RetroStatus;

public class Input_Sales_Order_Activity extends ToolbarActivity {

    private Activity context;
    private Kegiatan kegiatan;
    private TabLayoutWithArrow tab_order;
    private ViewPager view_pager_order;
    private OnItemChanged onItemChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sales_order);
        context = this;
        initToolbar(R.string.input_order_title);
        this.kegiatan = getIntent().getParcelableExtra("kegiatan");
        if (kegiatan.getSalesHeader() == null) {
            this.kegiatan.setSalesHeader(new SalesHeader());
            this.kegiatan.getSalesHeader().setIDServer(new SalesHeaderSQLite(this).getTempID());
            kegiatan.setSalesperson(App.getUser(this));
        }
        kegiatan.getSalesHeader().setIDKegiatan(kegiatan.getIDServer());

        if (Utils.isEmpty(kegiatan.getSalesHeader().getOrderNo()))
            new Proses_Sales_Header(this, "Generate No Order...").generateNoOrder(kegiatan.getSalesHeader().getIDServer(), new Callbacks.OnCBGenerateNoOrder() {
                @Override
                public void onCB(RetroStatus status, String no_order) {
                    if (status.isSuccess()) {
                        kegiatan.getSalesHeader().setOrderNo(no_order);
                        initView();
                    } else
                        DialogUtils.showErrorNetwork(context, null, status.getMessage(), true);
                }
            });
        else
            new Proses_Sales_Header(context, "Checking Status...").checkOrderStatus(kegiatan.getSalesHeader().getIDServer(), new Callbacks.OnCBCheckOrderStatus() {
                @Override
                public void onCB(RetroStatus status, int sales_status) {
                    if (status.isSuccess()) {
                        if (sales_status == SalesHeader.STATUS_ORDER_OPEN || sales_status == SalesHeader.STATUS_ORDER_ANDROID_EDIT || sales_status == SalesHeader.STATUS_ORDER_COS) {
                            kegiatan.getSalesHeader().setStatus(sales_status);
                            new SalesHeaderSQLite(context).post(kegiatan.getSalesHeader());
                            initView();
                        }
                    } else
                        DialogUtils.showErrorNetwork(context, null, status.getMessage(), true);
                }
            });
    }

    public SalesHeader getSalesHeader() {
        return this.kegiatan.getSalesHeader();
    }

    public void setSalesHeader(SalesHeader salesHeader) {
        this.kegiatan.setSalesHeader(salesHeader);
    }

    private void setupViewPager() {
        Adapter_ViewPager_Sales_Order adapter = new Adapter_ViewPager_Sales_Order(getSupportFragmentManager());
        adapter.addFragment(new Fragment_Sales_Header(), "SALES HEADER");
        adapter.addFragment(new Fragment_Sales_Line(), "SALES LINE");
        adapter.addFragment(new Fragment_Sales_Total(), "TOTAL");
        view_pager_order.setAdapter(adapter);
        view_pager_order.setOffscreenPageLimit(3);
        view_pager_order.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {
                final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                if (getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        tab_order = findViewById(R.id.tab_order);
        view_pager_order = findViewById(R.id.view_pager_order);
        setupViewPager();
        tab_order.setupWithViewPager(view_pager_order);
        View root = tab_order.getChildAt(0);
        if (root instanceof LinearLayout) {
            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(ContextCompat.getColor(this, R.color.WHITE));
            drawable.setSize(2, 1);
            ((LinearLayout) root).setDividerPadding(1);
            ((LinearLayout) root).setDividerDrawable(drawable);
        }

        checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION, new OnPermisionGranted() {
            @Override
            public void onPermissionGranted() {
                initGPS();
            }
        });
        setGPSActivePersistent(true);
    }

    @Override
    public void onBackPressed() {
        if (kegiatan.getSalesHeader().isEditable())
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

    public OnItemChanged getOnItemChanged() {
        return this.onItemChanged;
    }

    public void setOnItemChangedListener(OnItemChanged onItemChanged) {
        this.onItemChanged = onItemChanged;
    }

    public interface OnItemChanged {
        void onItemChanged();
    }
}