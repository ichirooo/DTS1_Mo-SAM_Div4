package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Kegiatan implements Parcelable {

    public static final Parcelable.Creator<Kegiatan> CREATOR = new Creator<Kegiatan>() {

        @Override
        public Kegiatan createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Kegiatan kegiatan = new Kegiatan();
            kegiatan.id_kegiatan = source.readInt();
            kegiatan.lokasi = source.readString();
            kegiatan.startDate = source.readString();
            kegiatan.endDate = source.readString();
            kegiatan.tipe = source.readString();
            kegiatan.jenis = source.readString();
            kegiatan.subject = source.readString();
            kegiatan.cancel = source.readInt();
            kegiatan.cancelReason = source.readString();
            kegiatan.keterangan = source.readString();
            kegiatan.result = source.readString();
            kegiatan.resultDate = source.readString();
            kegiatan.checkIn = source.readInt();
            kegiatan.sales_header = source.readParcelable(SalesHeader.class.getClassLoader());
            kegiatan.bex = source.readParcelable(BEX.class.getClassLoader());
            source.readTypedList(kegiatan.photos, Photo.CREATOR);
            return kegiatan;
        }

        @Override
        public Kegiatan[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Kegiatan[size];
        }
    };
    private int id_kegiatan, cancel, checkIn;
    private String lokasi, tipe, jenis,
            subject, cancelReason, keterangan, result, resultDate, startDate, endDate;
    private SalesHeader sales_header;
    private ArrayList<Photo> photos;
    private BEX bex;

    public int getID() {
        return id_kegiatan;
    }

    public void setID(int id_kegiatan) {
        this.id_kegiatan = id_kegiatan;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultdate) {
        this.resultDate = resultdate;
    }

    public String getReason() {
        return cancelReason;
    }

    public void setReason(String reason) {
        this.cancelReason = reason;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startdate) {
        this.startDate = startdate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String enddate) {
        this.endDate = enddate;
    }

    public ArrayList<Photo> getPhotos() {
        if (photos == null)
            photos = new ArrayList<Photo>();
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public SalesHeader getSalesHeader() {
        if (sales_header == null)
            sales_header = new SalesHeader();
        return sales_header;
    }

    public void setSalesHeader(SalesHeader sales_header) {
        this.sales_header = sales_header;
    }

    public int getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(int checkIn) {
        this.checkIn = checkIn;
    }

    public BEX getBEX() {
        return bex;
    }

    public void setBEX(BEX bex) {
        this.bex = bex;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeInt(id_kegiatan);
        dest.writeString(lokasi);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(tipe);
        dest.writeString(jenis);
        dest.writeString(subject);
        dest.writeInt(cancel);
        dest.writeString(cancelReason);
        dest.writeString(keterangan);
        dest.writeString(result);
        dest.writeString(resultDate);
        dest.writeInt(checkIn);
        dest.writeParcelable(sales_header, flags);
        dest.writeParcelable(bex, flags);
        dest.writeTypedList(photos);
    }
}
