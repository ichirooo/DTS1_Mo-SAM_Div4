package com.salamander.mo_sam_div4_dts1.services;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.salamander.mo_sam_div4_dts1.Function;

/**
 * Created by benny_aziz on 02/23/2015.
 */
public class Position extends Service implements LocationListener {

    protected LocationManager locManager;
    Intent intent;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation = false;
    private Location location;
    private double latitude, longitude;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
        String bex = intent.getStringExtra("bex");
        int id_kegiatan = intent.getIntExtra("id_kegiatan", 0);
        location = getLocation();
        if (latitude != 0 && longitude != 0) {
            //new Proses_Send_Position().execute(String.valueOf(id_kegiatan), bex, String.valueOf(latitude), String.valueOf(longitude));
        } else {
            new Function().writeToText("Position (" + String.valueOf(latitude) + ";" + String.valueOf(longitude) + ")",
                    "Gagal insert ke database");
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Location getLocation() {
        try {
            locManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            this.isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;

                if (isNetworkEnabled) {
                    location = getLocationByProvider(LocationManager.NETWORK_PROVIDER);
                }

                if (isGPSEnabled) {
                    location = getLocationByProvider(LocationManager.GPS_PROVIDER);
                }

                if (location == null) {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    String provider = locManager.getBestProvider(criteria, false);
                    location = getLocationByProvider(provider);
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            new Function().writeToText("Position (" + String.valueOf(latitude) + ";" + String.valueOf(longitude) + ")",
                    " : " + e.toString());
        }
        return location;
    }

    private Location getLocationByProvider(String provider) {
        Location loc = new Location(provider);
        if (location == null) {
            locManager.requestLocationUpdates(provider, 0, 0, this);
            if (locManager != null) {
                loc = locManager.getLastKnownLocation(provider);
                if (loc != null) {
                    this.latitude = loc.getLatitude();
                    this.longitude = loc.getLongitude();
                }
            }
        }
        return loc;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        } else latitude = 0.0;
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        } else longitude = 0.0;
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void stopGPS() {
        if (locManager != null) {
            locManager.removeUpdates(Position.this);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        stopGPS();
        super.onDestroy();
    }

    public void stopIt() {
        this.stopService(this.intent);
        onDestroy();
    }
}
