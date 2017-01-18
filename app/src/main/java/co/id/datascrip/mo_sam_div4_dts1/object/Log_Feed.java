package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by benny_aziz on 03/05/2015.
 */
public class Log_Feed implements Parcelable {

    public static final Parcelable.Creator<Log_Feed> CREATOR = new Creator<Log_Feed>() {

        @Override
        public Log_Feed createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Log_Feed log = new Log_Feed();
            log.id = source.readInt();
            log.log_date = source.readString();
            log.feedback = source.readParcelable(Feedback.class.getClassLoader());
            return log;
        }

        @Override
        public Log_Feed[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Log_Feed[size];
        }
    };
    private int id;
    private String log_date;
    private Date date;
    private Feedback feedback;

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

    public String getDate() {
        return this.log_date;
    }

    public void setDate(String log_date) {
        this.log_date = log_date;

        try {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            this.date = f.parse(log_date);
        } catch (ParseException e) {
            this.date = null;
        }
    }

    public Date getLogDate() {
        if (this.date == null) {
            try {
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                this.date = f.parse(log_date);
            } catch (ParseException e) {
                this.date = null;
            }
        }
        return this.date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(log_date);
        parcel.writeParcelable(feedback, flags);
    }
}
