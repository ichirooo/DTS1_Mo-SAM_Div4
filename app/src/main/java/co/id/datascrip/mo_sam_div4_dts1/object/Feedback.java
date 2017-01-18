package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benny_aziz on 02/23/2015.
 */
public class Feedback implements Parcelable {

    public static final Parcelable.Creator<Feedback> CREATOR = new Creator<Feedback>() {

        @Override
        public Feedback createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Feedback feedback = new Feedback();
            feedback.line_id = source.readInt();
            feedback.andro_so = source.readString();
            feedback.note = source.readString();
            feedback.bex = source.readParcelable(BEX.class.getClassLoader());
            return feedback;
        }

        @Override
        public Feedback[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Feedback[size];
        }
    };
    int line_id;
    String andro_so, note;
    BEX bex;

    public String getAndroSO() {
        return andro_so;
    }

    public void setAndroSO(String andro_so) {
        this.andro_so = andro_so;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BEX getBex() {
        return bex;
    }

    public void setBex(BEX bex) {
        this.bex = bex;
    }

    public int getLineID() {
        return line_id;
    }

    public void setLineID(int line_id) {
        this.line_id = line_id;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(line_id);
        dest.writeString(andro_so);
        dest.writeString(note);
        dest.writeParcelable(bex, flags);
    }
}
