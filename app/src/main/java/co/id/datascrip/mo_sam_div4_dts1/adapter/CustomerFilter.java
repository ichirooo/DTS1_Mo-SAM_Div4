package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.widget.Filter;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Customer;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.CustomerSQLite;

public class CustomerFilter extends Filter {

    private Context context;
    private Adapter_AutoComplete_Customer adapter;
    private ArrayList<Customer> filtered_list;

    public CustomerFilter(Context context, Adapter_AutoComplete_Customer adapter) {
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
            filtered_list = new CustomerSQLite(context).Find(constraint.toString().trim());
        }
        result.values = filtered_list;
        result.count = filtered_list.size();
        return result;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.filteredCustomer.clear();
        if (constraint != null)
            adapter.teks = constraint.toString();
        adapter.filteredCustomer.addAll(filtered_list);
        adapter.notifyDataSetChanged();
    }
}
