package co.id.datascrip.mo_sam_div4_dts1.callback;

import co.id.datascrip.mo_sam_div4_dts1.object.BEX;

public class CallbackLogin {

    public interface CBLogin {
        void onCB(BEX bex, String datetime);
    }
}
