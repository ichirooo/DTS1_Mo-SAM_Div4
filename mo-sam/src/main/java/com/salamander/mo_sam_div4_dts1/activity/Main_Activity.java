package com.salamander.mo_sam_div4_dts1.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.GlideApp;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.SessionManager;
import com.salamander.mo_sam_div4_dts1.activity.customer.Master_Customer_Activity;
import com.salamander.mo_sam_div4_dts1.fragment.Fragment_Kegiatan_Today;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_location.LocationActivity;
import com.salamander.salamander_logger.LoggingExceptionHandler;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main_Activity extends LocationActivity implements LocationActivity.OnLocationChanged, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_ALL_PERMISSION = 123;
    private static final int REQUEST_OPEN_SETTING = 2222;

    private Context context;
    private Toolbar toolbar;

    private Location prevLocation;
    private OnLocationChanged onChangeLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler(this));
        setContentView(R.layout.activity_main);
        context = this;
        initToolbar();
        initFirebase();
        initUser();
        initView();
    }

    private void initUser() {
        SessionManager sessionManager = new SessionManager(this);
        User user = App.getUser(this);
        if (user == null)
            sessionManager.LogoutUser();
        else if (user.getEmpNo() == 0)
            sessionManager.LogoutUser();
    }

    private void initView() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        Menu menu = navigationView.getMenu();

        View headerView = navigationView.getHeaderView(0);
        final CircleImageView img_photo = headerView.findViewById(R.id.imageView);
        TextView tx_no = headerView.findViewById(R.id.tx_no);
        TextView tx_initial = headerView.findViewById(R.id.tx_initial);
        TextView tx_fullname = headerView.findViewById(R.id.tx_fullname);
        TextView tx_email = headerView.findViewById(R.id.tx_email);

        User user = App.getUser(this);
        tx_no.setText(String.valueOf(user.getEmpNo()));
        tx_initial.setText(user.getInitial());
        tx_fullname.setText(user.getFullname());
        //tx_email.setText(user.getEmail());

        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        String URL_PHOTO = "https://intraweb.datascrip.co.id/komint/download.php?path=\\\\PDI\\DATA\\PROG\\HRO\\Photo\\jpg\\&f=" + user.getPhoto();
        try {
            GlideApp.with(this)
                    .load(URL_PHOTO.replace(" ", "%20"))
                    .placeholder(R.drawable.ic_action_person)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate()
                    .into(img_photo);
        } catch (Exception e) {
            Log.d("MainActivity", "initView: " + e.toString());
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Fragment_Kegiatan_Today()).commit();
    }

    private void initFirebase() {

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onNavigationItemSelected(final @NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openSelectionDrawerItem(item.getItemId());
            }
        }, 250);
        return true;
    }

    private void openSelectionDrawerItem(int itemId) {
        switch (itemId) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new Fragment_Kegiatan_Today()).commit();
                break;
            case R.id.nav_history_kegiatan:
                startActivity(new Intent(this, History_Kegiatan_Activity.class));
                break;
            case R.id.nav_customer:
                startActivity(new Intent(this, Master_Customer_Activity.class));
                break;
            case R.id.nav_setting:
                //startActivity(new Intent(this, Setting_Activity.class));
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setMessage("Log Out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                App.getSession(context).LogoutUser();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Location prevLocation = App.getSession(Main_Activity.this).getLastLocation();
            if (prevLocation != null) {
                if ((prevLocation.getLatitude() != location.getLatitude()) || (prevLocation.getLongitude() != location.getLongitude())) {
                    App.getSession(Main_Activity.this).setLastLocation(location.getTime(), location.getLatitude(), location.getLongitude());
                    if (onChangeLocationListener != null)
                        onChangeLocationListener.onLocationChanged(location);
                }
            } else {
                App.getSession(Main_Activity.this).setLastLocation(location.getTime(), location.getLatitude(), location.getLongitude());
                if (onChangeLocationListener != null)
                    onChangeLocationListener.onLocationChanged(location);
            }
        }
    }

    public void setOnLocationChanged(OnLocationChanged onLocationChanged) {
        this.onChangeLocationListener = onLocationChanged;
    }

    public interface OnLocationChanged {
        void onLocationChanged(Location location);
    }
}
