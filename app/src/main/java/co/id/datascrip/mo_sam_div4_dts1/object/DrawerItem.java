package co.id.datascrip.mo_sam_div4_dts1.object;

public class DrawerItem {

    String itemName, title;
    int imgID;

    public DrawerItem(String itemname, int imgID) {
        super();
        itemName = itemname;
        this.imgID = imgID;
    }

    public String getitemName() {
        return itemName;
    }

    public void setitemName(String itemname) {
        itemName = itemname;
    }

    public int getimgID() {
        return imgID;
    }

    public void setimgID(int imgID) {
        this.imgID = imgID;
    }

}
