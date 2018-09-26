package com.salamander.mo_sam_div4_dts1.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class ExceptionDialog extends AlertDialog {

    private Context context;
    private String exception;

    public ExceptionDialog(Context context) {
        super(context);
        this.context = context;
    }

    public ExceptionDialog(Context context, String exception) {
        super(context);
        this.context = context;
        this.exception = exception;
    }

    public void show() {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setMessage("Silakan coba kembali");
        d.setTitle("Unknown Error Occured");
        d.setPositiveButton("OK", null);
        d.show();
        Toast.makeText(context, exception, Toast.LENGTH_LONG).show();
    }
}
