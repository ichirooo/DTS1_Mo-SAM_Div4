package com.salamander.mo_sam_div4_dts1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.salamander.mo_sam_div4_dts1.GlideApp;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.custom.SquareImageView;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Photo;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.PhotoSQLite;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_base_module.widget.SalamanderDialog;
import com.salamander.salamander_network.RetroStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

public class Adapter_List_Photo extends RecyclerView.Adapter<Adapter_List_Photo.PhotoHolder> {

    private Context context;
    private ArrayList<Photo> list_photo;
    private PhotoSQLite photoSQLite;

    public Adapter_List_Photo(Context context, Kegiatan kegiatan) {
        this.context = context;
        this.photoSQLite = new PhotoSQLite(context);
        this.list_photo = photoSQLite.getAll(kegiatan.getIDServer());
    }

    @Override
    public @NonNull
    PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_photo, parent, false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PhotoHolder viewHolder, int position) {
        final Photo photo = list_photo.get(position);

        viewHolder.tx_no.setText(String.valueOf(position + 1) + ".");
        viewHolder.tx_name.setText(photo.getName());
        viewHolder.tx_description.setText(photo.getDescription());
        if (photo.getTanggal() != null)
            viewHolder.tx_tanggal.setText(photo.getTanggal().getTglString(Tanggal.FORMAT_UI, new Locale("id")));

        if (!Utils.isEmpty(photo.getPath()) && new File(photo.getPath()).exists())
            GlideApp.with(context)
                    .load(new File(photo.getPath()))
                    .placeholder(R.drawable.ic_loading_photo)
                    .fitCenter()
                    .into(viewHolder.img_photo);
        else if (!Utils.isEmpty(photo.getURL()))
            GlideApp.with(context)
                    .load(photo.getURL())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.ic_loading_photo)
                    .fitCenter()
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            viewHolder.img_photo.setImageDrawable(resource);
                        }
                    });
        /*
        if (!Utils.isEmpty(photo.getPath()) && new File(photo.getPath()).exists())
            viewHolder.img_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri apkURI = FileProvider.getUriForFile(context, EasyImage.PACKAGE_NAME + ".photopick.fileprovider", new File(photo.getPath()));
                        intent.setDataAndType(apkURI, "image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + photo.getPath()), "image/*");
                    }
                    context.startActivity(intent);
                }
            });
            */

        viewHolder.bt_deskripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogKeterangan(photo);
            }
        });
        viewHolder.bt_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SalamanderDialog(context)
                        .setDialogType(SalamanderDialog.DIALOG_CONFIRMATION)
                        .setMessage("Hapus foto '" + photo.getName() + "' ?")
                        .setPositiveButton("Hapus", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Proses_Photo(context, false).Delete(photo, new Callbacks.OnCB() {
                                    @Override
                                    public void onCB(RetroStatus status) {
                                        if (status.isSuccess()) {
                                            remove(photo);
                                            photoSQLite.Delete(photo.getIDServer());
                                            Toast.makeText(context, "Photo berhasil dihapus.", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", null)
                        .show();
            }
        });
    }

    private void showDialogKeterangan(final Photo photo) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(R.layout.layout_dialog_description, null);
        final EditText tx_description = alertLayout.findViewById(R.id.tx_description);
        alertLayout.findViewById(R.id.tv_title).setVisibility(View.GONE);

        if (!Utils.isEmpty(photo.getDescription()))
            tx_description.setText(photo.getDescription());
        tx_description.setSelection(tx_description.getText().length());

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Keterangan Photo : ");
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
                        if (!Utils.isEmpty(tx_description.getText().toString())) {
                            photo.setDescription(tx_description.getText().toString());
                            new Proses_Photo(context, false).PostDescription(photo, new Callbacks.OnCB() {
                                @Override
                                public void onCB(RetroStatus status) {
                                    if (status.isSuccess()) {
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    } else
                                        DialogUtils.showErrorNetwork(context, status.getTitle(), status.getMessage(), false);
                                }
                            });
                        }
                    }
                });
            }
        });
        dialog.show();
    }

    public void remove(Photo photo) {
        list_photo.remove(photo);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list_photo.size();
    }

    public ArrayList<Photo> getItems() {
        return list_photo;
    }

    public Photo getItem(int position) {
        return list_photo.get(position);
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        SquareImageView img_photo;
        TextView tx_description, tx_tanggal, tx_no, tx_name;
        FancyButton bt_deskripsi, bt_hapus;
        ProgressBar progressBar;

        PhotoHolder(View itemView) {
            super(itemView);
            img_photo = itemView.findViewById(R.id.img_photo);
            tx_description = itemView.findViewById(R.id.tx_photo_description);
            tx_tanggal = itemView.findViewById(R.id.tx_tanggal);
            tx_no = itemView.findViewById(R.id.tx_no);
            tx_name = itemView.findViewById(R.id.tx_name);
            bt_deskripsi = itemView.findViewById(R.id.bt_deskripsi);
            bt_hapus = itemView.findViewById(R.id.bt_hapus);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
