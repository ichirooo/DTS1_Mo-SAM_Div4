package co.id.datascrip.mo_sam_div4_dts1.callback;

import java.util.ArrayList;

import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import co.id.datascrip.mo_sam_div4_dts1.object.Feedback;

public class Callback {

    public interface CBLogin {
        public void onCB(BEX bex, String datetime);
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
