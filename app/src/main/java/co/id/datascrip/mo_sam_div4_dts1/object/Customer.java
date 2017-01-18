package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    public static final Parcelable.Creator<Customer> CREATOR = new Creator<Customer>() {

        @Override
        public Customer createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Customer custDetail = new Customer();
            custDetail.Code = source.readString();
            custDetail.Name = source.readString();
            custDetail.Alias = source.readString();
            custDetail.Address1 = source.readString();
            custDetail.Address2 = source.readString();
            custDetail.City = source.readString();
            custDetail.Contact = source.readString();
            custDetail.NPWP = source.readString();
            custDetail.Phone = source.readString();
            custDetail.Email = source.readString();
            custDetail.Latitude = source.readDouble();
            custDetail.Longitude = source.readDouble();
            return custDetail;
        }

        @Override
        public Customer[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Customer[size];
        }
    };
    private String Code, Name, Alias, Address1, Address2, Contact, City, NPWP, Phone, Email;
    private double Latitude, Longitude;

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

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        this.Alias = alias;
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

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        this.Contact = contact;
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

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(Code);
        dest.writeString(Name);
        dest.writeString(Alias);
        dest.writeString(Address1);
        dest.writeString(Address2);
        dest.writeString(City);
        dest.writeString(Contact);
        dest.writeString(NPWP);
        dest.writeString(Phone);
        dest.writeString(Email);
        dest.writeDouble(Latitude);
        dest.writeDouble(Longitude);
    }
}