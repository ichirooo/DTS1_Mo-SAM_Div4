package co.id.datascrip.mo_sam_div4_dts1.location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

public class MapPosition {

    public static void setCurrentPosition(GoogleMap maps) {
        // TODO Auto-generated method stub
        LatLng pos;

        maps.clear();
        maps.setMyLocationEnabled(true);
        maps.setTrafficEnabled(true);
        pos = getLatLng();
        MarkerOptions marker = new MarkerOptions().position(pos);
        maps.addMarker(marker);
        if (pos != null) {
            CameraUpdate camerapos = CameraUpdateFactory.newLatLngZoom(pos, 13.0f);
            maps.animateCamera(camerapos);
        }
    }

    public static void setMapPosition(GoogleMap maps, HashMap<String, String> lokasi, String desc) {

        LatLng pos;

        maps.clear();
        maps.setMyLocationEnabled(true);
        maps.setTrafficEnabled(true);
        double lat = Double.parseDouble(lokasi.get("lat"));
        double lng = Double.parseDouble(lokasi.get("lng"));
        pos = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(pos);
        marker.title(desc);
        maps.addMarker(marker);

        if (pos != null) {
            CameraUpdate camerapos = CameraUpdateFactory.newLatLngZoom(pos, 15.0f);
            maps.animateCamera(camerapos);
        }
    }

    public static void setPosition(GoogleMap maps, LatLng pos, String desc) {

        maps.clear();
        maps.setMyLocationEnabled(true);
        maps.setTrafficEnabled(true);
        double lat = pos.latitude;
        double lng = pos.longitude;
        pos = new LatLng(lat, lng);
        MarkerOptions marker = new MarkerOptions().position(pos);
        marker.title(desc);
        maps.addMarker(marker);

        if (pos != null) {
            CameraUpdate camerapos = CameraUpdateFactory.newLatLngZoom(pos, 15.0f);
            maps.animateCamera(camerapos);
        }
    }

    public static LatLng getLatLng() {
        double lat, lang;

        lat = -6.156633463;
        lang = 106.8418492;

        return new LatLng(lat, lang);
    }
}
