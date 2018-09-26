package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

import java.util.Date;

public class Holiday implements Parcelable {

    public static final String HOLIDAY_TANGGAL = "Tgl";
    public static final String HOLIDAY_DESCRIPTION = "Desc";
    public static final Creator<Holiday> CREATOR = new Creator<Holiday>() {
        @Override
        public Holiday createFromParcel(Parcel in) {
            return new Holiday(in);
        }

        @Override
        public Holiday[] newArray(int size) {
            return new Holiday[size];
        }
    };
    private Date Tgl;
    private String Description;
    private long TanggalMilis;

    public Holiday() {
    }

    public Holiday(String tanggalStr, String description) {
        this.Tgl = DateUtils.stringToDate(Tanggal.FORMAT_DATE, tanggalStr);
        this.Description = description;
    }

    protected Holiday(Parcel in) {
        Description = in.readString();
        TanggalMilis = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        dest.writeLong(TanggalMilis);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Date getTanggal() {
        if (Tgl == null) {
            if (TanggalMilis != 0)
                Tgl = new Date(TanggalMilis);
        }
        return Tgl;
    }

    public void setTanggal(Date tanggal) {
        Tgl = tanggal;
    }

    public void setTanggal(String tanggalStr) {
        this.Tgl = DateUtils.stringToDate(Tanggal.FORMAT_DATE, tanggalStr);
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public long getTanggalMilis() {
        return TanggalMilis;
    }

    public void setTanggalMilis(long tanggalMilis) {
        TanggalMilis = tanggalMilis;
    }

    public Holiday getFromJSON(JSONObject jsonObject) {
        try {
            this.Tgl = DateUtils.stringToDate(Tanggal.FORMAT_DATE, JSON.getString(jsonObject, HOLIDAY_TANGGAL));
            this.Description = JSON.getString(jsonObject, HOLIDAY_DESCRIPTION);
        } catch (Exception e) {
            //Log.d(App.TAG, Holiday.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        return this;
    }
}
