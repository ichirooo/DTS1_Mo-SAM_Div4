package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.widget.Filter;

import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.sqlite.ItemSQLite;

import java.util.ArrayList;

public class ItemFilter extends Filter {

    private Context context;
    private Adapter_AutoComplete_Item adapter;
    private ArrayList<Item> filtered_list;

    public ItemFilter(Context context, Adapter_AutoComplete_Item adapter) {
        super();
        this.context = context;
        this.adapter = adapter;
        this.filtered_list = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filtered_list.clear();

        final FilterResults result = new FilterResults();

        if (constraint != null && constraint.length() != 0) {
            filtered_list = new ItemSQLite(context).Find(constraint.toString().trim());
        }
        result.values = filtered_list;
        result.count = filtered_list.size();
        return result;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredItem.clear();
        if (constraint != null)
            adapter.teks = constraint.toString();
        adapter.filteredItem.addAll(filtered_list);
        adapter.notifyDataSetChanged();
    }
}
