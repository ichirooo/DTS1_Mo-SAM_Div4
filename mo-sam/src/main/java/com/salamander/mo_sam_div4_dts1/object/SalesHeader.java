package com.salamander.mo_sam_div4_dts1.object;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.salamander.salamander_network.JSON.getString;

public class SalesHeader implements Parcelable {

    public static final int STATUS_ORDER_OPEN = 0;
    public static final int STATUS_ORDER_COS = 1;
    public static final int STATUS_ORDER_ANDROID_EDIT = 2;
    public static final int STATUS_ORDER_WEB_EDIT = 3;

    public static final String SALES_HEADER = "SalesHeader";
    public static final String SALES_HEADER_ID_SERVER = "IDServer";
    public static final String SALES_HEADER_ID_LOCAL = "IDLocal";
    public static final String SALES_HEADER_ID_KEGIATAN = "IDKegiatan";
    public static final String SALES_HEADER_SALESPERSON = "Salesperson";
    public static final String SALES_HEADER_ORDER_NO = "OrderNo";
    public static final String SALES_HEADER_CUSTOMER = "Customer";
    public static final String SALES_HEADER_NOTES = "Notes";
    public static final String SALES_HEADER_CURRENCY = "Currency";
    public static final String SALES_HEADER_TEMPO = "Tempo";
    public static final String SALES_HEADER_SYARAT_PEMBAYARAN = "Syarat";
    public static final String SALES_HEADER_PROMISED_DELIVERY_DATE = "PromisedDate";
    public static final String SALES_HEADER_INPUT_DATE = "InputDate";
    public static final String SALES_HEADER_SALES_LINE = "SalesLine";
    public static final String SALES_HEADER_SUBTOTAL = "Subtotal";
    public static final String SALES_HEADER_POTONGAN = "Potongan";
    public static final String SALES_HEADER_DPP = "DPP";
    public static final String SALES_HEADER_DPP_PPN = "PPnDPP";
    public static final String SALES_HEADER_TOTAL = "Total";
    public static final String SALES_HEADER_STATUS = "Status";
    public static final Creator<SalesHeader> CREATOR = new Creator<SalesHeader>() {
        @Override
        public SalesHeader createFromParcel(Parcel in) {
            return new SalesHeader(in);
        }

        @Override
        public SalesHeader[] newArray(int size) {
            return new SalesHeader[size];
        }
    };
    private double Subtotal, Potongan, DPP, PPnDPP, Total;
    private User Salesperson;
    private int Status, IDServer, IDLocal, IDKegiatan;
    private String OrderNo, Currency = "IDR", Notes, Syarat, Tempo;
    private Tanggal PromisedDate = new Tanggal(), InputDate = new Tanggal();
    private Customer customer;
    private ArrayList<Item> SalesLine;

    public SalesHeader() {
    }

