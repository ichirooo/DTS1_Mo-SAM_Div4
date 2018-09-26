package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.salamander_network.RetroStatus;

public class CallbackLogin {

    public interface CBLogin {
        void onCB(RetroStatus retroStatus, User user, String datetime);
    }
}
