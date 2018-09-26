package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by benny_aziz on 02/23/2015.
 */
public class Feedback implements Parcelable {

    public static final String FEEDBACK_LINE_ID = "IDLine";
    public static final String FEEDBACK_ORDER_NO = "OrderNo";
    public static final String FEEDBACK_NOTE = "Note";
    public static final String FEEDBACK_BEX = "BEX";
    public static final Parcelable.Creator<Feedback> CREATOR = new Creator<Feedback>() {

        @Override
        public Feedback createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Feedback feedback = new Feedback();
            feedback.IDLine = source.readInt();
            feedback.OrderNo = source.readString();
            feedback.Note = source.readString();
            feedback.user = source.readParcelable(User.class.getClassLoader());
            return feedback;
        }

        @Override
        public Feedback[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Feedback[size];
        }
    };
    private int IDLine;
    private String OrderNo, Note;
    private User user;

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String andro_so) {
        this.OrderNo = andro_so;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        this.Note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLineID() {
        return IDLine;
    }

    public void setLineID(int line_id) {
        this.IDLine = line_id;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(IDLine);
        dest.writeString(OrderNo);
        dest.writeString(Note);
        dest.writeParcelable(user, flags);
    }
}