    protected SalesHeader(Parcel in) {
        Subtotal = in.readDouble();
        Potongan = in.readDouble();
        DPP = in.readDouble();
        PPnDPP = in.readDouble();
        Total = in.readDouble();
        Salesperson = in.readParcelable(User.class.getClassLoader());
        Status = in.readInt();
        IDServer = in.readInt();
        IDLocal = in.readInt();
        IDKegiatan = in.readInt();
        OrderNo = in.readString();
        Currency = in.readString();
        Notes = in.readString();
        Syarat = in.readString();
        Tempo = in.readString();
        PromisedDate = in.readParcelable(Tanggal.class.getClassLoader());
        InputDate = in.readParcelable(Tanggal.class.getClassLoader());
        customer = in.readParcelable(Customer.class.getClassLoader());
        SalesLine = in.createTypedArrayList(Item.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(Subtotal);
        dest.writeDouble(Potongan);
        dest.writeDouble(DPP);
        dest.writeDouble(PPnDPP);
        dest.writeDouble(Total);
        dest.writeParcelable(Salesperson, flags);
        dest.writeInt(Status);
        dest.writeInt(IDServer);
        dest.writeInt(IDLocal);
        dest.writeInt(IDKegiatan);
        dest.writeString(OrderNo);
        dest.writeString(Currency);
        dest.writeString(Notes);
        dest.writeString(Syarat);
        dest.writeString(Tempo);
        dest.writeParcelable(PromisedDate, flags);
        dest.writeParcelable(InputDate, flags);
        dest.writeParcelable(customer, flags);
        dest.writeTypedList(SalesLine);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        this.Currency = currency;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }

    public String getSyarat() {
        return Syarat;
    }

    public void setSyarat(String syarat) {
        this.Syarat = syarat;
    }

    public String getTempo() {
        return Tempo;
    }

    public void setTempo(String tempo) {
        this.Tempo = tempo;
    }

    public String getOrderNo() {
        if (OrderNo != null) {
            if (OrderNo.equals(""))
                OrderNo = null;
        }
        return OrderNo;
    }

    public void setOrderNo(String orderno) {
        this.OrderNo = orderno;
    }

    public double getSubtotal() {
        double subtotal = 0.0;
        for (Item item : getSalesLine()) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.Subtotal = subtotal;
    }

    public double getPotongan() {
        return Potongan;
    }

    public void setPotongan(double potongan) {
        this.Potongan = potongan;
    }

    public double getDPP() {
        return getSubtotal() - getPotongan();
    }

    public void setDPP(double dpp) {
        this.DPP = dpp;
    }

    public double getPPnDPP() {
        return getDPP() * 10 / 100;
    }

    public void setPPnDPP(double ppndpp) {
        this.PPnDPP = ppndpp;
    }

    public double getTotal() {
        return getDPP() - getPPnDPP();
    }

    public void setTotal(double total) {
        this.Total = total;
    }

    public Customer getCustomer() {
        if (customer == null)
            customer = new Customer();
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        this.Status = status;
    }

    public int getIDServer() {
        return IDServer;
    }

    public void setIDServer(int HeaderID) {
        this.IDServer = HeaderID;
    }

    public ArrayList<Item> getSalesLine() {
        if (SalesLine == null)
            SalesLine = new ArrayList<>();
        return SalesLine;
    }

    public int getNextLineNo() {
        int maxLineNo = 0;
        for (Item item : getSalesLine()) {
            if (item.getLineNo() >= maxLineNo)
                maxLineNo = item.getLineNo();
        }
        return maxLineNo + 100;
    }

    public void setSalesLine(ArrayList<Item> sales_line) {
        this.SalesLine = sales_line;
    }

    public Tanggal getPromisedDate() {
        return PromisedDate;
    }

    public void setPromisedDate(Tanggal promisedDate) {
        PromisedDate = promisedDate;
    }

    public Tanggal getInputDate() {
        return InputDate;
    }

    public void setInputDate(Tanggal inputDate) {
        InputDate = inputDate;
    }

    public int getIDLocal() {
        return IDLocal;
    }

    public void setIDLocal(int IDLocal) {
        this.IDLocal = IDLocal;
    }

    public int getIDKegiatan() {
        return IDKegiatan;
    }

    public void setIDKegiatan(int IDKegiatan) {
        this.IDKegiatan = IDKegiatan;
    }

    public boolean isEditable() {
        return getStatus() == STATUS_ORDER_OPEN || getStatus() == STATUS_ORDER_ANDROID_EDIT;
    }

    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SALES_HEADER_ID_KEGIATAN, IDKegiatan);
            jsonObject.put(SALES_HEADER_ID_SERVER, IDServer);
            jsonObject.put(SALES_HEADER_ID_LOCAL, IDLocal);
            jsonObject.put(SALES_HEADER_ORDER_NO, OrderNo);
            jsonObject.put(SALES_HEADER_NOTES, Notes);
            jsonObject.put(SALES_HEADER_CURRENCY, Currency);
            jsonObject.put(SALES_HEADER_TEMPO, Tempo);
            jsonObject.put(SALES_HEADER_SYARAT_PEMBAYARAN, Syarat);
            jsonObject.put(SALES_HEADER_PROMISED_DELIVERY_DATE, PromisedDate.getTglString(Tanggal.FORMAT_DATE));
            jsonObject.put(SALES_HEADER_INPUT_DATE, InputDate.getTglString(Tanggal.FORMAT_DATE));
            jsonObject.put(SALES_HEADER_SUBTOTAL, getSubtotal());
            jsonObject.put(SALES_HEADER_POTONGAN, Potongan);
            jsonObject.put(SALES_HEADER_DPP, getDPP());
            jsonObject.put(SALES_HEADER_DPP_PPN, getPPnDPP());
            jsonObject.put(SALES_HEADER_TOTAL, getTotal());
            jsonObject.put(SALES_HEADER_STATUS, getStatus());

