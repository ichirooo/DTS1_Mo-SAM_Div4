package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.util.CurrencyUtils;

import java.util.ArrayList;

public class Adapter_Cari_Item extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> list;
    private LayoutInflater inflater;

    private String searchText = "";

    public Adapter_Cari_Item(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder vHolder = null;

        if (v == null) {
            vHolder = new ViewHolder();
            v = inflater.inflate(R.layout.adapter_find_item, parent, false);

            vHolder.tCode = v.findViewById(R.id.tx_search_item_code);
            vHolder.tName = v.findViewById(R.id.tx_search_item_description);
            vHolder.tPrice = v.findViewById(R.id.tx_search_item_price);

            v.setTag(vHolder);
        } else
            vHolder = (ViewHolder) v.getTag();

        Item item = list.get(position);

        String str[] = {"", "", "", ""};
        int index[] = {0, 0};
        str[0] = item.getCode().toLowerCase();
        index[0] = str[0].indexOf(searchText.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + searchText.length();

            str[0] = item.getCode();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tCode.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3] + " "));
        } else vHolder.tCode.setText(item.getCode());

        str[0] = item.getDescription().toLowerCase();
        index[0] = str[0].indexOf(searchText.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + searchText.length();

            str[0] = item.getDescription();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tName.setText(Html.fromHtml("<b>" + str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3] + " </b>"));
        } else vHolder.tName.setText(item.getDescription());
        vHolder.tPrice.setText("@ " + CurrencyUtils.formatCurrency(item.getPriceNAV()));

        return v;
    }

    @Override
    public Item getItem(int position) {
        return list.get(position);
    }

    public void setSearchText(String teks) {
        this.searchText = teks;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tCode, tName, tPrice;
    }
}
