package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

public class BEX implements Parcelable {

    public static final Parcelable.Creator<BEX> CREATOR = new Creator<BEX>() {

        @Override
        public BEX createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            BEX bex = new BEX();
            bex.emp_no = source.readString();
            bex.username = source.readString();
            bex.fullname = source.readString();
            bex.photo = source.readString();
            return bex;
        }

        @Override
        public BEX[] newArray(int size) {
            // TODO Auto-generated method stub
            return new BEX[size];
        }
    };
    private String emp_no, initial, username, fullname, photo;

    public String getEmpNo() {
        return emp_no;
    }

    public void setEmpNo(String emp_no) {
        this.emp_no = emp_no;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        // TODO Auto-generated method stub
        destination.writeString(emp_no);
        destination.writeString(username);
        destination.writeString(fullname);
        destination.writeString(photo);
    }
}
