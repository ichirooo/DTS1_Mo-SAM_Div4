package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;
import co.id.datascrip.mo_sam_div4_dts1.object.Log_Feed;

/**
 * Created by benny_aziz on 03/05/2015.
 */
public class Adapter_List_Log extends ArrayAdapter<Log_Feed> {

    private Context context;
    private int resource;
    private ArrayList<Log_Feed> logs;
    private LayoutInflater inflater;


    public Adapter_List_Log(Context context, int resource, ArrayList<Log_Feed> logs) {
        super(context, resource, logs);
        this.context = context;
        this.resource = resource;
        this.logs = logs;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public
    @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        ViewHolder viewHolder = null;

        if (root == null) {
            viewHolder = new ViewHolder();
            root = inflater.inflate(R.layout.adapter_list_log, parent, false);

            viewHolder.txDate = (RobotoTextView) root.findViewById(R.id.al_date);
            viewHolder.txNote = (RobotoTextView) root.findViewById(R.id.al_note);
            viewHolder.layout = (LinearLayout) root.findViewById(R.id.al_layout);
            viewHolder.layout_bg = (LinearLayout) root.findViewById(R.id.layout_bg);

            root.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) root.getTag();
        }

        viewHolder.txDate.setText(logs.get(position).getDate());
        viewHolder.txNote.setText(logs.get(position).getFeedback().getNote());

        if (logs.get(position).getFeedback().getBex().getEmpNo().trim().equals(Global.getBEX(context).getEmpNo())) {
            viewHolder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.log_bex_background));
            viewHolder.layout_bg.setGravity(Gravity.END);
            viewHolder.txDate.setGravity(Gravity.END);
            viewHolder.txNote.setGravity(Gravity.END);
        } else {
            viewHolder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.log_admin_background));
            viewHolder.layout_bg.setGravity(Gravity.START);
            viewHolder.txDate.setGravity(Gravity.START);
            viewHolder.txNote.setGravity(Gravity.START);
        }

        return root;
    }

    private class ViewHolder {
        RobotoTextView txDate, txNote;
        LinearLayout layout, layout_bg;
    }
}
