package co.id.datascrip.mo_sam_div4_dts1.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import co.id.datascrip.mo_sam_div4_dts1.Const;
import co.id.datascrip.mo_sam_div4_dts1.Global;

public class ServiceManager {

    public static void startService(Context context) {
        Intent intent = new Intent(context, Feedback_Service.class);
        intent.putExtra("bex", Global.getBEX(context));
        intent.putExtra("notif", Global.notifID);
        PendingIntent piHeartBeatService = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Const.FEEDBACK_INTERVAL, piHeartBeatService);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, Feedback_Service.class);
        intent.putExtra("bex", Global.getBEX(context));
        intent.putExtra("notif", Global.notifID);
        PendingIntent piHeartBeatService = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
    }
}
