package com.salamander.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;
import com.salamander.salamander_network.JSON;

import org.json.JSONObject;

public class Item implements Parcelable {

    public static final String ITEM = "Item";
    public static final String ITEM_ID_SERVER = "IDServer";
    public static final String ITEM_ID_LOCAL = "IDLocal";
    public static final String ITEM_ID_HEADER = "IDHeader";
    public static final String ITEM_ORDER_NO = "OrderNo";
    public static final String ITEM_LINE_NO = "LineNo";
    public static final String ITEM_CODE = "Code";
    public static final String ITEM_DESCRIPTION = "Description";
    public static final String ITEM_QUANTITY = "Quantity";
    public static final String ITEM_UNIT = "Unit";
    public static final String ITEM_PRICE = "Price";
    public static final String ITEM_PRICE_NAV = "PriceNAV";
    public static final String ITEM_DISCOUNT_VALUE = "DiscountValue";
    public static final String ITEM_DISCOUNT_PCT = "DiscountPct";
    public static final String ITEM_SUBTOTAL = "Total";
    public static final String ITEM_INPUT_DATE = "InputDate";
    public static final String ITEM_NOTES = "Notes";
    public static final String ITEM_IS_ERROR = "Error";
    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    private String Code, Description, Unit, Notes, OrderNo;
    private int Quantity, Error, IDServer, IDLocal, IDHeader, LineNo;
    private double Price, PriceNAV, DiscountValue, DiscountPct, Subtotal;
    private Tanggal InputDate = new Tanggal();

    public Item() {
    }

    protected Item(Parcel in) {
        Code = in.readString();
        Description = in.readString();
        Unit = in.readString();
        Notes = in.readString();
        OrderNo = in.readString();
        Quantity = in.readInt();
        Error = in.readInt();
        IDServer = in.readInt();
        IDLocal = in.readInt();
        IDHeader = in.readInt();
        LineNo = in.readInt();
        Price = in.readDouble();
        PriceNAV = in.readDouble();
        DiscountValue = in.readDouble();
        DiscountPct = in.readDouble();
        Subtotal = in.readDouble();
        InputDate = in.readParcelable(Tanggal.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Code);
        dest.writeString(Description);
        dest.writeString(Unit);
        dest.writeString(Notes);
        dest.writeString(OrderNo);
        dest.writeInt(Quantity);
        dest.writeInt(Error);
        dest.writeInt(IDServer);
        dest.writeInt(IDLocal);
        dest.writeInt(IDHeader);
        dest.writeInt(LineNo);
        dest.writeDouble(Price);
        dest.writeDouble(PriceNAV);
        dest.writeDouble(DiscountValue);
        dest.writeDouble(DiscountPct);
        dest.writeDouble(Subtotal);
        dest.writeParcelable(InputDate, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIDServer() {
        return IDServer;
    }

    public void setIDServer(int id) {
        this.IDServer = id;
    }

    public int getIDHeader() {
        return IDHeader;
    }

    public void setIDHeader(int headercode) {
        this.IDHeader = headercode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public double getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.DiscountValue = discountValue;
    }

    public double getSubtotal() {
        return (getPrice() * getQuantity()) - getDiscountValue();
    }

    public void setSubtotal(double subtotal) {
        this.Subtotal = subtotal;
    }

    public double getDiscountPct() {
        return DiscountPct;
    }

    public void setDiscountPct(double discpct) {
        this.DiscountPct = discpct;
    }

    public int getError() {
        return Error;
    }

    public void setError(int error) {
        this.Error = error;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        this.Notes = notes;
    }

    public int getLineNo() {
        return LineNo;
    }

    public void setLineNo(int lineNo) {
        LineNo = lineNo;
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

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(ITEM_ID_HEADER, IDHeader);
            jsonObject.put(ITEM_LINE_NO, LineNo);
            jsonObject.put(ITEM_ID_SERVER, IDServer);
            jsonObject.put(ITEM_CODE, Code);
            jsonObject.put(ITEM_DESCRIPTION, Description);
            jsonObject.put(ITEM_QUANTITY, Quantity);
            jsonObject.put(ITEM_UNIT, Unit);
            jsonObject.put(ITEM_PRICE, Price);
            jsonObject.put(ITEM_PRICE_NAV, PriceNAV);
            jsonObject.put(ITEM_DISCOUNT_VALUE, DiscountValue);
            jsonObject.put(ITEM_DISCOUNT_PCT, DiscountPct);
            jsonObject.put(ITEM_INPUT_DATE, DateUtils.dateToString(Tanggal.FORMAT_DATETIME_FULL, InputDate.getDate()));
            jsonObject.put(ITEM_SUBTOTAL, getSubtotal());
        } catch (Exception e) {
            Log.d(App.TAG, Item.class.getSimpleName() + " => getAsJSON  => " + e.toString());
        }
        return jsonObject;
    }

    public Item getFromJSON(JSONObject jsonObject) {
        IDHeader = JSON.getInt(jsonObject, ITEM_ID_HEADER);
        IDServer = JSON.getInt(jsonObject, ITEM_ID_SERVER);
        OrderNo = JSON.getString(jsonObject, ITEM_ORDER_NO);
        LineNo = JSON.getInt(jsonObject, ITEM_LINE_NO);
        Quantity = JSON.getInt(jsonObject, ITEM_QUANTITY);
        Code = JSON.getString(jsonObject, ITEM_CODE);
        Description = JSON.getString(jsonObject, ITEM_DESCRIPTION);
        Unit = JSON.getString(jsonObject, ITEM_UNIT);
        PriceNAV = JSON.getDouble(jsonObject, ITEM_PRICE_NAV);
        Price = JSON.getDouble(jsonObject, ITEM_PRICE);
        DiscountValue = JSON.getDouble(jsonObject, ITEM_DISCOUNT_VALUE);
        DiscountPct = JSON.getDouble(jsonObject, ITEM_DISCOUNT_PCT);
        //InputDate = new Tanggal(DateUtils.stringToDate(Tanggal.FORMAT_DATETIME_FULL, JSON.getString(jsonObject, ITEM_INPUT_DATE)));

        if (Price == 0 && PriceNAV > 0)
            Price = PriceNAV;
        return this;
    }

    public double getPriceNAV() {
        return PriceNAV;
    }

    public void setPriceNAV(double priceNAV) {
        PriceNAV = priceNAV;
    }
}
