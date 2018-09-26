package com.salamander.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.function.Hitung;
import com.salamander.mo_sam_div4_dts1.object.Item;

import java.util.List;

public class Adapter_List_Item extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> itemlist;
    private SparseBooleanArray mSelected;
    private LayoutInflater inflater;
    private int resId;

    public Adapter_List_Item(Context context, int resId, List<Item> itemlist) {
        super(context, resId, itemlist);
        // TODO Auto-generated constructor stub
        this.mSelected = new SparseBooleanArray();
        this.context = context;
        this.itemlist = itemlist;
        this.resId = resId;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(resId, null);

            viewHolder.txtCode = (TextView) view.findViewById(R.id.txtCode);
            viewHolder.txtQty = (TextView) view.findViewById(R.id.txtQty);
            viewHolder.txtUnit = (TextView) view.findViewById(R.id.txtUnit);
            viewHolder.txtPrice = (TextView) view.findViewById(R.id.txtPrice);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.txtCode.setText(itemlist.get(position).getCode());
        if (itemlist.get(position).getError() == 1) {
            viewHolder.txtCode.setTextColor(Color.parseColor("#CC0000"));
            viewHolder.txtCode.setText("*" + itemlist.get(position).getCode());
        }
        viewHolder.txtQty.setText(String.valueOf(itemlist.get(position).getQuantity()));
        viewHolder.txtUnit.setText(itemlist.get(position).getUnit());
        viewHolder.txtPrice.setText(new Hitung().formatNumber(itemlist.get(position).getPrice()));

        return view;
    }

    @Override
    public void remove(Item object) {
        // TODO Auto-generated method stub
        itemlist.remove(object);
        notifyDataSetChanged();
    }

    void selectView(int position, boolean value) {
        if (value)
            mSelected.put(position, true);
        else
            mSelected.delete(position);
        notifyDataSetChanged();
    }

    public SparseBooleanArray getSelectedId() {
        return mSelected;
    }

    public void ToggleSelection(int position) {
        selectView(position, !mSelected.get(position));
    }

    public int getSelectedCount() {
        return mSelected.size();
    }

    public void removeSelection() {
        mSelected = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public List<Item> getItems() {
        return itemlist;
    }

    static class ViewHolder {
        TextView txtCode, txtQty, txtUnit, txtPrice;
    }


}
