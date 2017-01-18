package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SalesHeader implements Parcelable {

    public static final Parcelable.Creator<SalesHeader> CREATOR = new Creator<SalesHeader>() {

        @Override
        public SalesHeader createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            SalesHeader header = new SalesHeader();
            header.Subtotal = source.readDouble();
            header.Potongan = source.readDouble();
            header.DPP = source.readDouble();
            header.PPnDPP = source.readDouble();
            header.Total = source.readDouble();
            header.OrderNo = source.readString();
            header.Currency = source.readString();
            header.Notes = source.readString();
            header.Syarat = source.readString();
            header.Tempo = source.readString();
            header.Status = source.readInt();
            header.HeaderID = source.readInt();
            header.PromisedDate = source.readString();
            header.customer = source.readParcelable(Customer.class.getClassLoader());
            source.readTypedList(header.sales_line, Item.CREATOR);
            return header;
        }

        @Override
        public SalesHeader[] newArray(int size) {
            // TODO Auto-generated method stub
            return new SalesHeader[size];
        }
    };
    private double Subtotal, Potongan, DPP, PPnDPP, Total;
    private int Status, HeaderID;
    private String OrderNo, Currency = "IDR", Notes, Syarat, Tempo, PromisedDate;
    private Customer customer;
    private ArrayList<Item> sales_line;

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
        return Subtotal;
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
        return DPP;
    }

    public void setDPP(double dpp) {
        this.DPP = dpp;
    }

    public double getPPnDPP() {
        return PPnDPP;
    }

    public void setPPnDPP(double ppndpp) {
        this.PPnDPP = ppndpp;
    }

    public double getTotal() {
        return Total;
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

    public int getHeaderID() {
        return HeaderID;
    }

    public void setHeaderID(int HeaderID) {
        this.HeaderID = HeaderID;
    }

    public String getPromisedDate() {
        return PromisedDate;
    }

    public void setPromisedDate(String promisedDate) {
        this.PromisedDate = promisedDate;
    }

    public ArrayList<Item> getSalesLine() {
        if (sales_line == null)
            sales_line = new ArrayList<Item>();
        return sales_line;
    }

    public void setSalesLine(ArrayList<Item> sales_line) {
        this.sales_line = sales_line;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeDouble(Subtotal);
        dest.writeDouble(Potongan);
        dest.writeDouble(DPP);
        dest.writeDouble(PPnDPP);
        dest.writeDouble(Total);
        dest.writeString(OrderNo);
        dest.writeString(Currency);
        dest.writeString(Notes);
        dest.writeString(Syarat);
        dest.writeString(Tempo);
        dest.writeInt(Status);
        dest.writeInt(HeaderID);
        dest.writeString(PromisedDate);
        dest.writeParcelable(customer, flags);
        dest.writeTypedList(sales_line);
    }
}