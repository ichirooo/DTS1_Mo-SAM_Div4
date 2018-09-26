package com.salamander.mo_sam_div4_dts1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.DrawerItem;

import java.util.List;

public class Custom_Drawer_Adapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> listDrawerItem;
    int layoutResID;

    public Custom_Drawer_Adapter(Context context, int resource,
                                 List<DrawerItem> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.layoutResID = resource;
        this.listDrawerItem = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        ViewHolder vHolder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            vHolder = new ViewHolder();

            view = inflater.inflate(layoutResID, parent, false);
            vHolder.text = (TextView) view.findViewById(R.id.drawer_itemcontent);
            vHolder.icon = (ImageView) view.findViewById(R.id.drawer_content);

            view.setTag(vHolder);

        } else {
            vHolder = (ViewHolder) view.getTag();
        }

        DrawerItem drawerItem = (DrawerItem) this.listDrawerItem.get(position);

        vHolder.icon.setImageDrawable(view.getResources().getDrawable(
                drawerItem.getimgID()));
        vHolder.text.setText(drawerItem.getitemName());

        return view;
    }

    static class ViewHolder {
        TextView text, drawerTitle;
        ImageView icon;
    }

}
