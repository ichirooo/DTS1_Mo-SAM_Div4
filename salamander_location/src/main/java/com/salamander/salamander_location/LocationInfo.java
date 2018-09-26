package com.salamander.salamander_location;

import android.location.Location;

/**
 * Created by benny_aziz on 04/11/2017.
 */

public class LocationInfo extends Location {

    public static final String POSITION_LATITUDE = "Latitude";
    public static final String POSITION_LONGITUDE = "Longitude";
    public static final String POSITION_TIMESTAMP = "DeviceTime";
    public static final String POSITION_PROVIDER = "Provider";

    public LocationInfo() {
        super("");
    }

    public LocationInfo(String provider) {
        super(provider);
    }

    public LocationInfo(double latitude, double longitude, long timestamp, String provider) {
        super(provider);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTime(timestamp);
        this.setProvider(provider);
    }

    public void cloneFrom(LocationInfo positionInfo) {
        this.setLatitude(positionInfo.getLatitude());
        this.setLongitude(positionInfo.getLongitude());
        this.setTime(positionInfo.getTime());
        this.setProvider(positionInfo.getProvider());
    }
}
