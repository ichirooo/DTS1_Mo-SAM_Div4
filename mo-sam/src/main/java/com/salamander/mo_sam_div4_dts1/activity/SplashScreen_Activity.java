package com.salamander.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greysonparrelli.permiso.Permiso;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.Const;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Data;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_location.BlackListAppManager;
import com.salamander.salamander_logger.LoggingExceptionHandler;
import com.salamander.salamander_network.DownloadCertificate;
import com.salamander.salamander_network.RetroStatus;

import java.io.File;

public class SplashScreen_Activity extends AppCompatActivity {

    private static final int REQUEST_OPEN_SETTING = 2000;
    private static final long SPLASH_DURATION = 500; //2 * 1000
    private static final String[] list_permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE};
    private static final String[] list_permissions_string = {"LOCATION", "STORAGE", "CAMERA", "PHONE"};
    final String DIALOG_MESSAGE_RATIONALE = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik OK untuk melanjutkan.";
    final String DIALOG_MESSAGE_DENIED = "Aplikasi membutuhkan permission : \n[PERMISSIONS]\n\nKlik tombol di bawah untuk melanjutkan, lalu pilih Permission";
    final String DIALOG_TITLE = "Access Permission Required";
    private Context context;
    private TextView tx_current_version, tx_new_version;
    private LinearLayout ll_version_outdated, ll_loading;
    private Handler handler;
    private String linkDownload;
    private Button bt_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_splash_screen);
        context = this;
        Permiso.getInstance().setActivity(this);
        handler = new Handler();
        initView();
    }

    private void downloadCertificate() {
        File file = new File(Const.CERTIFICATE_PATH);
        new File(file.getParent()).mkdirs();
        new DownloadCertificate(this, file, new DownloadCertificate.PostDownload() {
            @Override
            public void downloadDone(String errorMessage, File downloadedFile) {
                if (errorMessage != null) {
                    if (errorMessage.equals("class java.net.ProtocolException => unexpected end of stream"))
                        downloadCertificate();
                    else DialogUtils.showErrorMessage(context, errorMessage, true);
                } else if (downloadedFile != null) {
                    App.getSession(context).setCertFile(downloadedFile);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkVersion();
                        }
                    }, 500);
                } else DialogUtils.showErrorMessage(context, "File not Found", true);
            }
        }).execute();
    }

    private void checkVersion() {
        new Proses_Data(this, false).checkVersion(new Callbacks.OnCBCheckVersion() {
            @Override
            public void onCB(RetroStatus retroStatus, String version, final String link_download) {
                if (retroStatus.isSuccess()) {
                    if (!Utils.isEmpty(version)) {
                        if (!version.trim().toLowerCase().equals(App.getVersion(context).trim().toLowerCase())) {
                            linkDownload = link_download;
                            tx_current_version.setText(getString(R.string.current_version) + " " + App.getVersion(context));
                            tx_new_version.setText(getString(R.string.new_version) + " " + version);

                            ll_loading.setVisibility(View.GONE);
                            ll_version_outdated.setVisibility(View.VISIBLE);

                            bt_update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkDownload));
                                    startActivity(intent);
                                }
                            });
                            return;
                        }
                    }
                } else {
                    DialogUtils.showErrorNetwork(context, String.valueOf(retroStatus.getStatusCode()), retroStatus.getMessage(), true);
                    return;
                }
                doNext();
            }
        });
    }

    private void checkPermission() {
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.areAllPermissionsGranted()) {
                    //doNext();
                    downloadCertificate();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                                !shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                            showAlertDialog(DIALOG_TITLE, DIALOG_MESSAGE_DENIED.replace("[PERMISSIONS]", getPermissionsDenied()));
                        }
                    }
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog(DIALOG_TITLE, DIALOG_MESSAGE_RATIONALE.replace("[PERMISSIONS]", getPermissionsDenied()), null, callback);
            }
        }, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void showAlertDialog(String title, String message) {
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aktifkan Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppSetting();
                    }
                })
                .create();
        dialog.show();
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
        if (requestCode == REQUEST_OPEN_SETTING)
            checkPermission();
    }

    private void doNext() {
        BlackListAppManager.getBlackListApp(App.createRetrofit(this), this, App.getURL(this), new BlackListAppManager.OnCB() {
            @Override
            public void onCB(RetroStatus status) {
                gotoMain();
            }
        });
    }

    private void gotoMain() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (App.getSession(context).isLogin() && App.getUser(context).getEmpNo() != 0) {
                    intent = new Intent(context, Main_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    intent = new Intent(context, Login_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                finish();
                startActivity(intent);

            }
        }, SPLASH_DURATION);
    }

    private void initView() {
        TextView txAppName = findViewById(R.id.tx_app_name);
        TextView txVersion = findViewById(R.id.tx_version);
        tx_current_version = findViewById(R.id.tx_current_version);
        tx_new_version = findViewById(R.id.tx_new_version);
        ll_loading = findViewById(R.id.ll_loading);
        ll_version_outdated = findViewById(R.id.ll_version_outdated);
        bt_update = findViewById(R.id.bt_update);

        txVersion.setText(App.getVersionText(this));
        txAppName.setText(getString(R.string.app_name));
        checkPermission();
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
}