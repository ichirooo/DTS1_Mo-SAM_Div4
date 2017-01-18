package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Adapter_List_Photo;
import co.id.datascrip.mo_sam_div4_dts1.callback.CallbackPhoto;
import co.id.datascrip.mo_sam_div4_dts1.custom.QuickImagePick;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoButton;
import co.id.datascrip.mo_sam_div4_dts1.object.Photo;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Photo;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.PhotoSQLite;

public class Input_Photo_Activity extends AppCompatActivity {

    private static final int CAMERA_PIC_REQUEST = 111;
    private static final int GALLERY_PIC_REQUEST = 222;

    private Context context;
    private ListView lv_photos;
    private LinearLayout layout_button;
    private RobotoButton btTambah, btUpload;
    private Adapter_List_Photo adapter;
    private ArrayList<Photo> photos;
    private int id_kegiatan;
    final QuickImagePick.Callback mCallback = new QuickImagePick.Callback() {

        @Override
        public void onImagePicked(@NonNull final QuickImagePick.PickSource pPickSource, final int pRequestType, @NonNull final Uri pImageUri) {
            Log.d("mCallback", "Picked: " + pImageUri.toString() + "\nMIME type: " + QuickImagePick.getMimeType(context,
                    pImageUri) + "\nFile extension: " + QuickImagePick.getFileExtension(context, pImageUri) + "\nRequest type: " + pRequestType);

            File file = QuickImagePick.getCurrentFile();
            Photo photo = new Photo();
            photo.setIdKegiatan(id_kegiatan);
            photo.setPath(file.getPath());
            new PhotoSQLite(context).Post(photo);
            refresh();
        }

        @Override
        public void onError(@NonNull final QuickImagePick.PickSource pPickSource, final int pRequestType, @NonNull final String pErrorString) {
            Log.d("mCallback", "Err: " + pErrorString);
        }

        @Override
        public void onCancel(@NonNull final QuickImagePick.PickSource pPickSource, final int pRequestType) {
            Log.d("mCallback", "Cancel: " + pPickSource.name());
        }
    };
    private Uri mcapturedImgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        context = this;
        initToolbar();

        lv_photos = (ListView) findViewById(R.id.ip_lvPhoto);
        btTambah = (RobotoButton) findViewById(R.id.ip_btTambah);
        btUpload = (RobotoButton) findViewById(R.id.ip_btUpload);
        layout_button = (LinearLayout) findViewById(R.id.ip_layout_button);

        setEnabled(getIntent().getBooleanExtra("canEdit", true));
        id_kegiatan = getIntent().getIntExtra("id_kegiatan", 0);
        refresh();

