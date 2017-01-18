package co.id.datascrip.mo_sam_div4_dts1.custom;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import co.id.datascrip.mo_sam_div4_dts1.R;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoButton;
import co.id.datascrip.mo_sam_div4_dts1.font.RobotoTextView;

/**
 * Created by benny_aziz on 03/16/2015.
 */
public class ProgressDialogBar extends ProgressDialog {

    private RobotoTextView txPersen, txSize, txMessage;
    private ProgressBar progressBar;
    private Context context;
    private Button btCancel;

    public ProgressDialogBar(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progress_dialog);
        progressBar = (ProgressBar) findViewById(R.id.custom_progress_dialog_progressbar);
        txMessage = (RobotoTextView) findViewById(R.id.custom_progress_dialog_message);
        txPersen = (RobotoTextView) findViewById(R.id.custom_progress_dialog_tx_persen);
        txSize = (RobotoTextView) findViewById(R.id.custom_progress_dialog_tx_size);
        btCancel = (RobotoButton) findViewById(R.id.custom_progress_dialog_bt_cancel);
        progressBar.setProgress(0);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    public void setProgress(long currentProgressSize, long totalSize) {
        progressBar.setProgress((int) ((currentProgressSize / (float) totalSize) * 100));
        txPersen.setText(String.valueOf((int) ((currentProgressSize / (float) totalSize) * 100)) + "%");
        txSize.setText(Formatter.formatFileSize(context, currentProgressSize) + "/" + Formatter.formatFileSize(context, totalSize));
    }

    public void setMessage(String message) {
        txMessage.setText(message);
    }
}
