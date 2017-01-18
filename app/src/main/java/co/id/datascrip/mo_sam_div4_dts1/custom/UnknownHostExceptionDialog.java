package co.id.datascrip.mo_sam_div4_dts1.custom;

import android.app.AlertDialog;
import android.content.Context;

public class UnknownHostExceptionDialog extends AlertDialog {

    private Context context;

    public UnknownHostExceptionDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setTitle("Tidak dapat terhubung ke internet");
        d.setMessage("Silakan cek koneksi internet anda kemudian coba lagi.");
        d.setPositiveButton("OK", null);
        d.show();
    }
}
