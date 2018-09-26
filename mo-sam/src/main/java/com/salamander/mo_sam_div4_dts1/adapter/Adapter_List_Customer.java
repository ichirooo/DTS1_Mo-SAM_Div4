package com.salamander.mo_sam_div4_dts1.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.customer.Input_Customer_Activity;
import com.salamander.mo_sam_div4_dts1.activity.customer.Master_Customer_Activity;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.sqlite.CustomerSQLite;
import com.salamander.salamander_base_module.Utils;

import java.util.ArrayList;

public class Adapter_List_Customer extends RecyclerView.Adapter<Adapter_List_Customer.CustomerViewHolder> {

    private Activity activity;
    private ArrayList<Customer> list_customer;

    public Adapter_List_Customer(Activity activity) {
        this.activity = activity;
        this.list_customer = new CustomerSQLite(activity).getAllCustomerLeadBySalesperson();
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        final Customer customer = list_customer.get(position);

        holder.tx_customer_no.setText(customer.getCode());
        holder.tx_customer_name.setText(customer.getName());
        holder.tx_customer_address.setText(customer.getAddress1() + ", " + customer.getAddress2());
        holder.tx_customer_city.setText(customer.getCity());
        holder.tx_customer_alias.setText(customer.getAlias());
        if (Utils.isEmpty(customer.getAlias()))
            holder.tx_customer_alias.setVisibility(View.GONE);
        else holder.tx_customer_alias.setVisibility(View.VISIBLE);
        holder.card_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Input_Customer_Activity.class);
                intent.putExtra("customer", customer);
                activity.startActivityForResult(intent, Master_Customer_Activity.INTENT_EDIT_CUSTOMER);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_customer.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {

        TextView tx_customer_no, tx_customer_name, tx_customer_address, tx_customer_city, tx_customer_alias;
        CardView card_customer;
        LinearLayout ll_list_customer;

        CustomerViewHolder(View itemView) {
            super(itemView);
            ll_list_customer = itemView.findViewById(R.id.ll_list_customer);
            card_customer = itemView.findViewById(R.id.card_customer);
            tx_customer_no = itemView.findViewById(R.id.tx_customer_no);
            tx_customer_name = itemView.findViewById(R.id.tx_customer_name);
            tx_customer_address = itemView.findViewById(R.id.tx_customer_address);
            tx_customer_city = itemView.findViewById(R.id.tx_customer_city);
            tx_customer_alias = itemView.findViewById(R.id.tx_customer_alias);
        }
    }
}
