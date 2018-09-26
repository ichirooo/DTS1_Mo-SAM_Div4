package com.salamander.salamander_base_module;

import android.os.Parcel;
import android.os.Parcelable;

public class Log implements Parcelable {

    public static final Creator<Log> CREATOR = new Creator<Log>() {
        @Override
        public Log createFromParcel(Parcel in) {
            return new Log(in);
        }

        @Override
        public Log[] newArray(int size) {
            return new Log[size];
        }
    };
    private String ClassName, MethodName;
    private long DateTime;

    protected Log(Parcel in) {
        ClassName = in.readString();
        MethodName = in.readString();
        DateTime = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ClassName);
        dest.writeString(MethodName);
        dest.writeLong(DateTime);
    }
}
