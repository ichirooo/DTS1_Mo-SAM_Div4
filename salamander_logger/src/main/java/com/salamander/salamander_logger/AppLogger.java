package com.salamander.salamander_logger;

import android.content.Context;
import android.os.Environment;

import com.salamander.salamander_logger.sqlite.DBHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class AppLogger {

    private static final File backupFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
    private static File dataFolder = Environment.getDataDirectory();

    public static File getBackupDatabaseFile(Context context, String applicationName) {
        String backupDBPath = DBHelper.getDBName(context);
        return new File(backupFolder + applicationName, backupDBPath);
    }

    public static void backupDatabase(Context context, String applicationName) {
        try {
            String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//" + DBHelper.getDBName(context);
            String backupDBPath = DBHelper.getDBName(context);

            File currentDB = new File(dataFolder, currentDBPath);
            File backupDB = new File(backupFolder + applicationName, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();

            dst.transferFrom(src, 0, src.size());

            src.close();
            dst.close();
        } catch (Exception e) {
        }
    }
}