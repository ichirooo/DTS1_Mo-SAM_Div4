package com.salamander.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.greysonparrelli.permiso.Permiso;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Photo;
import com.salamander.mo_sam_div4_dts1.imagepicker.DefaultCallback;
import com.salamander.mo_sam_div4_dts1.imagepicker.EasyImage;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Photo;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.mo_sam_div4_dts1.sqlite.PhotoSQLite;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_base_module.widget.SalamanderDialog;
import com.salamander.salamander_network.RetroStatus;

import java.io.File;
import java.util.Date;

public class Photo_Activity_backup extends AppCompatActivity implements View.OnClickListener {

    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final int REQUEST_OPEN_SETTING = 2000;

    final String DIALOG_TITLE_STORAGE = "Access Storage";
    final String DIALOG_MESSAGE_RATIONALE_STORAGE = "Aplikasi membutuhkan permission untuk mengakses Storage.\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED_STORAGE = "Aplikasi membutuhkan permission untuk mengakses Storage.\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";

    final String DIALOG_TITLE_CAMERA = "Access Camera";
    final String DIALOG_MESSAGE_RATIONALE_CAMERA = "Aplikasi membutuhkan permission untuk mengakses Camera.\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED_CAMERA = "Aplikasi membutuhkan permission untuk mengakses Camera.\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";

    private Context context;
    private RecyclerView rv_photo;
    private PhotoSQLite photoSQLite;
    private Button bt_ok, bt_cancel;
    private LinearLayout ll_button_ok_cancel;
    private FloatingActionButton fab_tambah_photo;
    private Adapter_List_Photo adapterListPhoto;

    private Kegiatan kegiatan;
    private Photo selectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_backup);
        Permiso.getInstance().setActivity(this);
        context = this;
        initToolbar(getString(R.string.photo_title));
        int id_kegiatan = getIntent().getIntExtra("id_kegiatan", 0);
        kegiatan = new KegiatanSQLite(this).get(id_kegiatan);
        photoSQLite = new PhotoSQLite(context);
        initView();
    }

    protected void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        rv_photo = findViewById(R.id.rv_photo);
        ll_button_ok_cancel = findViewById(R.id.ll_button_ok_cancel);
        bt_ok = findViewById(R.id.bt_ok);
        bt_ok.setText(getString(R.string.action_save));
        bt_cancel = findViewById(R.id.bt_cancel);
        fab_tambah_photo = findViewById(R.id.fab_tambah_photo);

        ll_button_ok_cancel.setVisibility(View.GONE);

        refreshAdapter();
        bt_ok.setOnClickListener(this);
        fab_tambah_photo.setOnClickListener(this);
    }

    private void refreshAdapter() {
        adapterListPhoto = new Adapter_List_Photo(context, kegiatan);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getApplicationContext());
        rv_photo.setLayoutManager(mLayoutManager);
        rv_photo.setItemAnimator(new DefaultItemAnimator());
        rv_photo.setAdapter(adapterListPhoto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_tambah_photo:
                if (adapterListPhoto.getItemCount() == 5) {
                    DialogUtils.showErrorMessage(this, "Maksimum 5 foto dalam 1 kegiatan.\nSilakan hapus dulu foto yang lain untuk menambah foto lagi.", false);
                } else
                    tambahPhoto();
                break;
            case R.id.bt_ok:
                /*
                new Proses_Photo(this, false).UploadAll(photoSQLite.getAll(id_kegiatan), new Callbacks.OnCB() {
                    @Override
                    public void onCB(RetroStatus status) {
                        if (status.isSuccess()) {
                            Toast.makeText(context, "Photo berhasil disimpan ke Server", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                */
                break;
        }
    }

    private void tambahkanPhoto() {
        EasyImage.openCamera(this, 0);
    }

    private void tambahPhoto() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    // Permission granted!
                    Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                        @Override
                        public void onPermissionResult(Permiso.ResultSet resultSet) {
                            if (resultSet.areAllPermissionsGranted()) {
                                // Permission granted!
                                tambahkanPhoto();
                            } else {
                                // Permission denied
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (!shouldShowRequestPermissionRationale(PERMISSION_CAMERA))
                                        showAlertDialog(DIALOG_TITLE_CAMERA, DIALOG_MESSAGE_DENIED_CAMERA);
                                }
                            }
                        }

                        @Override
                        public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                            Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE_CAMERA, DIALOG_MESSAGE_RATIONALE_CAMERA, null, callback);
                        }
                    }, PERMISSION_CAMERA);
                } else {
                    // Permission denied
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(PERMISSION_WRITE_EXTERNAL_STORAGE))
                            showAlertDialog(DIALOG_TITLE_STORAGE, DIALOG_MESSAGE_DENIED_STORAGE);
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE_STORAGE, DIALOG_MESSAGE_RATIONALE_STORAGE, null, callback);
            }
        }, PERMISSION_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void showAlertDialog(String title, String message) {
        new SalamanderDialog(this)
                .setDialogTitle(title)
                .setMessage(message)
                .setPositiveButton("Aktifkan Permission", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openAppSetting();
                    }
                })
                .show();
    }

    private void openAppSetting() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(i, REQUEST_OPEN_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                //Handle the image
                String tgl = DateUtils.dateToString("yyyyMMddHHmmss", new Date());
                File newFile = new File(FileUtil.getPhotoDirectory(context) + File.separator + App.getUser(context).getInitial() + "_" + String.valueOf(kegiatan.getIDServer()) + "_" + tgl + "." + FileUtil.getFileExtension(imageFile));
                FileUtil.CopyFile(context, imageFile, newFile);

                Photo photo = new Photo();
                photo.setPath(newFile.getAbsolutePath());
                photo.setIDKegiatan(kegiatan.getIDServer());
                photo.setTanggal(new Tanggal(new Date()));
                //photo = photoSQLite.Post(photo);

                new Proses_Photo(context, true).Upload(photo, new Callbacks.OnCB() {
                    @Override
                    public void onCB(RetroStatus status) {
                        if (!status.isSuccess())
                            DialogUtils.showErrorNetwork(context, status.getTitle(), status.getMessage(), false);
                        refreshAdapter();
                    }
                });
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(Photo_Activity_backup.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}