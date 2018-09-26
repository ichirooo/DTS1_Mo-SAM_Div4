package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

public class User implements Parcelable {

    public static final String BEX = "BEX";
    public static final String BEX_EMP_NO = "EmpNo";
    public static final String BEX_INITIAL = "Initial";
    public static final String BEX_FULLNAME = "Fullname";
    public static final String BEX_PHOTO = "Photo";
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String Initial, Fullname, Photo;
    private int EmpNo;

    public User() {
    }

    public User(int empNo, String initial) {
        this.EmpNo = empNo;
        this.Initial = initial;
    }

    protected User(Parcel in) {
        Initial = in.readString();
        Fullname = in.readString();
        Photo = in.readString();
        EmpNo = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Initial);
        dest.writeString(Fullname);
        dest.writeString(Photo);
        dest.writeInt(EmpNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getEmpNo() {
        return EmpNo;
    }

    public void setEmpNo(int emp_no) {
        this.EmpNo = emp_no;
    }

    public String getInitial() {
        return Initial;
    }

    public void setInitial(String initial) {
        this.Initial = initial;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        this.Fullname = fullname;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        this.Photo = photo;
    }

    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(BEX_EMP_NO, EmpNo);
            jsonObject.put(BEX_INITIAL, Initial);
            jsonObject.put(BEX_FULLNAME, Fullname);
        } catch (Exception e) {
            Utils.showLog(getClass().getSimpleName(), "getAsJSON", e.toString());
            //Log.d(App.TAG, User.class.getSimpleName() + " => getAsJSON  => " + e.toString());
        }
        return jsonObject;
    }

    public User getFromJSON(String jsonObjectStr) {
        return getFromJSON(JSON.toJSONObject(jsonObjectStr));
    }

    public User getFromJSON(JSONObject jsonObject) {
        User user = new User();
        try {
            user.setEmpNo(JSON.getInt(jsonObject, BEX_EMP_NO));
            user.setInitial(JSON.getString(jsonObject, BEX_INITIAL));
            user.setFullname(JSON.getString(jsonObject, BEX_FULLNAME));
            user.setPhoto(JSON.getString(jsonObject, BEX_PHOTO));

        } catch (Exception e) {
            Utils.showLog(getClass().getSimpleName(), "getFromJSON", e.toString());
            //Log.d(App.TAG, User.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        return user;
    }

}
