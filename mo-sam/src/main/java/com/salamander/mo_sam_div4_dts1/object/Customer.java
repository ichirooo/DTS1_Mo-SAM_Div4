package com.salamander.mo_sam_div4_dts1.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.salamander_base_module.Utils;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

public class Customer implements Parcelable {

    public static final String CUSTOMER = "Customer";
    public static final String CUSTOMER_ID_KEGIATAN = "IDKegiatan";
    public static final String CUSTOMER_BEX = "BEX";
    public static final String CUSTOMER_CODE = "Code";
    public static final String CUSTOMER_CODE_NAV = "CodeNAV";
    public static final String CUSTOMER_NAME = "Name";
    public static final String CUSTOMER_ALIAS = "Alias";
    public static final String CUSTOMER_ADDRESS_1 = "Address1";
    public static final String CUSTOMER_ADDRESS_2 = "Address2";
    public static final String CUSTOMER_CONTACT_PERSON = "ContactPerson";
    public static final String CUSTOMER_CONTACT_PHONE = "ContactPhone";
    public static final String CUSTOMER_CITY = "City";
    public static final String CUSTOMER_NPWP = "NPWP";
    public static final String CUSTOMER_PHONE = "Phone";
    public static final String CUSTOMER_EMAIL = "Email";
    public static final String CUSTOMER_LATITUDE = "Latitude";
    public static final String CUSTOMER_LONGITUDE = "Longitude";
    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
    private String Code, CodeNAV, Name, Alias, Address1, Address2, ContactPerson, ContactPhone, City, NPWP, Phone, Email;
    private double Latitude, Longitude;
    private int IDKegiatan, BEX;

    public Customer() {
    }

    protected Customer(Parcel in) {
        Code = in.readString();
        CodeNAV = in.readString();
        Name = in.readString();
        Alias = in.readString();
        Address1 = in.readString();
        Address2 = in.readString();
        ContactPerson = in.readString();
        ContactPhone = in.readString();
        City = in.readString();
        NPWP = in.readString();
        Phone = in.readString();
        Email = in.readString();
        Latitude = in.readDouble();
        Longitude = in.readDouble();
        IDKegiatan = in.readInt();
        BEX = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Code);
        dest.writeString(CodeNAV);
        dest.writeString(Name);
        dest.writeString(Alias);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(ContactPerson);
        dest.writeString(ContactPhone);
        dest.writeString(City);
        dest.writeString(NPWP);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
        dest.writeInt(IDKegiatan);
        dest.writeInt(BEX);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getNPWP() {
        return NPWP;
    }

    public void setNPWP(String npwp) {
        this.NPWP = npwp;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        this.Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        this.Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        this.City = city;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.ContactPerson = contactPerson;
    }

    public String getContactPhone() {
        return ContactPhone;
    }

    public void setContactPhone(String contactPhone) {
        ContactPhone = contactPhone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        this.Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        this.Longitude = longitude;
    }

    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(CUSTOMER_CODE, Code);
            jsonObject.put(CUSTOMER_BEX, BEX);
            jsonObject.put(CUSTOMER_CODE_NAV, CodeNAV == null ? Code : CodeNAV);
            jsonObject.put(CUSTOMER_NAME, Name);
            jsonObject.put(CUSTOMER_ALIAS, Alias);
            jsonObject.put(CUSTOMER_ADDRESS_1, Address1);
            jsonObject.put(CUSTOMER_ADDRESS_2, Address2);
            jsonObject.put(CUSTOMER_CONTACT_PERSON, ContactPerson);
            jsonObject.put(CUSTOMER_CONTACT_PHONE, ContactPhone);
            jsonObject.put(CUSTOMER_CITY, City);
            jsonObject.put(CUSTOMER_NPWP, NPWP);
            jsonObject.put(CUSTOMER_PHONE, Phone);
            jsonObject.put(CUSTOMER_EMAIL, Email);
            jsonObject.put(CUSTOMER_LATITUDE, Latitude);
            jsonObject.put(CUSTOMER_LONGITUDE, Longitude);
        } catch (Exception e) {
            Utils.showLog(getClass().getSimpleName(), "getAsJSON", e.toString());
            //Log.d(App.TAG, Customer.class.getSimpleName() + " => getAsJSONOrder  => " + e.toString());
        }
        return jsonObject;
    }

    public Customer getFromJSON(Context context, String jsonStr) {
        return getFromJSON(context, JSON.toJSONObject(jsonStr));
    }

    public Customer getFromJSON(Context context, JSONObject jsonObject) {
        try {
            setCode(JSON.getString(jsonObject, CUSTOMER_CODE));
            if (JSON.getInt(jsonObject, CUSTOMER_BEX) == 0)
                setBEX(App.getUser(context).getEmpNo());
            else setBEX(JSON.getInt(jsonObject, CUSTOMER_BEX));
            setCodeNAV(JSON.getString(jsonObject, CUSTOMER_CODE_NAV));
            setName(JSON.getString(jsonObject, CUSTOMER_NAME));
            setAlias(JSON.getString(jsonObject, CUSTOMER_ALIAS));
            setAddress1(JSON.getString(jsonObject, CUSTOMER_ADDRESS_1));
            setAddress2(JSON.getString(jsonObject, CUSTOMER_ADDRESS_2));
            setContactPerson(JSON.getString(jsonObject, CUSTOMER_CONTACT_PERSON));
            setContactPhone(JSON.getString(jsonObject, CUSTOMER_CONTACT_PHONE));
            setCity(JSON.getString(jsonObject, CUSTOMER_CITY));
            setNPWP(JSON.getString(jsonObject, CUSTOMER_NPWP));
            setPhone(JSON.getString(jsonObject, CUSTOMER_PHONE));
            setEmail(JSON.getString(jsonObject, CUSTOMER_EMAIL));
            setLatitude(JSON.getDouble(jsonObject, CUSTOMER_LATITUDE));
            setLongitude(JSON.getDouble(jsonObject, CUSTOMER_LONGITUDE));
        } catch (Exception e) {
            Utils.showLog(getClass().getSimpleName(), "getFromJSON", e.toString());
            //Log.d(App.TAG, Customer.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        return this;
    }

    public int getIDKegiatan() {
        return IDKegiatan;
    }

    public void setIDKegiatan(int IDKegiatan) {
        this.IDKegiatan = IDKegiatan;
    }

    public String getCodeNAV() {
        return CodeNAV;
    }

    public void setCodeNAV(String codeNAV) {
        CodeNAV = codeNAV;
    }

    public int getBEX() {
        return BEX;
    }

    public void setBEX(int BEX) {
        this.BEX = BEX;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }
}