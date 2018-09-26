package com.salamander.mo_sam_div4_dts1.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Adapter_List_Kegiatan extends RecyclerView.Adapter<Adapter_List_Kegiatan.KegiatanViewHolder> {

    private Activity activity;
    private String date;
    private ArrayList<Kegiatan> list_kegiatan;
    private KegiatanSQLite kegiatanSQLite;

    private Date currentDate;

    public Adapter_List_Kegiatan(Activity activity, Date currentDate) {
        this.activity = activity;
        this.currentDate = currentDate;
        kegiatanSQLite = new KegiatanSQLite(activity);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        list_kegiatan = kegiatanSQLite.Read(calendar, Const.KEGIATAN_BY_DATE);
    }

    @Override
    public @NonNull
    KegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_kegiatan, parent, false);
        return new KegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position) {
        final Kegiatan kegiatan = list_kegiatan.get(position);
        final Customer customer = kegiatan.getSalesHeader().getCustomer();
        holder.tx_tgl_kegiatan.setText(kegiatan.getStartDate().getTglString());
        holder.tx_id_kegiatan.setText("ID = " + String.valueOf(kegiatan.getIDServer()));
        holder.tx_customer_code.setText(customer.getCode());
        holder.tx_customer_name.setText(customer.getName());
        holder.tx_customer_address.setText(customer.getAddress1() + " " + customer.getAddress2());

        holder.card_kegiatan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ArrayList<String> options = new ArrayList<>();
                        if (kegiatan.isCanceled())
                            options.add("View Kegiatan");
                        else {
                            if (kegiatan.getStartDate().getDate().before(currentDate))
                                options.add("View Kegiatan");
                            else
                                options.add("Edit Kegiatan");
                        }
                        options.add("Sales Order");
                        options.add("Photo");
                        if (!kegiatan.isCanceled())
                            options.add("Cancel");
                        options.add("Hasil");
/*
                        App.setAdapter(activity, new Adapter_List_String(activity, options), new App.OnPilih() {
                            @Override
                            public void onPilih(String teks) {
                                switch (teks) {
                                    case "View Kegiatan":
                                        Intent intentView = new Intent(activity, Input_Kegiatan_Activity_backup.class);
                                        intentView.putExtra("kegiatan", kegiatan);
                                        intentView.putExtra("isEditable", false);
                                        activity.startActivity(intentView);
                                        break;
                                    case "Edit Kegiatan":
                                        Intent intentEdit = new Intent(activity, Input_Kegiatan_Activity_backup.class);
                                        intentEdit.putExtra("kegiatan", kegiatan);
                                        intentEdit.putExtra("isEditable", true);
                                        activity.startActivity(intentEdit);
                                        break;
                                    case "Sales Order":
                                        Intent intentSalesOrder = new Intent(activity, Input_Sales_Order_Activity.class);
                                        intentSalesOrder.putExtra("kegiatan", kegiatan);
                                        intentSalesOrder.putExtra("isEditable", true);
                                        activity.startActivity(intentSalesOrder);
                                        break;
                                    case "Cancel":
                                        showDialogCancel(kegiatan);
                                        break;
                                    case "Hasil":
                                        if (App.getUser(activity).isShowQuestion())
                                            showDialogHasilDiv7(kegiatan);
                                        else showDialogHasilMASDiv2(kegiatan);
                                        break;
                                    case "Photo":
                                        Intent intent_photo = new Intent(activity, Photo_Activity.class);
                                        intent_photo.putExtra("id_kegiatan", kegiatan.getIDServer());
                                        activity.startActivity(intent_photo);
                                        break;
                                }
                            }
                        });*/
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return list_kegiatan.size();
    }

    public Kegiatan getItem(int position) {
        return list_kegiatan.get(position);
    }

    public void remove(Kegiatan object) {
        list_kegiatan.remove(object);
        notifyDataSetChanged();
    }

    private void refresh() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        list_kegiatan = kegiatanSQLite.Read(calendar, Const.KEGIATAN_BY_DATE);
        notifyDataSetChanged();
    }

    class KegiatanViewHolder extends RecyclerView.ViewHolder {

        TextView tx_id_kegiatan, tx_customer_code, tx_customer_name, tx_customer_address, tx_tgl_kegiatan, tx_cancel;
        //ImageView img_check_in, img_check_out;
        LinearLayout ll_list_kegiatan, ll_kegiatan;//, ll_check_in, ll_check_out;
        CardView card_kegiatan;
        ImageButton bt_check_in, bt_check_out;

        KegiatanViewHolder(View rootView) {
            super(rootView);
            this.tx_tgl_kegiatan = rootView.findViewById(R.id.tx_tgl_kegiatan);
            this.tx_id_kegiatan = rootView.findViewById(R.id.tx_id_kegiatan);
            this.tx_customer_code = rootView.findViewById(R.id.tx_customer_code);
            this.tx_customer_name = rootView.findViewById(R.id.tx_customer_name);
            this.tx_customer_address = rootView.findViewById(R.id.tx_customer_address);
            this.ll_kegiatan = rootView.findViewById(R.id.ll_kegiatan);
            this.tx_cancel = rootView.findViewById(R.id.tx_cancel);
            this.card_kegiatan = rootView.findViewById(R.id.card_kegiatan);
            this.ll_list_kegiatan = rootView.findViewById(R.id.ll_list_kegiatan);
        }
    }
}