package co.id.datascrip.mo_sam_div4_dts1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;
import co.id.datascrip.mo_sam_div4_dts1.object.SalesHeader;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.ItemSQLite;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.SalesHeaderSQLite;

public class Adapter_List_Kegiatan extends ArrayAdapter<Kegiatan> {

    private Context context;
    private List<Kegiatan> keglist;
    private SparseBooleanArray mSelected;
    private LayoutInflater inflater;
    private int resId;

    public Adapter_List_Kegiatan(Context context, int resId, List<Kegiatan> keglist) {
        super(context, resId, keglist);
        // TODO Auto-generated constructor stub
        mSelected = new SparseBooleanArray();
        this.context = context;
        this.keglist = keglist;
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

            viewHolder.ID = (TextView) view.findViewById(R.id.lkID);
            viewHolder.CCode = (TextView) view.findViewById(R.id.lkCCode);
            viewHolder.CName = (TextView) view.findViewById(R.id.lkCName);
            viewHolder.Waktu = (TextView) view.findViewById(R.id.lkWaktu);
            viewHolder.No = (TextView) view.findViewById(R.id.lkNoOrder);
            viewHolder.iconCancel = (ImageView) view.findViewById(R.id.icon_cancel);
            viewHolder.iconCOS = (ImageView) view.findViewById(R.id.icon_cos);
            viewHolder.iconCheckIn = (ImageView) view.findViewById(R.id.icon_checkin);
            viewHolder.iconSaved = (ImageView) view.findViewById(R.id.icon_saved);
            viewHolder.layout = (LinearLayout) view.findViewById(R.id.layout_keg);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.ID.setText("ID = " + Integer.toString(keglist.get(position).getID()));

        viewHolder.CCode.setText(keglist.get(position).getSalesHeader().getCustomer().getCode());
        viewHolder.CName.setText(keglist.get(position).getSalesHeader().getCustomer().getName());
        viewHolder.Waktu.setText(keglist.get(position).getStartDate().substring(0, 16));

        setIconVisible(viewHolder, keglist.get(position));

        boolean isError = new ItemSQLite(context).isError(keglist.get(position).getSalesHeader().getHeaderID());

        if (isError)
            viewHolder.No.setTextColor(Color.RED);

        return view;
    }

    @Override
    public void remove(Kegiatan object) {
        // TODO Auto-generated method stub
        keglist.remove(object);
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelected = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelected.put(position, value);
        else
            mSelected.delete(position);
        notifyDataSetChanged();
    }

    public List<Kegiatan> getItems() {
        return keglist;
    }

    public void ToggleSelection(int position) {
        selectView(position, !mSelected.get(position));
    }

    public int getSelectedCount() {
        return mSelected.size();
    }

    public SparseBooleanArray getSelectedId() {
        return mSelected;
    }

    private void setIconVisible(ViewHolder vHolder, Kegiatan k) {
        SalesHeaderSQLite s = new SalesHeaderSQLite(context);
        SalesHeader h = s.get(k.getID());

        if (h != null) {
            if (h.getOrderNo() == null) {
                vHolder.No.setVisibility(View.GONE);
                vHolder.iconSaved.setVisibility(View.GONE);
            } else {
                if (h.getOrderNo().contains("XXX") || h.getHeaderID() >= Const.DEFAULT_HEADER_ID) {
                    vHolder.iconSaved.setVisibility(View.VISIBLE);
                    vHolder.No.setVisibility(View.GONE);
                    vHolder.layout.setBackgroundResource(R.drawable.bg_light_blue);
                } else {
                    vHolder.No.setVisibility(View.VISIBLE);
                    vHolder.iconSaved.setVisibility(View.GONE);
                    vHolder.No.setText(h.getOrderNo());
                }
            }

            if (k.getCheckIn() == 1) {
                vHolder.iconCheckIn.setVisibility(View.VISIBLE);
                vHolder.iconCancel.setVisibility(View.GONE);
                vHolder.iconCOS.setVisibility(View.GONE);
            } else vHolder.iconCheckIn.setVisibility(View.GONE);

            if (k.getCancel() != 0) {
                if (h.getOrderNo() != null)
                    if (h.getOrderNo().contains("XXX")) {
                        new SalesHeaderSQLite(context).Delete(k.getID());
                        vHolder.iconSaved.setVisibility(View.GONE);
                    }
                vHolder.layout.setBackgroundResource(R.drawable.bg_light_red);
                vHolder.iconCancel.setVisibility(View.VISIBLE);
                vHolder.iconCheckIn.setVisibility(View.GONE);
                vHolder.iconCOS.setVisibility(View.GONE);
            }

            if (h.getStatus() == 1) {
                vHolder.layout.setBackgroundResource(R.drawable.bg_light_green);
                vHolder.iconCOS.setVisibility(View.VISIBLE);
                vHolder.iconCancel.setVisibility(View.GONE);
                vHolder.iconCheckIn.setVisibility(View.GONE);
            } else vHolder.iconCOS.setVisibility(View.GONE);
        } else vHolder.No.setVisibility(View.GONE);

    }

    @Override
    public Kegiatan getItem(int position) {
        return keglist.get(position);
    }

    static class ViewHolder {
        TextView ID, CCode, CName, Waktu, No;
        ImageView iconCancel, iconCOS, iconCheckIn, iconSaved;
        LinearLayout layout;
    }
}
