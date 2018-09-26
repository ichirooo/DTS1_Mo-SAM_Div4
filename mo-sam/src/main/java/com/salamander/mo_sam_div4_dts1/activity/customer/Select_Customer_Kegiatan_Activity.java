package com.salamander.mo_sam_div4_dts1.activity.customer;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.ToolbarActivity;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_AutoComplete_Customer;
import com.salamander.mo_sam_div4_dts1.adapter.Adapter_Cari_Customer;
import com.salamander.mo_sam_div4_dts1.location.Constant;
import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Customer;
import com.salamander.mo_sam_div4_dts1.proses.callback.Callbacks;
import com.salamander.mo_sam_div4_dts1.util.FileUtil;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.RetroStatus;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geocoding.utils.LocationAddress;

public class Select_Customer_Kegiatan_Activity extends ToolbarActivity implements OnMapReadyCallback {

    private static final int REQUEST_NEW_CUSTOMER = 1000;
    public Intent placeIntent;
    private Context context;
    private AutoCompleteTextView tx_cari_customer;
    private Button bt_cari_customer;
    private TextView tv_customer_code, tv_customer_name, tv_customer_address, tv_customer_city;
    private CardView card_detail_customer;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Button bt_ok, bt_cancel;
    private LinearLayout ll_cari_customer, ll_map, ll_button_ok_cancel;
    private ProgressBar progressBar;
    private Customer customer;
    private AlertDialog dialog;
    private boolean isEditable;
    private Adapter_AutoComplete_Customer adapter_autocomplete;
    private AddressResultReceiver mResultReceiver;
    private int currentIndexSearch = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer_kegiatan);
        context = this;
        initToolbar(R.string.select_customer_title);
        customer = getIntent().getParcelableExtra("customer");
        isEditable = getIntent().getBooleanExtra("isEditable", true);
        initView();
        setCustomer();
        mResultReceiver = new AddressResultReceiver(null);
        checkSinglePermission(Manifest.permission.ACCESS_FINE_LOCATION, new OnPermisionGranted() {
            @Override
            public void onPermissionGranted() {
                initGPS();
            }
        });
        setGPSActivePersistent(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_customer:
                startActivityForResult(new Intent(this, Input_Customer_Activity.class), REQUEST_NEW_CUSTOMER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if (customer.getLatitude() == 0 && customer.getLongitude() == 0) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            } else
                                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                            if (mLastLocation != null) {
                                customer.setLatitude(mLastLocation.getLatitude());
                                customer.setLongitude(mLastLocation.getLongitude());
                            }
                        }
                        gotoLocation(customer.getLatitude(), customer.getLongitude(), 15);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEditable)
            getMenuInflater().inflate(R.menu.menu_input_kegiatan, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        this.googleMap = mMap;
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setAllGesturesEnabled(true);

        gotoLocation(customer.getLatitude(), customer.getLongitude(), 15);
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        CameraPosition target = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))
                .zoom(zoom)
                .tilt(25)
                .build();
        try {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(target);
            googleMap.animateCamera(update, null);
        } catch (Exception e) {
            FileUtil.writeExceptionLog(context, Select_Customer_Kegiatan_Activity.class.getSimpleName() + " => gotoLocation  => ", e);
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        ll_cari_customer = findViewById(R.id.ll_cari_customer);
        ll_button_ok_cancel = findViewById(R.id.ll_button_ok_cancel);
        tx_cari_customer = findViewById(R.id.tx_cari_customer);
        bt_cari_customer = findViewById(R.id.bt_cari_customer);
        tv_customer_code = findViewById(R.id.tv_customer_code);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_customer_address = findViewById(R.id.tv_customer_address);
        tv_customer_city = findViewById(R.id.tv_customer_city);
        card_detail_customer = findViewById(R.id.card_detail_customer);
        progressBar = findViewById(R.id.progressBar);
        ll_map = findViewById(R.id.ll_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        bt_ok = findViewById(R.id.bt_ok);
        bt_cancel = findViewById(R.id.bt_cancel);

        mapFragment.getMapAsync(this);
        buildGoogleApiClient();

        adapter_autocomplete = new Adapter_AutoComplete_Customer(this, false);
        tx_cari_customer.setAdapter(adapter_autocomplete);

        tx_cari_customer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tx_cari_customer.requestFocus();
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(tx_cari_customer.getWindowToken(), 0);

                customer = adapter_autocomplete.getItem(i);
                setCustomer();
                if (customer.getLatitude() == 0 || customer.getLongitude() == 0) {
                    if (mLastLocation != null)
                        gotoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 15);
                } else
                    gotoLocation(customer.getLatitude(), customer.getLongitude(), 15);
            }
        });

        bt_cari_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tx_cari_customer.requestFocus();
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(tx_cari_customer.getWindowToken(), 0);

                if (!TextUtils.isEmpty(tx_cari_customer.getText().toString())) {
                    new Proses_Customer(context).find(tx_cari_customer.getText().toString(), new Callbacks.onCBCustomers() {
                        @Override
                        public void onCB(RetroStatus status, ArrayList<Customer> list_customer) {
                            if (status.isSuccess()) {
                                showDialog(list_customer);
                            } else
                                DialogUtils.showErrorNetwork(context, null, status.getMessage(), false);
                        }
                    });
                }
            }
        });

        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isEmpty(customer.getCode())) {
                    DialogUtils.showErrorMessage(context, "Silakan pilih Customer.", false);
                } else {
                    customer.setLatitude(googleMap.getCameraPosition().target.latitude);
                    customer.setLongitude(googleMap.getCameraPosition().target.longitude);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("customer", customer);
                    bundle.putParcelable("position", new LatLng(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude));
                    intent.putExtra("bundle", bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (isEditable) {
            if (customer != null)
                ll_button_ok_cancel.setVisibility(View.VISIBLE);
            else ll_button_ok_cancel.setVisibility(View.GONE);
            ll_cari_customer.setVisibility(View.VISIBLE);
        } else {
            ll_cari_customer.setVisibility(View.GONE);
            ll_button_ok_cancel.setVisibility(View.GONE);
        }
    }

    private void showDialog(final ArrayList<Customer> customers) {
        Adapter_Cari_Customer adapter = new Adapter_Cari_Customer(this, R.layout.adapter_find_customer_old, customers);
        adapter.setSearchText(tx_cari_customer.getText().toString());
        (new AlertDialog.Builder(this))
                .setTitle("Pilih Customer : ")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        customer = customers.get(i);
                        setCustomer();
                        if (customer.getLatitude() == 0 || customer.getLongitude() == 0) {
                            currentIndexSearch = 1;
                            getPlacesLocation(customer.getAddress1());
                        } else
                            gotoLocation(customer.getLatitude(), customer.getLongitude(), 15);
                    }

                })
                .show();
    }

    private void getPlacesLocation(String address) {
        /*
        placeIntent = new Intent(context, GeocodeAddressIntentService.class);
        placeIntent.putExtra(Constant.RECEIVER, mResultReceiver);
        placeIntent.putExtra(Constant.LOCATION_NAME_DATA_EXTRA, address);
        progressBar.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForegroundService(placeIntent);
        else startService(placeIntent);
        */
        SmartLocation.with(this)
                .geocoding()
                .direct(address, new OnGeocodingListener() {
                    @Override
                    public void onLocationResolved(String s, List<LocationAddress> list) {
                        if (list.size() > 0)
                            list.get(0);
                    }
                });
    }

    private void setCustomer() {
        if (!(customer == null || customer.getCode() == null || customer.getCode().trim().isEmpty())) {
            ll_map.setVisibility(View.VISIBLE);
            card_detail_customer.setVisibility(View.VISIBLE);
            tv_customer_code.setText(customer.getCode());
            tv_customer_name.setText(customer.getName());
            tv_customer_address.setText(customer.getAddress1());
            tv_customer_city.setText(customer.getCity());
            if (customer.getLatitude() == 0 && customer.getLongitude() == 0)
                getPlacesLocation(customer.getAddress1());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == App.GPS_SETTING) {
            checkGPS();
        } else if (requestCode == REQUEST_NEW_CUSTOMER && resultCode == RESULT_OK) {
            this.customer = data.getBundleExtra("bundle").getParcelable("customer");
            setCustomer();
        }
    }

    private class AddressResultReceiver extends ResultReceiver {

        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constant.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constant.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (address != null)
                            gotoLocation(address.getLatitude(), address.getLongitude(), 15);
                    }
                });
            } else {
                currentIndexSearch = currentIndexSearch + 1;
                if (currentIndexSearch == 2 && customer.getAddress2() != null)
                    getPlacesLocation(customer.getAddress2());
                else if (currentIndexSearch == 3 && customer.getCity() != null)
                    getPlacesLocation(customer.getCity());
                else {
                    Toast.makeText(context, "Customer Location not found!", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                    stopService(placeIntent);
                }
            }
        }
    }
}