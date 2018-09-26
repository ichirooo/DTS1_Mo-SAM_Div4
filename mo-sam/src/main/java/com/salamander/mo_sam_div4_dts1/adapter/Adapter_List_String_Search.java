package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_List_String_Search extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> list_data = new ArrayList<>();

    private String searchText = "";

    public Adapter_List_String_Search(Context context, ArrayList<String> list_data) {
        super(context, android.R.layout.simple_list_item_1, list_data);
        this.context = context;
        this.list_data.addAll(list_data);
    }

    @Override
    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);

            viewHolder.tx_textview = view.findViewById(android.R.id.text1);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String data = list_data.get(position);
        viewHolder.tx_textview.setText(data);

        String str[] = {"", "", "", ""};
        int index[] = {0, 0};

        str[0] = data.toLowerCase();
        index[0] = str[0].indexOf(searchText.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + searchText.length();

            str[0] = data;
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            viewHolder.tx_textview.setText(Html.fromHtml("<b>" + str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3] + " </b>"));
        } else viewHolder.tx_textview.setText(data);
        return view;
    }

    @Override
    public String getItem(int position) {
        return list_data.get(position);
    }

    @Override
    public int getCount() {
        return list_data.size();
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    private class ViewHolder {
        TextView tx_textview;
    }
}
