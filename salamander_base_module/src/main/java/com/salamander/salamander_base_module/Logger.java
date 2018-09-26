package com.salamander.salamander_base_module;

import android.content.Context;
import android.content.SharedPreferences;

public class Logger {

    private Context context;

    private SharedPreferences spf;
    private SharedPreferences.Editor editor;

    public Logger(Context context) {
        this.context = context.getApplicationContext();
        this.spf = this.context.getSharedPreferences(context.getApplicationContext().getPackageName() + "_log", 0);
        editor = spf.edit();
        editor.apply();
    }
}
