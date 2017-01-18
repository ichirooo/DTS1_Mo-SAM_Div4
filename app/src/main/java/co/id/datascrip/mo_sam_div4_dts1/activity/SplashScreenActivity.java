package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.app.Dialog;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.SessionManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 1111;
    private static final int REQUEST_ALL_PERMISSION = 123;
    private static final int REQUEST_OPEN_SETTING = 2222;
    private final int SPLASH_DURATION = 1 * 1500;
    private SessionManager session;
    private Intent i;
    private boolean alreadyAsk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkAndRequestPermission()) {
                init();
            }
        } else
            init();
    }

    private boolean checkAndRequestPermission() {
        int permissionFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionReadStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionAccount = ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);

        final ArrayList<String> listPermissionNeeded = new ArrayList<>();
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionReadStorage != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCamera != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.CAMERA);
        if (permissionAccount != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.GET_ACCOUNTS);

        if (!listPermissionNeeded.isEmpty()) {
            if (!alreadyAsk) {
                new AlertDialog.Builder(this)
                        .setMessage("Aplikasi memerlukan permission untuk dapat berjalan.\nSilakan klik OK untuk melanjutkan.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SplashScreenActivity.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_ALL_PERMISSION);
                            }
                        })
                        .setCancelable(false)
                        .show();
                alreadyAsk = true;
            } else
                ActivityCompat.requestPermissions(SplashScreenActivity.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_ALL_PERMISSION);
            return false;
        }
        return true;
    }

    private void openAppSetting() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(i, REQUEST_OPEN_SETTING);
    }

    private void init() {
        if (checkServices()) {
            Global.terms = new ArrayList<>();

            Global.photo_path = getDir("photo", Context.MODE_PRIVATE);
            if (!Global.photo_path.exists()) {
                Global.photo_path.mkdirs();
            }

            session = new SessionManager(getApplicationContext());
            if (session.isLogin()) {
                i = new Intent(getApplicationContext(), MainActivity.class);
            } else i = new Intent(getApplicationContext(), Login_Activity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    finish();
                }
            }, SPLASH_DURATION);
        }
    }

    private boolean checkServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = googleApiAvailability.getErrorDialog(this, resultCode, ERROR_DIALOG_REQUEST);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finish();
                    }
                });
                dialog.show();
                return false;
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ALL_PERMISSION:

                Map<String, Integer> permission = new HashMap<>();
                permission.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                permission.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);

                Map<String, String> permission_teks = new HashMap<>();
                permission_teks.put(Manifest.permission.ACCESS_FINE_LOCATION, "Location");
                permission_teks.put(Manifest.permission.ACCESS_COARSE_LOCATION, "Location");
                permission_teks.put(Manifest.permission.READ_EXTERNAL_STORAGE, "Storage");
                permission_teks.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Storage");
                permission_teks.put(Manifest.permission.CAMERA, "Camera");
                permission_teks.put(Manifest.permission.GET_ACCOUNTS, "Contact");

                String missing_permission = "";

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        permission.put(permissions[i], grantResults[i]);
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            if (!missing_permission.contains(permission_teks.get(permissions[i])))
                                missing_permission += "\n - " + permission_teks.get(permissions[i]);
                        }
                    }
                    if (!missing_permission.equals(""))
                        missing_permission = "\nMissing Permisssion : " + missing_permission;

                    if (permission.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                        init();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {
                            new AlertDialog.Builder(this)
                                    .setMessage("Aplikasi tidak dapat dijalankan tanpa semua permission.\nSilakan coba lagi atau keluar.\n" + missing_permission)
                                    .setPositiveButton("Coba Lagi", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            checkAndRequestPermission();
                                        }
                                    })
                                    .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        } else {
                            new AlertDialog.Builder(this)
                                    .setMessage(("Aplikasi tidak dapat dijalankan tanpa semua permission.\nSilakan aktifkan semua permission dengan cara klik Setting -> Permissions.\n" + missing_permission))
                                    .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            openAppSetting();
                                        }
                                    })
                                    .setNegativeButton("Keluar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();
                                        }
                                    })
                                    .setCancelable(false)
                                    .show();
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        boolean reset = intent.getBooleanExtra("reset", false);
        int notif_id = intent.getIntExtra("notif_id", 0);
        if (reset) {
            Global.notifID = Global.notifID + 1;
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_SETTING) {
            if (checkAndRequestPermission())
                init();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}