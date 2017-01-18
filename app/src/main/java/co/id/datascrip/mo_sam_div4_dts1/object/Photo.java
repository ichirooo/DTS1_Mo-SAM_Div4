package co.id.datascrip.mo_sam_div4_dts1.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {

    public static final Parcelable.Creator<Photo> CREATOR = new Creator<Photo>() {

        @Override
        public Photo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            Photo photo = new Photo();
            photo.ID = source.readInt();
            photo.id_kegiatan = source.readInt();
            photo.Path = source.readString();
            photo.Info = source.readString();
            photo.isPosted = source.readInt();
            return photo;
        }

        @Override
        public Photo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Photo[size];
        }
    };
    private String Path, Info;
    private int ID, id_kegiatan, isPosted = 0;

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public int getIdKegiatan() {
        return id_kegiatan;
    }

    public void setIdKegiatan(int id_kegiatan) {
        this.id_kegiatan = id_kegiatan;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        this.Path = path;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        this.Info = info;
    }

    public int getPosted() {
        return isPosted;
    }

    public void setPosted(int isposted) {
        this.isPosted = isposted;
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
        dest.writeInt(id_kegiatan);
        dest.writeString(Path);
        dest.writeString(Info);
        dest.writeInt(isPosted);
    }
}
