package com.salamander.mo_sam_div4_dts1.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.Input_Kegiatan_Activity;
import com.salamander.mo_sam_div4_dts1.activity.Photo_Activity;
import com.salamander.mo_sam_div4_dts1.activity.order.Input_Sales_Order_Activity;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_base_module.util.Adapter_List_String;
import com.salamander.salamander_network.RetroStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Adapter_List_Kegiatan_New extends RecyclerView.Adapter<Adapter_List_Kegiatan_New.KegiatanViewHolder> {

    private static String[] list_status = new String[]{"OPEN", "ON PROGRESS", "CLOSED"};
    private Activity activity;
    private Date date;
    private ArrayList<Kegiatan> list_kegiatan;
    private KegiatanSQLite kegiatanSQLite;

    private OnCheckInCheckOutChange mOnCheckInCheckOutChange;
    private OnCheckOutClick mOnCheckOutClick;

    private Calendar calendar;

    public Adapter_List_Kegiatan_New(Activity activity, Date date) {
        this.activity = activity;
        this.date = date;
        kegiatanSQLite = new KegiatanSQLite(activity);
        list_kegiatan = kegiatanSQLite.getByDate(date);

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        //currentDate = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public @NonNull
    KegiatanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_kegiatan_new, parent, false);
        return new KegiatanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KegiatanViewHolder holder, int position) {
        final Kegiatan kegiatan = list_kegiatan.get(position);
        final Customer customer = kegiatan.getSalesHeader().getCustomer();
        if (!Utils.isEmpty(kegiatan.getSalesHeader().getOrderNo()))
            holder.tx_order_no.setVisibility(View.VISIBLE);
        else holder.tx_order_no.setVisibility(View.GONE);
        holder.tx_order_no.setText(kegiatan.getSalesHeader().getOrderNo());
        holder.tx_id_kegiatan.setText("ID = " + String.valueOf(kegiatan.getIDServer()));
        holder.tx_tgl_kegiatan.setText(kegiatan.getStartDate().getTglString(Tanggal.FORMAT_UI, new Locale("id")));
        holder.tx_customer_code.setText(customer.getCode());
        holder.tx_customer_name.setText(customer.getName());
        holder.tx_customer_city.setText(customer.getCity());

        if (kegiatan.isCanceled()) {
            holder.bt_check_in.setVisibility(View.GONE);
            holder.bt_check_out.setVisibility(View.GONE);
        } else {
            if (kegiatan.isCheckedIn()) {
                holder.bt_check_in.setVisibility(View.GONE);
                holder.bt_check_out.setVisibility(View.VISIBLE);
            } else {
                holder.bt_check_in.setVisibility(View.VISIBLE);
                holder.bt_check_out.setVisibility(View.GONE);
            }

            holder.bt_check_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if (App.isNotCheckedIn(activity))
                    new Proses_Kegiatan(activity).checkIn(kegiatan, new Callbacks.OnCB() {
                        @Override
                        public void onCB(RetroStatus retroStatus) {
                            if (retroStatus.isSuccess())
                                refresh();
                            else
                                DialogUtils.showErrorMessage(activity, retroStatus.getMessage(), false);
                        }
                    });
                        /*
                        new Proses_Location(activity).PostLocation(kegiatan, 1, new Callbacks.onCBLocation() {
                            @Override
                            public void onCB(RetroStatus status, Kegiatan kegiatan) {
                                if (status.isSuccess()) {
                                    Location location = App.getSession(activity).getLastLocation();
                                    //App.getSession(activity).setLastLocationCheckIn(kegiatan.getIDServer(), location.getLatitude(), location.getLongitude());
                                    App.getSession(activity).setLastCheckIn(new Date().getTime());
                                    setCheckInCheckOut(kegiatan, 1);
                                    Toast.makeText(activity, "Check In Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (status.getMessage().equals("fake_location")) {
                                        //App.setLastLocationAsMock(activity);
                                    }
                                    DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                                }
                            }
                        });
                        */
                }
            });

            holder.bt_check_out.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //if (App.isNotCheckedIn(activity))
                    new Proses_Kegiatan(activity).checkOut(kegiatan, new Callbacks.OnCB() {
                        @Override
                        public void onCB(RetroStatus retroStatus) {
                            if (retroStatus.isSuccess())
                                refresh();
                            else
                                DialogUtils.showErrorMessage(activity, retroStatus.getMessage(), false);
                        }
                    });
                }
            });
        }

        holder.card_kegiatan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final ArrayList<String> options = new ArrayList<>();
                        if (kegiatan.isCheckedIn()) {
                            if (kegiatan.isCanceled()) {
                                options.add(0, activity.getString(R.string.action_lbl_view_kegiatan));
                            } else {
                                options.add(0, activity.getString(R.string.action_lbl_edit_kegiatan));
                            }
                            options.add(1, activity.getString(R.string.action_lbl_sales_order));
                            options.add(2, activity.getString(R.string.action_lbl_photo));
                            if (kegiatan.isCanceled()) {
                                options.add(3, activity.getString(R.string.action_lbl_alasan_cancel));
                            } else {
                                options.add(3, activity.getString(R.string.action_lbl_cancel));
                            }
                            options.add(4, activity.getString(R.string.action_lbl_hasil));
                        } else {
                            if (kegiatan.isCanceled())
                                options.add(0, activity.getString(R.string.action_lbl_view_kegiatan));
                            else
                                options.add(0, activity.getString(R.string.action_lbl_edit_kegiatan));

                            options.add(1, activity.getString(R.string.action_lbl_photo));
                            if (kegiatan.isCanceled()) {
                                options.add(2, activity.getString(R.string.action_lbl_alasan_cancel));
                            } else {
                                options.add(2, activity.getString(R.string.action_lbl_cancel));
                            }
                            options.add(3, activity.getString(R.string.action_lbl_hasil));
                        }

                        App.setAdapter(activity, new Adapter_List_String(activity, options), new App.OnPilih() {
                            @Override
                            public void onPilih(String teks) {
                                switch (teks) {
                                    case "View Kegiatan":
                                        Intent intentView = new Intent(activity, Input_Kegiatan_Activity.class);
                                        intentView.putExtra("kegiatan", kegiatan);
                                        intentView.putExtra("isEditable", false);
                                        activity.startActivity(intentView);
                                        break;
                                    case "Edit Kegiatan":
                                        Intent intentEdit = new Intent(activity, Input_Kegiatan_Activity.class);
                                        intentEdit.putExtra("kegiatan", kegiatan);
                                        intentEdit.putExtra("isEditable", true);
                                        activity.startActivity(intentEdit);
                                        break;
                                    case "Sales Order":
                                        openSalesOrder(kegiatan);
                                        break;
                                    case "Cancel":
                                        showDialogCancel(kegiatan);
                                        break;
                                    case "Alasan Cancel":
                                        showDialogCancel(kegiatan);
                                        break;
                                    case "Photo":
                                        Intent intent_photo = new Intent(activity, Photo_Activity.class);
                                        intent_photo.putExtra("id_kegiatan", kegiatan.getIDServer());
                                        activity.startActivity(intent_photo);
                                        break;
                                    case "Hasil":
                                        showDialogHasil(kegiatan);
                                        break;
                                }
                            }
                        });
                    }
                }
        );

        if (!kegiatan.isCanceled()) {
            if (kegiatan.isCheckedIn()) {
                holder.bt_check_out.setVisibility(View.VISIBLE);
                holder.bt_check_in.setVisibility(View.GONE);
            } else {
                holder.bt_check_out.setVisibility(View.GONE);
                holder.bt_check_in.setVisibility(View.VISIBLE);
            }
        }

        if (kegiatan.isCanceled()) {
            holder.tx_status.setText(Kegiatan.KEGIATAN_CANCELED);
            holder.tx_status.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorCanceled));
            holder.bt_check_in.setVisibility(View.GONE);
            holder.bt_check_out.setVisibility(View.GONE);
            holder.tx_status.setVisibility(View.VISIBLE);
        }
    }

    private void openSalesOrder(Kegiatan kegiatan) {
        Intent intent = new Intent(activity, Input_Sales_Order_Activity.class);
        intent.putExtra("kegiatan", kegiatan);
        activity.startActivity(intent);
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
        list_kegiatan = kegiatanSQLite.getByDate(date);
        notifyDataSetChanged();
    }

    public void setOnDataChangeListener(OnCheckInCheckOutChange OnCheckInCheckOutChange) {
        mOnCheckInCheckOutChange = OnCheckInCheckOutChange;
    }

    public void setOnCheckOutClickListener(OnCheckOutClick onCheckOutClick) {
        this.mOnCheckOutClick = onCheckOutClick;
    }

    private void showDialogCancel(final Kegiatan kegiatan) {
        if (kegiatan.isCheckedIn()) {
            DialogUtils.showErrorMessage(activity, "Silakan Check Out dulu untuk melanjutkan", false);
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(activity);
        View alertLayout = inflater.inflate(R.layout.layout_dialog_description, null);
        final EditText tx_description = alertLayout.findViewById(R.id.tx_description);
        alertLayout.findViewById(R.id.tv_title).setVisibility(View.GONE);

        if (!Utils.isEmpty(kegiatan.getCancelReason()))
            tx_description.setText(kegiatan.getCancelReason());
        tx_description.setSelection(tx_description.getText().length());

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Alasan Cancel");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", null);
        alert.setNegativeButton("Batal", null);
        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(tx_description.getText().toString()))
                            tx_description.setError("Masukkan Alasan Cancel");
                        else {
                            kegiatan.setCancel(1);
                            kegiatan.setCancelReason(tx_description.getText().toString());
                            new Proses_Kegiatan(activity).cancel(kegiatan, new Callbacks.OnCB() {
                                @Override
                                public void onCB(RetroStatus status) {
                                    if (status.isSuccess()) {
                                        refresh();
                                        dialog.dismiss();
                                    } else
                                        DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(tx_description.getText().toString()));
        tx_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(tx_description.getText().toString()));
            }
        });
    }

    private void showDialogHasil(final Kegiatan kegiatan) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View alertLayout = inflater.inflate(R.layout.layout_dialog_description, null);
        final EditText tx_description = alertLayout.findViewById(R.id.tx_description);
        alertLayout.findViewById(R.id.tv_title).setVisibility(View.GONE);

        if (!Utils.isEmpty(kegiatan.getResult()))
            tx_description.setText(kegiatan.getResult());
        tx_description.setSelection(tx_description.getText().length());

        tx_description.setEnabled(!kegiatan.isCanceled());

        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Hasil Kegiatan : ");
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", null);
        if (!kegiatan.isCanceled())
            alert.setNegativeButton("Batal", null);
        final AlertDialog dialog = alert.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!kegiatan.isCanceled()) {
                            if (TextUtils.isEmpty(tx_description.getText().toString()))
                                tx_description.setError("Masukkan Hasil Kegiatan");
                            else {
                                kegiatan.setResult(tx_description.getText().toString());
                                kegiatan.setResultDate(new Date());
                                new Proses_Kegiatan(activity).result(kegiatan, new Callbacks.OnCB() {
                                    @Override
                                    public void onCB(RetroStatus status) {
                                        if (status.isSuccess()) {
                                            Toast.makeText(activity, "Hasil berhasil disimpan", Toast.LENGTH_SHORT).show();
                                            refresh();
                                            dialog.dismiss();
                                        } else
                                            DialogUtils.showErrorNetwork(activity, null, status.getMessage(), false);
                                    }
                                });
                            }
                        } else dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();

        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(kegiatan.isCanceled() || !TextUtils.isEmpty(tx_description.getText().toString()));
        tx_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!kegiatan.isCanceled())
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(!TextUtils.isEmpty(tx_description.getText().toString()));
            }
        });
    }

    public interface OnCheckInCheckOutChange {
        void onCheck(boolean checkIn);
    }

    public interface OnCheckOutClick {
        void onCheckOutClick(Kegiatan kegiatan);
    }

    class KegiatanViewHolder extends RecyclerView.ViewHolder {

        TextView tx_id_kegiatan, tx_tgl_kegiatan, tx_customer_code, tx_customer_name, tx_customer_city, tx_order_no, tx_status;
        LinearLayout ll_list_kegiatan, ll_kegiatan;
        CardView card_kegiatan;

        ImageButton bt_check_in, bt_check_out;

        KegiatanViewHolder(View rootView) {
            super(rootView);
            this.tx_order_no = rootView.findViewById(R.id.tx_order_no);
            this.tx_id_kegiatan = rootView.findViewById(R.id.tx_id_kegiatan);
            this.tx_tgl_kegiatan = rootView.findViewById(R.id.tx_tgl_kegiatan);
            this.tx_customer_code = rootView.findViewById(R.id.tx_customer_code);
            this.tx_customer_name = rootView.findViewById(R.id.tx_customer_name);
            this.tx_customer_city = rootView.findViewById(R.id.tx_customer_city);
            this.ll_kegiatan = rootView.findViewById(R.id.ll_kegiatan);
            this.tx_status = rootView.findViewById(R.id.tx_status);
            this.card_kegiatan = rootView.findViewById(R.id.card_kegiatan);
            this.ll_list_kegiatan = rootView.findViewById(R.id.ll_list_kegiatan);
            this.bt_check_in = rootView.findViewById(R.id.bt_check_in);
            this.bt_check_out = rootView.findViewById(R.id.bt_check_out);
        }
    }

}