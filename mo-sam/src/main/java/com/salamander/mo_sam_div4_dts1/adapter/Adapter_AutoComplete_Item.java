package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Item;

import java.util.ArrayList;

/**
 * Created by benny_aziz on 4/21/2016.
 */
public class Adapter_AutoComplete_Item extends ArrayAdapter<Item> {

    String teks = "";
    ArrayList<Item> filteredItem = new ArrayList<>();
    private Context context;

    public Adapter_AutoComplete_Item(Context context) {
        super(context, 0, new ArrayList<Item>());
        this.context = context;
    }

    @Override
    public int getCount() {
        return filteredItem.size();
    }

    @Override
    public Filter getFilter() {
        return new ItemFilter(context, this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item from filtered list.
        Item item = filteredItem.get(position);

        View v = convertView;
        ViewHolder vHolder = null;

        if (v == null) {
            vHolder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.adapter_find_customer_old, parent, false);

            vHolder.tCode = (TextView) v.findViewById(R.id.search_cust_code);
            vHolder.tName = (TextView) v.findViewById(R.id.search_cust_name);
            vHolder.tLastPrice = (TextView) v.findViewById(R.id.search_cust_address);

            v.setTag(vHolder);
        } else
            vHolder = (ViewHolder) v.getTag();

        String str[] = {"", "", "", ""};
        int index[] = {0, 0};
        str[0] = item.getCode().toLowerCase();
        index[0] = str[0].indexOf(teks.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + teks.length();

            str[0] = item.getCode();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tCode.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3]));
        } else vHolder.tCode.setText(item.getCode());

        str[0] = item.getDescription().toLowerCase();
        index[0] = str[0].indexOf(teks.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + teks.length();

            str[0] = item.getDescription();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tName.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3]));
        } else vHolder.tName.setText(item.getDescription());

        vHolder.tLastPrice.setText("Last update Price : " + item.getInputDate());
        return v;
    }

    @Override
    public Item getItem(int position) {
        return filteredItem.get(position);
    }

    private static class ViewHolder {
        TextView tCode, tName, tLastPrice;
    }
}
