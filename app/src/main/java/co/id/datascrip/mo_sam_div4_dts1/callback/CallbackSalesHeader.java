package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Item;
import co.id.datascrip.mo_sam_div4_dts1.object.Kegiatan;

public class CallbackSalesHeader {

    public interface CBNoOrder {
        void onCB(String no_order);
    }

    public interface CBFindItem {
        void onCB(ArrayList<Item> items);
    }

    public interface CBPosting {
        void onCB(Kegiatan kegiatan);
    }

    public interface CBCheckStatus {
        void onCB(int sales_status);
    }

    public interface CBGetPrice {
        void onCB(boolean success, double price);
    }
}
