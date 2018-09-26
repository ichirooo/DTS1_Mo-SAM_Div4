package com.salamander.mo_sam_div4_dts1.custom;

import android.app.AlertDialog;
import android.content.Context;

public class TimeOutDialog extends AlertDialog {

    Context context;

    public TimeOutDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void show() {
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        d.setMessage("Tunggu beberapa saat kemudian coba lagi.");
        d.setTitle("Connection Timed Out");
        d.setPositiveButton("OK", null);
        d.show();
    }
}
