package com.salamander.salamander_network;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class RetroData implements Parcelable {

    public static final String RETRO_STATUS = "status";
    public static final String RETRO_DATA = "data";
    public static final String RETRO_TITLE = "title";
    public static final String RETRO_MESSAGE = "msg";
    public static final String RETRO_SQL = "sql";
    public static final Creator<RetroData> CREATOR = new Creator<RetroData>() {
        @Override
        public RetroData createFromParcel(Parcel in) {
            return new RetroData(in);
        }

        @Override
        public RetroData[] newArray(int size) {
            return new RetroData[size];
        }
    };
    private String ClassName, MethodName, Parameter;
    private String Result;
    private RetroStatus retroStatus = new RetroStatus();

    public RetroData() {
    }

    public RetroData(String className, String methodName, String parameter) {
        this.ClassName = className;
        this.MethodName = methodName;
        this.Parameter = parameter;
    }

    protected RetroData(Parcel in) {
        ClassName = in.readString();
        MethodName = in.readString();
        Parameter = in.readString();
        Result = in.readString();
        retroStatus = in.readParcelable(RetroStatus.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ClassName);
        dest.writeString(MethodName);
        dest.writeString(Parameter);
        dest.writeString(Result);
        dest.writeParcelable(retroStatus, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        String[] splitString = className.split("\\.");
        ClassName = splitString[splitString.length - 1];
    }

    public String getMethodName() {
        return MethodName;
    }

    public void setMethodName(String methodName) {
        MethodName = methodName;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }

    public RetroStatus getRetroStatus() {
        return retroStatus;
    }

    public void setRetroStatus(RetroStatus retroStatus) {
        this.retroStatus = retroStatus;
    }

    public boolean isSuccess() {
        return retroStatus != null && retroStatus.isSuccess();
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getErrorMsg() {
        return retroStatus.getMessage();
    }

    public JSONObject getJSONData() {
        return JSON.getJSONObject(JSON.toJSONObject(getResult()), RETRO_DATA);
    }

    public String getData() {
        return JSON.getStringOrNull(JSON.toJSONObject(getResult()), RETRO_DATA);
    }
}
