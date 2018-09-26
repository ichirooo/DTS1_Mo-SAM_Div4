package com.salamander.mo_sam_div4_dts1.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.Input_Kegiatan_Activity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_List_Kegiatan_New;
import com.salamander.mo_sam_div4_dts1.imagepicker.DefaultCallback;
import com.salamander.mo_sam_div4_dts1.imagepicker.EasyImage;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.Photo;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Kegiatan;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Photo;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.sqlite.PhotoSQLite;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.RetroStatus;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class Fragment_Kegiatan_Today extends Fragment {

    private static final int REQUEST_CODE_EDIT_KEGIATAN = 11111;

    private Context context;
    private LinearLayout ll_tidak_ada_kegiatan;
    private Adapter_List_Kegiatan_New adapterListKegiatan;
    private RecyclerView rv_kegiatan;

    private Kegiatan currentSelected;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        this.rootView = inflater.inflate(R.layout.fragment_kegiatan_today, container, false);
        this.context = getActivity();
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        rv_kegiatan = rootView.findViewById(R.id.rv_kegiatan);
        ll_tidak_ada_kegiatan = rootView.findViewById(R.id.ll_tidak_ada_kegiatan);

        ((TextView) rootView.findViewById(R.id.tx_versions)).setText(App.getVersionText(context));
    }

    private void refreshAdapter() {
        adapterListKegiatan = new Adapter_List_Kegiatan_New(getActivity(), new Date());
        assert getActivity() != null;
        rv_kegiatan.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        rv_kegiatan.setItemAnimator(new DefaultItemAnimator());
        rv_kegiatan.setAdapter(adapterListKegiatan);
        rv_kegiatan.setNestedScrollingEnabled(false);

        if (adapterListKegiatan.getItemCount() > 0) {
            rv_kegiatan.setVisibility(View.VISIBLE);
            ll_tidak_ada_kegiatan.setVisibility(View.GONE);
        } else {
            rv_kegiatan.setVisibility(View.GONE);
            ll_tidak_ada_kegiatan.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        refreshFragment();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (rootView != null)
            refreshFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT_KEGIATAN && resultCode == Activity.RESULT_OK)
            refreshAdapter();
        else {
            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
                public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                    //Some error handling
                }

                @Override
                public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                    String tgl = DateUtils.dateToString("yyyyMMddHHmmss", new Date());
                    File newFile = new File(FileUtil.getPhotoDirectory(context) + File.separator + App.getUser(context).getInitial() + "_" + tgl + "." + FileUtil.getFileExtension(imageFile));
                    FileUtil.CopyFile(context, imageFile, newFile);

                    Photo photo = new Photo();
                    photo.setPath(newFile.getAbsolutePath());
                    photo.setIDKegiatan(currentSelected.getIDServer());
                    photo.setTanggal(new Tanggal());
                    photo = new PhotoSQLite(context).Post(photo);

                    new Proses_Photo(context, false).Upload(photo, new Callbacks.OnCB() {
                        @Override
                        public void onCB(RetroStatus status) {
                            if (status.isSuccess()) ;
                            //adapterListKegiatan.checkOutKegiatan(currentSelected);
                        }
                    });
                }

                @Override
                public void onCanceled(EasyImage.ImageSource source, int type) {
                    if (source == EasyImage.ImageSource.CAMERA) {
                        File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
                        if (photoFile != null) photoFile.delete();
                    }
                }
            });
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tambah_kegiatan:
                startActivity(new Intent(getActivity(), Input_Kegiatan_Activity.class));
                return true;
            case R.id.action_sync_kegiatan:
                String startDate;
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                startDate = DateUtils.dateToString(Tanggal.FORMAT_DATE, calendar.getTime());
                refreshKegiatan(startDate, startDate);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshKegiatan(String startDate, String endDate) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Data...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Proses_Kegiatan(getActivity()).syncKegiatan(startDate, endDate, new Callbacks.OnCB() {
            @Override
            public void onCB(RetroStatus status) {
                if (status.isSuccess()) {
                    refreshAdapter();
                } else
                    DialogUtils.showErrorNetwork(getActivity(), null, status.getMessage(), false);
                progressDialog.dismiss();
            }
        });
    }

    public void refreshFragment() {
        initView(rootView);
        refreshAdapter();
    }
}