package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.object.Customer;

public class Find_Customer_Adapter extends ArrayAdapter<Customer> {

    Context context;
    ArrayList<Customer> list = new ArrayList<Customer>();
    LayoutInflater inflater;

    public Find_Customer_Adapter(Context context, int resource,
                                 ArrayList<Customer> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = objects;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        ViewHolder vHolder = null;

        if (v == null) {
            vHolder = new ViewHolder();
            v = inflater.inflate(R.layout.adapter_find_customer, parent, false);

            vHolder.tCode = (TextView) v.findViewById(R.id.search_cust_code);
            vHolder.tName = (TextView) v.findViewById(R.id.search_cust_name);
            vHolder.tAddress = (TextView) v.findViewById(R.id.search_cust_address);

            v.setTag(vHolder);
        } else
            vHolder = (ViewHolder) v.getTag();

        Customer customer = list.get(position);
        vHolder.tCode.setText(customer.getCode());
        vHolder.tName.setText(customer.getName());
        vHolder.tAddress.setText(customer.getAddress1() + "," + customer.getAddress2() + "," + customer.getCity());

        return v;
    }

    @Override
    public Customer getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    private static class ViewHolder {
        TextView tCode, tName, tAddress;
    }
}
