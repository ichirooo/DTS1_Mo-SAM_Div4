package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.Customer;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.Item;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.SalesHeader;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_network.RetroStatus;

import java.util.ArrayList;

public class Callbacks {

    public interface OnCB {
        void onCB(RetroStatus retroStatus);
    }

    public interface OnCBLogin {
        void onCB(RetroStatus retroStatus, User user, String datetime);
    }

    public interface OnCBKegiatan {
        void onCB(RetroStatus retroStatus, Kegiatan kegiatan);
    }

    public interface OnCBGenerateCodeCustomer {
        void onCB(RetroStatus retroStatus, String customer_code);
    }

    public interface OnCBSalesOrder {
        void onCB(RetroStatus retroStatus, SalesHeader salesHeader);
    }

    public interface OnCBGenerateNoOrder {
        void onCB(RetroStatus retroStatus, String no_order);
    }

    public interface OnCBCheckOrderStatus {
        void onCB(RetroStatus status, int sales_status);
    }

    public interface OnCBCheckVersion {
        void onCB(RetroStatus status, String version, String link_download);
    }

    public interface OnCBFindItem {
        void onCB(RetroStatus status, ArrayList<Item> list_item);
    }

    public interface OnCBGetPrice {
        void onCB(RetroStatus status, double price);
    }

    public interface onCBLocation {
        void onCB(RetroStatus status, Kegiatan kegiatan);
    }

    public interface onCBCustomers {
        void onCB(RetroStatus status, ArrayList<Customer> list_customer);
    }

    public interface CBLoadData {
        public void onCB();
    }

    public interface CBFeedback {
        public void onCB(ArrayList<Feedback> feedbacks);
    }

    public interface CBPosition {
        public void onCB();
    }

    public interface CBReply {
        public void onCB(String feed_notes);
    }
}
