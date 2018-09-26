package com.salamander.mo_sam_div4_dts1.proses.callback;

import com.salamander.mo_sam_div4_dts1.object.Feedback;

import java.util.ArrayList;

public class CallbackFeedback {

    public interface CBLoad {
        void onCB(ArrayList<Feedback> list_feedback);
    }

    public interface CBPost {
        void onCB(String feedback_note);
    }
}
