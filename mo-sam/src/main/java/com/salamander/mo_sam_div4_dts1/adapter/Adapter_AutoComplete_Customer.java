package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Customer;

import java.util.ArrayList;

public class Adapter_AutoComplete_Customer extends ArrayAdapter<Customer> {

    String teks = "";
    ArrayList<Customer> filteredCustomer = new ArrayList<>();
    private Context context;
    private boolean hideCustomerLead;

    public Adapter_AutoComplete_Customer(Context context, boolean hideCustomerLead) {
        super(context, 0, new ArrayList<Customer>());
        this.context = context;
        this.hideCustomerLead = hideCustomerLead;
    }

    @Override
    public int getCount() {
        return filteredCustomer.size();
    }

    @Override
    public @NonNull
    Filter getFilter() {
        return new CustomerFilter(context, this, hideCustomerLead);
    }

    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item from filtered list.
        Customer customer = filteredCustomer.get(position);

        View v = convertView;
        ViewHolder vHolder = null;

        if (v == null) {
            vHolder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.adapter_find_customer, parent, false);

            vHolder.tCode = v.findViewById(R.id.tx_search_customer_code);
            vHolder.tName = v.findViewById(R.id.tx_search_customer_name);
            vHolder.tAddress = v.findViewById(R.id.tx_search_customer_address);

            v.setTag(vHolder);
        } else
            vHolder = (ViewHolder) v.getTag();

        String str[] = {"", "", "", ""};
        int index[] = {0, 0};
        str[0] = customer.getCode().toLowerCase();
        index[0] = str[0].indexOf(teks.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + teks.length();

            str[0] = customer.getCode();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tCode.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3] + " "));
        } else vHolder.tCode.setText(customer.getCode());

        str[0] = customer.getName().toLowerCase();
        index[0] = str[0].indexOf(teks.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + teks.length();

            str[0] = customer.getName();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tName.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3] + " "));
        } else vHolder.tName.setText(customer.getName());
        vHolder.tAddress.setText(customer.getAddress1() + "," + customer.getAddress2() + "," + customer.getCity());

        return v;
    }

    @Override
    public Customer getItem(int position) {
        return filteredCustomer.get(position);
    }

    private static class ViewHolder {
        TextView tCode, tName, tAddress;
    }
}