        lv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final Photo photo = photos.get(i);
                ArrayAdapter<String> action = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, new String[]{"Open Image", "Input Description", "Delete"});
                AlertDialog.Builder d = new AlertDialog.Builder(context);
                d.setTitle("Choose Action :");
                d.setAdapter(action, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                if (new File(photo.getPath()).exists()) {
                                    Intent intentViewPhoto = new Intent();
                                    intentViewPhoto.setAction(Intent.ACTION_VIEW);
                                    intentViewPhoto.setDataAndType(Uri.fromFile(new File(photo.getPath())), "image/*");
                                    startActivity(intentViewPhoto);
                                } else
                                    Toast.makeText(context, "Gambar tidak ditemukan",
                                            Toast.LENGTH_LONG).show();
                                break;

                            case 1:
                                final EditText input = new EditText(context);
                                input.setHint("Informasi Photo");
                                input.setText(photo.getInfo());
                                new AlertDialog.Builder(context)
                                        .setTitle("Decription")
                                        .setView(input)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                photo.setInfo(input.getText().toString());
                                                new PhotoSQLite(context).Post(photo);
                                                refresh();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                                break;
                            case 2:
                                new AlertDialog.Builder(context)
                                        .setTitle("Confirm")
                                        .setMessage("Delete this photo?")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                new PhotoSQLite(context).Delete(photo);
                                                refresh();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                                break;
                        }
                    }
                }).show();
            }
        });

        btTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photos.size() < 5) {
                    ArrayAdapter<String> source = new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, new String[]{"Camera", "Gallery"});
                    AlertDialog.Builder d = new AlertDialog.Builder(context);
                    d.setTitle("Choose Photo Source :");
                    d.setAdapter(source, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            final File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MoSAM/DTS1");
                            QuickImagePick.setCameraPicsDirectory(context, outDir.getAbsolutePath());
                            QuickImagePick.allowOnlyLocalContent(context, false);
                            final String[] types = {QuickImagePick.MIME_TYPE_IMAGES_ALL};
                            QuickImagePick.setAllowedMimeTypes(context, types);
                            switch (which) {
                                case 0:
                                    /*
                                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    mcapturedImgUri = Utils.getOutputMediaFileUri();
                                    intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, mcapturedImgUri);
                                    startActivityForResult(intentCamera, CAMERA_PIC_REQUEST);
                                    */
                                    QuickImagePick.pickFromCamera(Input_Photo_Activity.this);
                                    break;

                                case 1:
                                    /*
                                    Intent intentGallery = new Intent(Intent.ACTION_PICK,
                                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(intentGallery, GALLERY_PIC_REQUEST);
                                    */
                                    QuickImagePick.pickFromGallery(Input_Photo_Activity.this);
                                    break;
                            }
                        }
                    }).show();
                } else
                    Toast.makeText(context, "Maximum 5 photo.", Toast.LENGTH_LONG).show();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                new Proses_Photo(context, new CallbackKegiatan.CBPhoto() {
                    @Override
                    public void onCB(boolean hasil) {
                        if (hasil) {
                            for (int i = 0; i < photos.size(); i++) {
                                photos.get(i).setPosted(1);
                                new PhotoSQLite(context).Post(photos.get(i));
                            }
                            Toast.makeText(context, "Photo successfully upload", Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    }
                }).execute(photos);
               */
                new Process_Photo(context).Upload(photos, new CallbackPhoto.CBUploadPhoto() {
                    @Override
                    public void onCB(boolean success) {
                        if (success) {
                            for (int i = 0; i < photos.size(); i++) {
                                photos.get(i).setPosted(1);
                                new PhotoSQLite(context).Post(photos.get(i));
                            }
                            Toast.makeText(context, "Photos successfully upload", Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    }
                });
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void refresh() {
        /*
        new Process_Photo.Load_SQLite_Photo(this, new CallbackPhoto.CBLoadSQLitePhoto() {
            @Override
            public void onCB(ArrayList<Photo> list_photos, Adapter_List_Photo adapter_list_photo) {
                photos = list_photos;
                adapter = adapter_list_photo;
                lv_photos.setAdapter(adapter);
            }
        }).execute(id_kegiatan);
        */
        photos = new PhotoSQLite(context).gets(id_kegiatan);
        adapter = new Adapter_List_Photo(this, R.layout.adapter_list_photo, photos);
        lv_photos.setAdapter(adapter);
    }

    private void setEnabled(boolean canEdit) {
        if (canEdit)
            layout_button.setVisibility(View.VISIBLE);
        else
            layout_button.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!QuickImagePick.handleActivityResult(this, requestCode, resultCode, data, mCallback)) {
            super.onActivityResult(requestCode, resultCode, data);
            /*
            if (resultCode == RESULT_OK) {
                switch (requestCode) {

                    case CAMERA_PIC_REQUEST:
                        new Process_Photo.Get_Camera_Photo(this, new CallbackPhoto.CBCamera() {
                            @Override
                            public void onCB(Photo photo) {
                                refresh();
                            }
                        }, id_kegiatan).execute(mcapturedImgUri);
                        break;

                    case GALLERY_PIC_REQUEST:
                        if (data != null) {
                            String path = Utils.getRealPathFromURI(this, data.getData());
                            Photo photo = new Photo();
                            photo.setIdKegiatan(id_kegiatan);
                            photo.setPath(path);
                            if (path != null)
                                new PhotoSQLite(this).Post(photo);
                        }
                        refresh();
                        break;
                }
            }
        */
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