            jsonObject.put(SALES_HEADER_CUSTOMER, getCustomer().getAsJSON());
            jsonObject.put(SALES_HEADER_SALESPERSON, getSalesperson().getAsJSON());
            JSONArray jsonArraySalesLine = new JSONArray();
            for (Item item : getSalesLine()) {
                jsonArraySalesLine.put(item.getAsJSON());
            }
            jsonObject.put(SALES_HEADER_SALES_LINE, jsonArraySalesLine);

        } catch (Exception e) {
            Log.d(App.TAG, SalesHeader.class.getSimpleName() + " => getAsJSON  => " + e.toString());
        }
        return jsonObject;
    }

    public SalesHeader getFromJSON(Context context, String json) {
        return getFromJSON(context, JSON.toJSONObject(json));
    }

    public SalesHeader getFromJSON(Context context, JSONObject jsonObject) {
        IDKegiatan = JSON.getInt(jsonObject, SALES_HEADER_ID_KEGIATAN);
        IDServer = JSON.getInt(jsonObject, SALES_HEADER_ID_SERVER);
        IDLocal = JSON.getInt(jsonObject, SALES_HEADER_ID_LOCAL);
        OrderNo = getString(jsonObject, SALES_HEADER_ORDER_NO);
        Notes = getString(jsonObject, SALES_HEADER_NOTES);
        Currency = getString(jsonObject, SALES_HEADER_CURRENCY);
        Tempo = getString(jsonObject, SALES_HEADER_TEMPO);
        Syarat = getString(jsonObject, SALES_HEADER_SYARAT_PEMBAYARAN);
        if (getString(jsonObject, SALES_HEADER_PROMISED_DELIVERY_DATE) != null)
            setPromisedDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATE, getString(jsonObject, SALES_HEADER_PROMISED_DELIVERY_DATE))));
        if (getString(jsonObject, SALES_HEADER_INPUT_DATE) != null)
            setInputDate(new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, getString(jsonObject, SALES_HEADER_INPUT_DATE))));
        Potongan = JSON.getDouble(jsonObject, SALES_HEADER_POTONGAN);
        Status = JSON.getInt(jsonObject, SALES_HEADER_STATUS);
        if (getString(jsonObject, SALES_HEADER_CUSTOMER) != null)
            customer = new Customer().getFromJSON(context, JSON.getString(jsonObject, SALES_HEADER_CUSTOMER));

        ArrayList<Item> list_item = new ArrayList<>();
        try {
            JSONArray jsonArray = JSON.toJSONArray(getString(jsonObject, SALES_HEADER_SALES_LINE));
            if (jsonArray != null) {
                int jsonArraySize = jsonArray.length();
                for (int i = 0; i < jsonArraySize; i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    list_item.add(new Item().getFromJSON(jsonItem));
                }
            }
        } catch (Exception e) {
            Log.d(App.TAG, SalesHeader.class.getSimpleName() + " => getFromJSON  => " + e.toString());
        }
        setSalesperson(App.getUser(context));
        setSalesLine(list_item);
        return this;
    }

    public User getSalesperson() {
        return Salesperson;
    }

    public void setSalesperson(User salesperson) {
        Salesperson = salesperson;
    }
}