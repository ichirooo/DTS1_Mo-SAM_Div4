package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.Feedback;

public class CallbackFeedback {

    public interface CBLoad {
        void onCB(ArrayList<Feedback> list_feedback);
    }

    public interface CBPost {
        void onCB(String feedback_note);
    }
}
