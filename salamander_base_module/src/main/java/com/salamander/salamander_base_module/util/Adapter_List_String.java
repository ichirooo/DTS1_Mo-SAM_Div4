package com.salamander.salamander_base_module.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salamander.salamander_base_module.R;

import java.util.ArrayList;
import java.util.Arrays;

public class Adapter_List_String extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> list_string;

    public Adapter_List_String(Context context, ArrayList<String> list_string) {
        super(context, R.layout.adapter_list_string, list_string);
        this.context = context;
        this.list_string = list_string;
    }

    public Adapter_List_String(Context context, String[] list_string) {
        super(context, R.layout.adapter_list_string, list_string);
        this.context = context;
        this.list_string = new ArrayList<>(Arrays.asList(list_string));
    }

    @Override
    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.adapter_list_string, parent, false);

            viewHolder.text = view.findViewById(R.id.adapter_list_string_text);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String text = list_string.get(position);
        viewHolder.text.setText(text);

        return view;
    }

    @Override
    public String getItem(int position) {
        return list_string.get(position);
    }

    public ArrayList<String> getItems() {
        return list_string;
    }

    public int getItemPosition(String teks) {
        if (teks == null)
            return 0;
        for (int i = 0; i < list_string.size(); i++)
            if (list_string.get(i).equals(teks))
                return i;
        return 0;
    }

    @Override
    public int getCount() {
        return list_string.size();
    }

    private static class ViewHolder {
        TextView text;
    }
}