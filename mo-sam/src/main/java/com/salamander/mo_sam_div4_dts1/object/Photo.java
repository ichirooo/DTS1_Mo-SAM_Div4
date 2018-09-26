package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

import java.io.File;

public class Photo implements Parcelable {

    public static final String PHOTO = "Photo";
    public static final String PHOTO_ID_LOCAL = "IDLocal";
    public static final String PHOTO_ID_SERVER = "IDServer";
    public static final String PHOTO_ID_KEGIATAN = "IDKegiatan";
    public static final String PHOTO_PATH = "Path";
    public static final String PHOTO_URL = "URL";
    public static final String PHOTO_NAME = "Name";
    public static final String PHOTO_DESCRIPTION = "Description";
    public static final String PHOTO_TANGGAL = "Tanggal";
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    private int IDLocal = 0, IDServer = 0, IDKegiatan = 0;
    private String Path, URL, Name, Description;
    private Tanggal tanggal = new Tanggal();

    public Photo() {
    }

    protected Photo(Parcel in) {
        IDLocal = in.readInt();
        IDServer = in.readInt();
        IDKegiatan = in.readInt();
        Path = in.readString();
        URL = in.readString();
        Description = in.readString();
        tanggal = in.readParcelable(Tanggal.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IDLocal);
        dest.writeInt(IDServer);
        dest.writeInt(IDKegiatan);
        dest.writeString(Path);
        dest.writeString(URL);
        dest.writeString(Description);
        dest.writeParcelable(tanggal, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIDLocal() {
        return IDLocal;
    }

    public void setIDLocal(int IDLocal) {
        this.IDLocal = IDLocal;
    }

    public int getIDKegiatan() {
        return IDKegiatan;
    }

    public void setIDKegiatan(int IDKegiatan) {
        this.IDKegiatan = IDKegiatan;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public boolean isPosted() {
        return getIDServer() > 0;
    }

    public Tanggal getTanggal() {
        if (tanggal == null)
            return new Tanggal();
        else return tanggal;
    }

    public void setTanggal(Tanggal createdDate) {
        this.tanggal = createdDate;
    }


    public int getIDServer() {
        return IDServer;
    }

    public void setIDServer(int IDServer) {
        this.IDServer = IDServer;
    }

    public String getName() {
        if (!Utils.isEmpty(Path) || Utils.isEmpty(Name))
            return new File(Path).getName();
        else return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(PHOTO_ID_LOCAL, IDLocal);
            jsonObject.put(PHOTO_ID_SERVER, IDServer);
            jsonObject.put(PHOTO_ID_KEGIATAN, IDKegiatan);
            jsonObject.put(PHOTO_NAME, getName());
            jsonObject.put(PHOTO_DESCRIPTION, Description);
        } catch (Exception e) {
            //Log.d(App.TAG, Photo.class.getSimpleName() + " => getAsJSON  => " + e.toString());
        }
        return jsonObject;
    }

    public Photo getFromJSON(JSONObject json) {
        try {
            setIDServer(JSON.getInt(json, PHOTO_ID_SERVER));
            setIDKegiatan(JSON.getInt(json, PHOTO_ID_KEGIATAN));
            setURL(JSON.getString(json, PHOTO_URL));
            setName(JSON.getString(json, PHOTO_NAME));
            setDescription(JSON.getString(json, PHOTO_DESCRIPTION));
            getTanggal().set(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, PHOTO_TANGGAL)));
        } catch (Exception e) {
            //Log.d(App.TAG, Photo.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        return this;
    }
}
