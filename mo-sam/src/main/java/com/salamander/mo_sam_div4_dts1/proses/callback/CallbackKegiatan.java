package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.Kegiatan;

import java.util.ArrayList;


public class CallbackKegiatan {

    public interface CBPost {
        void onCB(Kegiatan kegiatan);
    }

    public interface CBAction {
        void onCB(Kegiatan k);
    }

    public interface CBCheckInCheckOut {
        void onCB(Kegiatan k);
    }

    public interface CBPhoto {
        void onCB(boolean hasil);
    }

    public interface CBRefresh {
        void onCB(ArrayList<Kegiatan> list_kegiatan);
    }
}
