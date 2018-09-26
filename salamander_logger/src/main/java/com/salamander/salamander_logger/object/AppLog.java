package com.salamander.salamander_logger.object;

import android.os.Parcel;
import android.os.Parcelable;

public class AppLog implements Parcelable {

    public static final Creator<AppLog> CREATOR = new Creator<AppLog>() {
        @Override
        public AppLog createFromParcel(Parcel in) {
            return new AppLog(in);
        }

        @Override
        public AppLog[] newArray(int size) {
            return new AppLog[size];
        }
    };
    private int UserID, LineNumber;
    private String ClassName, MethodName, Exception;

    public AppLog() {
    }

    protected AppLog(Parcel in) {
        UserID = in.readInt();
        LineNumber = in.readInt();
        ClassName = in.readString();
        MethodName = in.readString();
        Exception = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UserID);
        dest.writeInt(LineNumber);
        dest.writeString(ClassName);
        dest.writeString(MethodName);
        dest.writeString(Exception);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        if (className.contains("$"))
            className = className.substring(0, className.indexOf("$"));
        ClassName = className;
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public String getException() {
        return Exception;
    }

    public void setException(String exception) {
        Exception = exception;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getLineNumber() {
        return LineNumber;
    }

    public void setLineNumber(int lineNumber) {
        LineNumber = lineNumber;
    }
}
