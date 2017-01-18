package co.id.datascrip.mo_sam_div4_dts1.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.id.datascrip.mo_sam_div4_dts1.Global;
import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.SessionManager;
import co.id.datascrip.mo_sam_div4_dts1.adapter.Custom_Drawer_Adapter;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;
import co.id.datascrip.mo_sam_div4_dts1.fragment.FragmentHistory;
import co.id.datascrip.mo_sam_div4_dts1.fragment.FragmentHome;
import co.id.datascrip.mo_sam_div4_dts1.fragment.FragmentSetting;
import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationLibrary;
import co.id.datascrip.mo_sam_div4_dts1.object.DrawerItem;
import co.id.datascrip.mo_sam_div4_dts1.service.ServiceManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ALL_PERMISSION = 123;
    private static final int REQUEST_OPEN_SETTING = 2222;
    public static List<DrawerItem> dataList;
    private static long back_pressed;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle, mTitle;
    private Custom_Drawer_Adapter adapter;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkAndRequestPermission()) {
                Global.initLocationLibrary(this);
                LocationLibrary.forceLocationUpdate(this);
                onResume();
            }
        } else {
            Global.initLocationLibrary(this);
            LocationLibrary.forceLocationUpdate(this);
            onResume();
        }

        initView(savedInstanceState);
    }

    private void initToolbar() {

    }

    private boolean checkAndRequestPermission() {
        int permissionFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionWriteStorage = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        final ArrayList<String> listPermissionNeeded = new ArrayList<>();
        if (permissionFineLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCoarseLocation != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED)
            listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (!listPermissionNeeded.isEmpty()) {
            new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("Aplikasi memerlukan permission untuk dapat berjalan.\nSilakan klik OK untuk melanjutkan, \nkemudian pilih 'Allow' untuk semua permission yang muncul.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), REQUEST_ALL_PERMISSION);
                        }
                    })
                    .setCancelable(false)
                    .show();
            return false;
        }
        return true;
    }

    private void initView(Bundle savedInstanceState) {
        TextView username = (TextView) findViewById(R.id.username);
        TextView fullname = (TextView) findViewById(R.id.fullname);
        ImageView photo = (ImageView) findViewById(R.id.imgP);
        TextView txVersion = (TextView) findViewById(R.id.main_txversion);
        Button btLogout = (Button) findViewById(R.id.btLogout);
        RobotoTextView tx_version = (RobotoTextView) findViewById(R.id.main_tx_version);

        txVersion.setText(Global.getVersionText(this));
        tx_version.setText(Global.getVersionText(this));

        //initializing
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        LinearLayout mLinear = (LinearLayout) findViewById(R.id.linear);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setSelector(android.R.color.holo_blue_dark);
        mTitle = mDrawerTitle = getTitle();
        dataList = new ArrayList<>();
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        dataList.add(new DrawerItem("Home", R.drawable.home));
        dataList.add(new DrawerItem("Semua Kegiatan", R.drawable.ic_action_settings));
        dataList.add(new DrawerItem("Setting", R.drawable.ic_action_refresh));

        adapter = new Custom_Drawer_Adapter(this, R.layout.custom_drawer_item, dataList);
        mDrawerList.setAdapter(adapter);

        session = new SessionManager(getApplicationContext());
        if (Global.getBEX(this) == null)
            finish();

        username.setText(Global.getBEX(this).getInitial());
        fullname.setText(Global.getBEX(this).getFullname());

        if (Global.getBEX(this).getPhoto() != null) {
            Glide.with(this)
                    .load(Global.getURL(this) + "../assets/photo/" + Global.getBEX(this).getPhoto().replaceAll(" ", "%20"))
                    .into(photo);
        }

        for (int i = 0; i < Global.term.length; i++) {
            Global.terms.add(Global.term[i] + " HR");
        }

        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                mDrawerLayout.closeDrawers();
                SelectItem(position);
            }
        });

        mLinear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

        btLogout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                (new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK))
                        .setMessage("Log Out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                ServiceManager.stopService(MainActivity.this);
                                //unregisterReceiver(lftBroadcastReceiver);
                                session.LogoutUser();
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        setActionBarDrawer();

        if (savedInstanceState == null) {
            SelectItem(0);
        }
    }

    public void SelectItem(int position) {
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentHome()).commit();
                break;

            case 1:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentHistory()).commit();
                break;

            case 2:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new FragmentSetting()).commit();
                break;
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getitemName());
    }

    public void setActionBarDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View v) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View dView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ServiceManager.startService(this);
        Global.registerReceiver(this);
        LocationLibrary.startAlarmAndListener(this);
        LocationLibrary.forceLocationUpdate(this);
        /*
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkAndRequestPermission()) {
                ServiceManager.startService(this);
                Global.registerReceiver(this);
                LocationLibrary.startAlarmAndListener(this);
                LocationLibrary.forceLocationUpdate(this);
            }
        } else {
            ServiceManager.startService(this);
            Global.registerReceiver(this);
            LocationLibrary.startAlarmAndListener(this);
            LocationLibrary.forceLocationUpdate(this);
        }
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Global.unregisterReceiver(this);
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

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        permission.put(permissions[i], grantResults[i]);

                    if (permission.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            permission.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
                        Global.initLocationLibrary(this);
                        LocationLibrary.forceLocationUpdate(this);
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.GET_ACCOUNTS)) {
                            new android.support.v7.app.AlertDialog.Builder(this)
                                    .setMessage("Aplikasi tidak dapat dijalankan tanpa semua permission.\nSilakan coba lagi atau keluar.")
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
                            new android.support.v7.app.AlertDialog.Builder(this)
                                    .setMessage("Aplikasi tidak dapat dijalankan tanpa semua permission.\nSilakan aktifkan semua permission dari menu Setting -> App -> Permissions.")
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

    private void openAppSetting() {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(i, REQUEST_OPEN_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Global.GPS_SETTING) {
            //checkGPS();
            return;
        } else if (requestCode == REQUEST_OPEN_SETTING) {
            if (checkAndRequestPermission()) {
                LocationLibrary.forceLocationUpdate(this);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
