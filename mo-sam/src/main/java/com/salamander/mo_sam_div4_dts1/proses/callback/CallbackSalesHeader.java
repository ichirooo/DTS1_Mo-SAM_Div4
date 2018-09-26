package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;

import java.util.ArrayList;

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
