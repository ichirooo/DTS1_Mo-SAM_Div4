package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.HashMap;

public class CallbackLocation {

    public interface CBLocation {
        void onCB(HashMap<String, String> locations);
    }

}
