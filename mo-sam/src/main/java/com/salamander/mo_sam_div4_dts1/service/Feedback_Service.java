package com.salamander.mo_sam_div4_dts1.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.salamander.mo_sam_div4_dts1.R;
import com.salamander.mo_sam_div4_dts1.activity.Main_Activity;
import com.salamander.mo_sam_div4_dts1.object.Feedback;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.proses.Proses_Feedback;
import com.salamander.mo_sam_div4_dts1.proses.callback.CallbackFeedback;
import com.salamander.mo_sam_div4_dts1.sqlite.ItemSQLite;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Feedback_Service extends Service {

    private Context context;
    private User user;
    private int NOTIF_ID;
    public Runnable running = new Runnable() {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) <= 18) {
                new Proses_Feedback(context).LoadFeedback(new CallbackFeedback.CBLoad() {
                    @Override
                    public void onCB(ArrayList<Feedback> list_feedback) {
                        buildNotification(list_feedback.size());
                        stopSelf();
                    }
                });
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void buildNotification(int feedbacks_count) {
        Intent intent = new Intent(this, Main_Activity.class);
        intent.putExtra("notif_id", NOTIF_ID);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main_Activity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pi = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] vibrationPattern = {0, 500, 50, 100, 50, 100, 80, 100, 50};
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setTicker("Notifikasi Info Stock")
                .setContentTitle("Notifikasi Info Stock")
                .setContentText("New Stock Info")
                .setSmallIcon(R.drawable.icon_notification);

        if (feedbacks_count > 0) {
            notification.setVibrate(vibrationPattern);
            notification.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        style.setBigContentTitle("No. Order :");

        ArrayList<String> sos = new ItemSQLite(context).getErrorAndroSO(user);

        for (int i = 0; i < sos.size(); i++) {
            style.addLine(sos.get(i));
            ArrayList<String> codes = new ItemSQLite(context).getItemError(sos.get(i));
            for (int j = 0; j < codes.size(); j++) {
                style.addLine("  - " + codes.get(j));
            }
        }

        notification.setStyle(style);
        notification.setNumber(sos.size());
        notification.setContentIntent(pi);
        if (sos.size() > 0)
            manager.notify(NOTIF_ID, notification.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        user = intent.getParcelableExtra("user");
        NOTIF_ID = intent.getIntExtra("notif_id", 0);
        context = this;
        this.running.run();
        return START_REDELIVER_INTENT;
    }
}
