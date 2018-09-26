package com.salamander.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.greysonparrelli.permiso.Permiso;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_base_module.widget.SalamanderDialog;

/**
 * Created by benny_aziz on 05/09/2017.
 */

public class ToolbarActivity extends AppCompatActivity {

    private static final int REQUEST_OPEN_SETTING = 2000;
    private static final String[] list_permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_PHONE_STATE};
    private static final String[] list_permissions_string = {"LOCATION", "STORAGE", "CAMERA", "CONTACT", "PHONE"};
    final String DIALOG_MESSAGE_RATIONALE = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";
    final String DIALOG_TITLE = "Access Permission Required";
    private Context context;
    private Activity activity;
    private SalamanderDialog salamanderDialog;
    private final BroadcastReceiver GPSListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkGPS();
            //App.startService(context);
        }
    };
    private String permission;
    private OnPermisionGranted onPermisionGranted;
    private boolean showGPSDialog = false, GPSMustActive;
    private boolean isCheckSinglePermission, isCheckAllPermission;
    private boolean isPermissionGranted;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        this.context = this;
        this.activity = this;
        Permiso.getInstance().setActivity(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            isPermissionGranted = true;
    }

    public void setGPSActivePersistent(boolean GPSMustActive) {
        this.GPSMustActive = GPSMustActive;
        this.showGPSDialog = GPSMustActive;
        if (GPSMustActive)
            checkGPS();
    }

    protected void initToolbar(String title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initToolbar(int title) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(title));
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkAllPermission() {
        checkAllPermission(true);
    }

    public void checkAllPermission(boolean showGPSDialog) {
        checkAllPermission(showGPSDialog, new OnPermisionGranted() {
            @Override
            public void onPermissionGranted() {
                initGPS();
            }
        });
    }

    public void checkSinglePermission(final String permission, final OnPermisionGranted onPermissionGranted) {
        this.permission = permission;
        this.showGPSDialog = true;
        this.onPermisionGranted = onPermissionGranted;
        this.isCheckSinglePermission = true;
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    isPermissionGranted = true;
                    if (onPermissionGranted != null) {
                        onPermissionGranted.onPermissionGranted();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(permission)) {
                            showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE_DENIED.replace("[PERMISSIONS]", getPermissionDenied(permission)));
                        }
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE, DIALOG_MESSAGE_RATIONALE.replace("[PERMISSIONS]", getPermissionDenied(permission)), null, callback);
            }
        }, permission);
    }

    public void checkAllPermission(boolean showGPSDialog, final OnPermisionGranted onPermissionGranted) {
        this.onPermisionGranted = onPermissionGranted;
        this.showGPSDialog = showGPSDialog;
        this.isCheckAllPermission = true;
        Permiso.getInstance().setActivity(this);
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    if (onPermissionGranted != null) {
                        onPermissionGranted.onPermissionGranted();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                            showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE_DENIED.replace("[PERMISSIONS]", getPermissionsDenied()));
                        }
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE, DIALOG_MESSAGE_RATIONALE.replace("[PERMISSIONS]", getPermissionsDenied()), null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void showAlertDialog(String title, String message) {
        new SalamanderDialog(context)
                .setDialogType(SalamanderDialog.DIALOG_ERROR)
                .setDialogTitle(title)
                .setMessage(message)
                .setPositiveButtonText("Aktifkan Permission")
                .setPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openAppSetting();
                    }
                })
                .cancelable(false)
                .show();
    }

    private String getPermissionDenied(String permission) {
        String list_permission_required = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            for (int i = 0; i < list_permissions.length; i++) {
                if (permission.equals(list_permissions[i]) && checkSelfPermission(list_permissions[i]) != PackageManager.PERMISSION_GRANTED)
                    list_permission_required += "\n" + "- " + list_permissions_string[i];
            }
        return list_permission_required;
    }

    private String getPermissionsDenied() {
        String list_permission_required = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            for (int i = 0; i < list_permissions.length; i++) {
                if (checkSelfPermission(list_permissions[i]) != PackageManager.PERMISSION_GRANTED)
                    list_permission_required += "\n" + "- " + list_permissions_string[i];
            }
        return list_permission_required;
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
        if (requestCode == App.GPS_SETTING) {
            checkGPS();
        } else if (requestCode == REQUEST_OPEN_SETTING)
            checkAllPermission(showGPSDialog, onPermisionGranted);
    }

    public void checkGPS() {
        if (salamanderDialog != null)
            salamanderDialog.dismiss();
        if (GPSMustActive && !App.isGPSActive(this)) {
            salamanderDialog = new SalamanderDialog(this)
                    .setDialogTitle("GPS tidak aktif.")
                    .setMessage("Silakan aktifkan GPS untuk melanjutkan.")
                    .setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), App.GPS_SETTING);
                        }
                    })
                    .cancelable(false);
            salamanderDialog.show();
        }
    }

    public void initGPS() {
        if (showGPSDialog) {
            checkGPS();
            IntentFilter filter = new IntentFilter(App.ACTION_GPS_LISTENER);
            registerReceiver(GPSListener, filter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
        initGPS();
        checkGPS();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (showGPSDialog)
                unregisterReceiver(GPSListener);
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, ToolbarActivity.class.getSimpleName() + " => onPause  => ", e);
            Log.d(App.TAG, ToolbarActivity.class.getSimpleName() + " => onPause  => " + e.toString());
        }
    }

    public interface OnPermisionGranted {
        void onPermissionGranted();
    }
}