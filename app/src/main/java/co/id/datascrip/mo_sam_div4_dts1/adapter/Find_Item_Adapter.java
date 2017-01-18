package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.object.Item;

/**
 * Created by benny_aziz on 02/17/2015.
 */
public class Find_Item_Adapter extends ArrayAdapter<Item> {

    private Context context;
    private ArrayList<Item> list = new ArrayList<Item>();
    private int resource;
    private LayoutInflater inflater;
    private String searchText = "";

    public Find_Item_Adapter(Context context, int resource, ArrayList<Item> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = objects;
        this.resource = resource;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        ViewHolder vHolder = null;

        if (v == null) {
            vHolder = new ViewHolder();
            v = inflater.inflate(R.layout.adapter_find_item, null);

            vHolder.tCode = (TextView) v.findViewById(R.id.search_item_code);
            vHolder.tDesc = (TextView) v.findViewById(R.id.search_item_name);

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

            vHolder.tCode.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3]));
        } else vHolder.tCode.setText(item.getCode());

        str[0] = item.getDesc().toLowerCase();
        index[0] = str[0].indexOf(searchText.toLowerCase());
        if (index[0] >= 0) {
            index[1] = index[0] + searchText.length();

            str[0] = item.getDesc();
            str[1] = str[0].substring(0, index[0]);
            str[2] = str[0].substring(index[0], index[1]);
            str[3] = str[0].substring(index[1], str[0].length());

            vHolder.tDesc.setText(Html.fromHtml(str[1] + "<font color='#32ba46'>" + str[2] + "</font>" + str[3]));
        } else vHolder.tDesc.setText(item.getDesc());

        return v;
    }

    @Override
    public Item getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tCode, tDesc;
    }
}
