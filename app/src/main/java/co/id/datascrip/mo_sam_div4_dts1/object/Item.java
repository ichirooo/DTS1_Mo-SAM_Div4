package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Item itemsDetail = new Item();
            itemsDetail.ID = source.readInt();
            itemsDetail.HeaderID = source.readInt();
            itemsDetail.Code = source.readString();
            itemsDetail.Description = source.readString();
            itemsDetail.Unit = source.readString();
            itemsDetail.Quantity = source.readInt();
            itemsDetail.Price = source.readDouble();
            itemsDetail.Discount = source.readDouble();
            itemsDetail.DiscPct = source.readDouble();
            itemsDetail.Subtotal = source.readDouble();
            itemsDetail.Error = source.readInt();
            itemsDetail.Notes = source.readString();
            itemsDetail.InputDate = source.readString();
            return itemsDetail;
        }

        @Override
        public Item[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Item[size];
        }
    };
    private String Code, Description, Unit, Notes, InputDate;
    private int Quantity, Error, ID, HeaderID;
    private double Price, Discount, DiscPct, Subtotal;

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int headercode) {
        this.HeaderID = headercode;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        this.Code = code;
    }

    public String getDesc() {
        return Description;
    }

    public void setDesc(String description) {
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

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        this.Discount = discount;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.Subtotal = subtotal;
    }

    public double getDiscPct() {
        return DiscPct;
    }

    public void setDiscPct(double discpct) {
        this.DiscPct = discpct;
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

    public String getInputDate() {
        return InputDate;
    }

    public void setInputDate(String inputDate) {
        InputDate = inputDate;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(ID);
        dest.writeInt(HeaderID);
        dest.writeString(Code);
        dest.writeString(Description);
        dest.writeString(Unit);
        dest.writeInt(Quantity);
        dest.writeDouble(Price);
        dest.writeDouble(Discount);
        dest.writeDouble(DiscPct);
        dest.writeDouble(Subtotal);
        dest.writeInt(Error);
        dest.writeString(Notes);
        dest.writeString(InputDate);
    }
}
