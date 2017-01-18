package co.id.datascrip.mo_sam_div4_dts1.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;

public class LoadingDialog extends ProgressDialog {

    private RobotoTextView txMsg;
    private ImageView imgAnim;
    private String message = "";
    private boolean cancelable = true;

    public LoadingDialog(Context context) {
        // TODO Auto-generated constructor stub
        super(context);
    }

    public LoadingDialog(Context context, String message) {
        // TODO Auto-generated constructor stub
        super(context);
        this.message = message;
    }

    public LoadingDialog(Context context, String message, boolean cancelable) {
        // TODO Auto-generated constructor stub
        super(context);
        this.message = message;
        this.cancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_loading_dialog);
        this.setCancelable(cancelable);
        txMsg = (RobotoTextView) findViewById(R.id.custom_loading_dialog_message);
        imgAnim = (ImageView) findViewById(R.id.custom_loading_dialog_image);
        imgAnim.setBackgroundResource(R.drawable.progress_dialog_anim);
        AnimationDrawable frameAnimation = (AnimationDrawable) imgAnim.getBackground();
        frameAnimation.start();
        txMsg.setText(message);
    }
}
