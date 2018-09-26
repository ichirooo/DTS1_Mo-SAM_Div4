package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.salamander.salamander_base_module.object.Tanggal;

/**
 * Created by benny_aziz on 03/05/2015.
 */
public class Log_Feed implements Parcelable {

    public static final String LOG_FEED_ID = "IDServer";
    public static final String LOG_FEED_TANGGAL = "Tanggal";
    public static final String LOG_FEED_FEEDBACK = "Feedback";
    public static final Creator<Log_Feed> CREATOR = new Creator<Log_Feed>() {
        @Override
        public Log_Feed createFromParcel(Parcel in) {
            return new Log_Feed(in);
        }

        @Override
        public Log_Feed[] newArray(int size) {
            return new Log_Feed[size];
        }
    };
    private int id;
    private Tanggal LogDate;
    private Feedback feedback;

    public Log_Feed() {
    }

    protected Log_Feed(Parcel in) {
        id = in.readInt();
        feedback = in.readParcelable(Feedback.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(feedback, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Tanggal getLogDate() {
        return LogDate;
    }

    public void setLogDate(Tanggal logDate) {
        LogDate = logDate;
    }

    public void setLogDate(long logDate) {
        LogDate = new Tanggal(logDate);
    }
}
