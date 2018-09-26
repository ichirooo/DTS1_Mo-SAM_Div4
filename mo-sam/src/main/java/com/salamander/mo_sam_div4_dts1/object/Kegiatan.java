package com.salamander.mo_sam_div4_dts1.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class Kegiatan implements Parcelable {

    public static final String KEGIATAN = "Kegiatan";
    public static final String KEGIATAN_ID_SERVER = "IDServer";
    public static final String KEGIATAN_ID_LOKAL = "IDLokal";
    public static final String KEGIATAN_TIPE = "Tipe";
    public static final String KEGIATAN_JENIS = "Jenis";
    public static final String KEGIATAN_SUBJECT = "Subject";
    public static final String KEGIATAN_TGL_MULAI = "TglMulai";
    public static final String KEGIATAN_TGL_SELESAI = "TglSelesai";
    public static final String KEGIATAN_KETERANGAN = "Keterangan";
    public static final String KEGIATAN_RESULT = "Result";
    public static final String KEGIATAN_RESULT_DATE = "ResultDate";
    public static final String KEGIATAN_CANCELED = "Canceled";
    public static final String KEGIATAN_CANCEL_DATE = "CancelDate";
    public static final String KEGIATAN_CANCEL_REASON = "CancelReason";
    public static final String KEGIATAN_LIST_PHOTO = "Photos";
    public static final String KEGIATAN_CHECKED_IN = "CheckedIn";
    public static final String KEGIATAN_INPUT_DATE = "InputDate";
    public static final Creator<Kegiatan> CREATOR = new Creator<Kegiatan>() {
        @Override
        public Kegiatan createFromParcel(Parcel in) {
            return new Kegiatan(in);
        }

        @Override
        public Kegiatan[] newArray(int size) {
            return new Kegiatan[size];
        }
    };
    private int IDServer, IDLokal, Cancel, CheckedIn;
    private String Tipe, Jenis, Subject, CancelReason, Keterangan, Result;
    private SalesHeader SalesOrder = new SalesHeader();
    private ArrayList<Photo> ListPhoto = new ArrayList<>();
    private Tanggal StartDate = new Tanggal(), EndDate = new Tanggal(), ResultDate = new Tanggal(), CancelDate = new Tanggal(), InputDate = new Tanggal();
    private User Salesperson;

    public Kegiatan() {
    }

    protected Kegiatan(Parcel in) {
        IDServer = in.readInt();
        IDLokal = in.readInt();
        Cancel = in.readInt();
        CheckedIn = in.readInt();
        Tipe = in.readString();
        Jenis = in.readString();
        Subject = in.readString();
        CancelReason = in.readString();
        Keterangan = in.readString();
        Result = in.readString();
        SalesOrder = in.readParcelable(SalesHeader.class.getClassLoader());
        ListPhoto = in.createTypedArrayList(Photo.CREATOR);
        StartDate = in.readParcelable(Tanggal.class.getClassLoader());
        EndDate = in.readParcelable(Tanggal.class.getClassLoader());
        ResultDate = in.readParcelable(Tanggal.class.getClassLoader());
        CancelDate = in.readParcelable(Tanggal.class.getClassLoader());
        InputDate = in.readParcelable(Tanggal.class.getClassLoader());
        Salesperson = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(IDServer);
        dest.writeInt(IDLokal);
        dest.writeInt(Cancel);
        dest.writeInt(CheckedIn);
        dest.writeString(Tipe);
        dest.writeString(Jenis);
        dest.writeString(Subject);
        dest.writeString(CancelReason);
        dest.writeString(Keterangan);
        dest.writeString(Result);
        dest.writeParcelable(SalesOrder, flags);
        dest.writeTypedList(ListPhoto);
        dest.writeParcelable(StartDate, flags);
        dest.writeParcelable(EndDate, flags);
        dest.writeParcelable(ResultDate, flags);
        dest.writeParcelable(CancelDate, flags);
        dest.writeParcelable(InputDate, flags);
        dest.writeParcelable(Salesperson, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public SalesHeader getSalesHeader() {
        return SalesOrder;
    }

    public void setSalesHeader(SalesHeader salesOrder) {
        SalesOrder = salesOrder;
    }

    public int getIDServer() {
        return IDServer;
    }

    public void setIDServer(int IDServer) {
        this.IDServer = IDServer;
    }

    public int getIDLokal() {
        return IDLokal;
    }

    public void setIDLokal(int IDLokal) {
        this.IDLokal = IDLokal;
    }

    public int getCancel() {
        return Cancel;
    }

    public void setCancel(int cancel) {
        Cancel = cancel;
    }

    public int getCheckedIn() {
        return CheckedIn;
    }

    public String getTipe() {
        return Tipe;
    }

    public void setTipe(String tipe) {
        Tipe = tipe;
    }

    public String getJenis() {
        return Jenis;
    }

    public void setJenis(String jenis) {
        Jenis = jenis;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getCancelReason() {
        return CancelReason;
    }

    public void setCancelReason(String cancelReason) {
        CancelReason = cancelReason;
    }

    public String getKeterangan() {
        return Keterangan;
    }

    public void setKeterangan(String keterangan) {
        Keterangan = keterangan;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public ArrayList<Photo> getListPhoto() {
        return ListPhoto;
    }

    public void setListPhoto(ArrayList<Photo> listPhoto) {
        ListPhoto = listPhoto;
    }

    public Tanggal getStartDate() {
        return StartDate;
    }

    public void setStartDate(Tanggal startDate) {
        StartDate = startDate;
    }

    public void setStartDate(Date startDate) {
        StartDate = new Tanggal(startDate);
    }

    public Tanggal getEndDate() {
        return EndDate;
    }

    public void setEndDate(Tanggal endDate) {
        EndDate = endDate;
    }

    public void setEndDate(Date endDate) {
        EndDate = new Tanggal(endDate);
    }

    public Tanggal getResultDate() {
        return ResultDate;
    }

    public void setResultDate(Tanggal resultDate) {
        ResultDate = resultDate;
    }

    public void setResultDate(Date resultDate) {
        ResultDate = new Tanggal(resultDate);
    }

    public Tanggal getCancelDate() {
        return CancelDate;
    }

    public void setCancelDate(Tanggal cancelDate) {
        CancelDate = cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        CancelDate = new Tanggal(cancelDate);
    }

    public User getSalesperson() {
        return Salesperson;
    }

    public void setSalesperson(User salesperson) {
        Salesperson = salesperson;
    }

    public boolean isCanceled() {
        return Cancel == 1;
    }

    public boolean isCheckedIn() {
        return CheckedIn == 1;
    }

    public void setCheckedIn(int checkedIn) {
        CheckedIn = checkedIn;
    }

    public String getAsJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(Customer.CUSTOMER_CODE, getSalesHeader().getCustomer().getCode());
            if (!getSalesHeader().getCustomer().getCode().contains("CUST"))
                json.put(Customer.CUSTOMER_CODE_NAV, getSalesHeader().getCustomer().getCode());
            json.put(Customer.CUSTOMER_NAME, getSalesHeader().getCustomer().getName());
            json.put(Customer.CUSTOMER_LATITUDE, getSalesHeader().getCustomer().getLatitude());
            json.put(Customer.CUSTOMER_LONGITUDE, getSalesHeader().getCustomer().getLongitude());

            json.put(User.BEX_EMP_NO, getSalesperson().getEmpNo());
            json.put(User.BEX_INITIAL, getSalesperson().getInitial());

            json.put(KEGIATAN_ID_SERVER, getIDServer());
            json.put(KEGIATAN_TGL_MULAI, getStartDate().getTglString());
            json.put(KEGIATAN_TGL_SELESAI, getEndDate().getTglString());
            json.put(KEGIATAN_TIPE, getTipe());
            json.put(KEGIATAN_JENIS, getJenis());
            json.put(KEGIATAN_SUBJECT, getSubject());
            json.put(KEGIATAN_CANCELED, getCancel());
            json.put(KEGIATAN_CANCEL_DATE, getCancelDate().getTglString());
            json.put(KEGIATAN_CANCEL_REASON, getCancelReason());
            json.put(KEGIATAN_KETERANGAN, getKeterangan());
            json.put(KEGIATAN_RESULT, getResult());
            json.put(KEGIATAN_RESULT_DATE, getResultDate().getTglString());
            json.put(KEGIATAN_CHECKED_IN, getCheckedIn());

        } catch (Exception e) {
            return e.toString();
        }
        return json.toString();
    }

    public Kegiatan getFromJSON(Context context, JSONObject json) {
        try {
            setIDServer(JSON.getInt(json, KEGIATAN_ID_SERVER));
            setTipe(JSON.getString(json, KEGIATAN_TIPE));
            setJenis(JSON.getString(json, KEGIATAN_JENIS));
            setSubject(JSON.getString(json, KEGIATAN_SUBJECT));
            setStartDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, KEGIATAN_TGL_MULAI)));
            setEndDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, KEGIATAN_TGL_SELESAI)));
            setKeterangan(JSON.getString(json, KEGIATAN_KETERANGAN));
            setResult(JSON.getString(json, KEGIATAN_KETERANGAN));
            setResultDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, KEGIATAN_RESULT_DATE)));
            setCheckedIn(JSON.getInt(json, KEGIATAN_CHECKED_IN));
            setCancel(JSON.getInt(json, KEGIATAN_CANCELED));
            setCancelReason(JSON.getString(json, KEGIATAN_CANCEL_REASON));
            setCancelDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, KEGIATAN_CANCEL_DATE)));
            setInputDate(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(json, KEGIATAN_INPUT_DATE)));

            setSalesperson(App.getUser(context));
            setSalesHeader(new SalesHeader().getFromJSON(context, JSON.getString(json, SalesHeader.SALES_HEADER)));
            getSalesHeader().setIDKegiatan(IDServer);
            getSalesHeader().getCustomer().setIDKegiatan(IDServer);
        } catch (Exception e) {
            Log.d(App.TAG, Kegiatan.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        return this;
    }

    public String getAsJSONPost() {
        JSONObject json = new JSONObject();
        try {
            json.put(KEGIATAN_ID_SERVER, getIDServer());
            json.put(KEGIATAN_TGL_MULAI, getStartDate().getTglString());
            json.put(KEGIATAN_TGL_SELESAI, getEndDate().getTglString());
            json.put(KEGIATAN_TIPE, getTipe());
            json.put(KEGIATAN_JENIS, getJenis());
            json.put(KEGIATAN_SUBJECT, getSubject());

            json.put(Customer.CUSTOMER, getSalesHeader().getCustomer().getAsJSON());
            json.put(User.BEX, Salesperson.getAsJSON());
        } catch (Exception e) {
            return e.toString();
        }
        return json.toString();
    }

    public Tanggal getInputDate() {
        return InputDate;
    }

    public void setInputDate(Tanggal inputDate) {
        InputDate = inputDate;
    }

    public void setInputDate(Date inputDate) {
        InputDate = new Tanggal(inputDate);
    }
}
