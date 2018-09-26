package com.salamander.mo_sam_div4_dts1.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Item_Activity;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Sales_Order_Activity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Sales_Line;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.util.CurrencyUtils;

/**
 * Created by benny_aziz on 04/26/2017.
 */

public class Fragment_Sales_Line extends Fragment {

    public static final int REQ_NEW_ITEM = 1001;
    public static final int REQ_EDIT_ITEM = 1002;

    private Input_Sales_Order_Activity context;
    private RecyclerView rv_list_sales_line;
    private Button bt_tambahkan_item, bt_tambah_item;
    private LinearLayout ll_tidak_ada_data;
    private TextView tx_total;
    private Adapter_List_Sales_Line adapterListSalesLine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sales_line, container, false);
        context = (Input_Sales_Order_Activity) getActivity();
        setHasOptionsMenu(true);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        rv_list_sales_line = rootView.findViewById(R.id.rv_list_sales_line);
        ll_tidak_ada_data = rootView.findViewById(R.id.ll_tidak_ada_data);
        bt_tambahkan_item = rootView.findViewById(R.id.bt_tambahkan_item);
        bt_tambah_item = rootView.findViewById(R.id.bt_tambah_item);
        tx_total = rootView.findViewById(R.id.tx_total);

        refreshAdapter();

        if (context.getSalesHeader().isEditable()) {
            bt_tambahkan_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Input_Item_Activity.class);
                    intent.putExtra("requestCode", REQ_NEW_ITEM);
                    startActivityForResult(intent, REQ_NEW_ITEM);
                }
            });
            bt_tambah_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Input_Item_Activity.class);
                    intent.putExtra("requestCode", REQ_NEW_ITEM);
                    startActivityForResult(intent, REQ_NEW_ITEM);
                }
            });
        } else {
            bt_tambahkan_item.setVisibility(View.GONE);
            bt_tambah_item.setVisibility(View.GONE);
        }
    }

    private void refreshAdapter() {
        adapterListSalesLine = new Adapter_List_Sales_Line(context, context.getSalesHeader().getSalesLine());
        adapterListSalesLine.setOnItemDeletedListener(new Adapter_List_Sales_Line.OnDelete() {
            @Override
            public void onDelete(Item item, final int position) {
                new AlertDialog.Builder(context)
                        .setMessage("Hapus item '" + item.getCode() + "' ?")
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.getSalesHeader().getSalesLine().remove(position);
                                refreshAdapter();
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .create()
                        .show();
            }
        });
        if (adapterListSalesLine.getItemCount() > 0) {
            rv_list_sales_line.setVisibility(View.VISIBLE);
            ll_tidak_ada_data.setVisibility(View.GONE);
            adapterListSalesLine.setOnItemClickListener(new Adapter_List_Sales_Line.OnItemClick() {
                @Override
                public void onItemClick(Item item, int position) {
                    Intent intent = new Intent(context, Input_Item_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("item", item);
                    bundle.putInt("position", position);
                    bundle.putBoolean("isEditable", context.getSalesHeader().isEditable());
                    intent.putExtra("bundle", bundle);
                    intent.putExtra("requestCode", REQ_EDIT_ITEM);
                    startActivityForResult(intent, REQ_EDIT_ITEM);
                }
            });
            rv_list_sales_line.setAdapter(adapterListSalesLine);
            rv_list_sales_line.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
            rv_list_sales_line.setItemAnimator(new DefaultItemAnimator());
            rv_list_sales_line.setNestedScrollingEnabled(false);
        } else {
            rv_list_sales_line.setVisibility(View.GONE);
            ll_tidak_ada_data.setVisibility(View.VISIBLE);
        }

        tx_total.setText(CurrencyUtils.formatCurrency(context.getSalesHeader().getSubtotal()));
        if (context.getOnItemChanged() != null)
            context.getOnItemChanged().onItemChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            Bundle bundle = data.getBundleExtra("bundle");
            Item item = bundle.getParcelable("item");
            if (item != null) {
                item.setOrderNo(context.getSalesHeader().getOrderNo());
                item.setIDHeader(context.getSalesHeader().getIDServer());
            }
            int position = bundle.getInt("position");

            if (requestCode == REQ_NEW_ITEM) {
                context.getSalesHeader().getSalesLine().add(item);
            } else if (requestCode == REQ_EDIT_ITEM) {
                context.getSalesHeader().getSalesLine().remove(position);
                if (item != null)
                    context.getSalesHeader().getSalesLine().add(position, item);
            }
            refreshAdapter();
        }
    }
}