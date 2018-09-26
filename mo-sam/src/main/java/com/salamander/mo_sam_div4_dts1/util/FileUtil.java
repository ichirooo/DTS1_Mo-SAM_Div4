package com.salamander.mo_sam_div4_dts1.util;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.App;
import com.salamander.mo_sam_div4_dts1.sqlite.ConstSQLite;
import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_network.Retro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FileUtil {

    public static final String appName = "Mo-SAM";
    private static final File logFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + appName + "/Log");
    private static final File backupFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + appName);
    private static File dataFolder = Environment.getDataDirectory();
    private static File logFile;
    private static StringBuilder isiText;

    public static String getPhotoDirectory(Context context) {
        File imageFolder = new File(Environment.getExternalStorageDirectory(), "/" + App.getApplicationName(context) + "/Picture");
        boolean created = false;
        if (!imageFolder.exists())
            created = imageFolder.mkdirs();
        return imageFolder.getAbsolutePath() + File.separator;
    }

    public static void CopyFile(Context context, File sourceFile, File destFile) {
        try {

            FileChannel src = new FileInputStream(sourceFile).getChannel();
            FileChannel dst = new FileOutputStream(destFile).getChannel();

            dst.transferFrom(src, 0, src.size());

            src.close();
            dst.close();
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static String getFileExtension(File file) {
        String fileName = file.getAbsolutePath();
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static File getBackupDatabaseFile() {
        String backupDBPath = ConstSQLite.DATABASE_NAME;
        return new File(backupFolder, backupDBPath);
    }

    public static void backupDatabase(Context context, boolean showToast) {
        try {
            if (!logFolder.exists())
                logFolder.mkdirs();
            String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//"
                    + ConstSQLite.DATABASE_NAME;
            String backupDBPath = ConstSQLite.DATABASE_NAME;

            File currentDB = new File(dataFolder, currentDBPath);
            File backupDB = new File(backupFolder, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();

            dst.transferFrom(src, 0, src.size());

            src.close();
            dst.close();

            if (showToast)
                Toast.makeText(context, "Database Backed Up Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            if (showToast)
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void restoreDatabase(Context context) {
        try {
            if (!logFolder.exists())
                logFolder.mkdirs();
            String currentDBPath = "//data//" + context.getApplicationContext().getPackageName() + "//databases//"
                    + ConstSQLite.DATABASE_NAME;
            String backupDBPath = ConstSQLite.DATABASE_NAME;
            File toDB = new File(dataFolder, currentDBPath);
            File fromDB = new File(backupFolder, backupDBPath);

            FileChannel src = new FileInputStream(fromDB).getChannel();
            FileChannel dst = new FileOutputStream(toDB).getChannel();

            dst.transferFrom(src, 0, src.size());

            src.close();
            dst.close();

            Toast.makeText(context, "Database Restored Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /*
        public static void writeNetworkLog(Context context, String function, String parameter, Status status) {
            writeNetworkLog(context, function, parameter, status.getHeader(), status.getMessage());
        }
    */
    public static void writeNetworkLog(Context context, String function, String parameter, String rawResponse, String respon) {
        try {
            if (!logFolder.exists())
                logFolder.mkdirs();
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd", App.getLocale());
            SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss", App.getLocale());

            String tgl = f.format(new Date().getTime());
            String jam = ft.format(new Date().getTime());

            logFile = new File(logFolder, tgl + "-" + App.getUser(context).getInitial() + ".txt");
            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            if (isiText == null)
                isiText = new StringBuilder();

            //readLog(context);
            if (respon != null) {
                String tempText = respon.replace(" ", "");
                if (tempText.contains("<body>") && tempText.contains("</body>"))
                    respon = respon.substring(respon.indexOf("<body>"), respon.indexOf("</body>") + "</body>".length());
            }

            isiText.append(jam);
            isiText.append('\n');
            isiText.append(function);
            isiText.append(" : ");
            if (parameter != null) {
                isiText.append('\n');
                isiText.append("Params");
                isiText.append(" : ");
                isiText.append(parameter);
            }
            if (rawResponse != null) {
                isiText.append('\n');
                isiText.append(rawResponse);
            }
            if (respon != null) {
                isiText.append('\n');
                isiText.append(respon);
            }
            isiText.append('\n');
            isiText.append('\n');

            try {
                FileOutputStream fo = new FileOutputStream(logFile);
                OutputStreamWriter writer = new OutputStreamWriter(fo);

                writer.append(isiText);

                writer.close();
                fo.close();
            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void writeNetworkLog(Context context, String function, String parameter, Throwable throwable) {
        writeNetworkLog(context, function, parameter, throwable.getClass().getSimpleName(), throwable.getMessage());
    }

    public static void writeNetworkLog(Context context, String function, Response<ResponseBody> response) {
        String respon = Retro.getString(response);
        String rawResponse = response.raw().toString();
        writeNetworkLog(context, function, null, rawResponse, respon);
    }

    public static void writeExceptionLog(Context context, String function, Exception exception) {
        String exceptionMessage = exception.toString();
        writeNetworkLog(context, function + exception.getClass().getSimpleName(), null, "", exceptionMessage);
    }

    public static void writeNetworkLog(Context context, String function, String parameter, Response<ResponseBody> response, String respon) {
        String rawResponse = response.raw().toString();
        writeNetworkLog(context, function, parameter, rawResponse, respon);
    }

    private static void readLog(Context context) {
        try {
            FileInputStream fi = new FileInputStream(logFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fi));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    isiText.append(line);
                    isiText.append('\n');
                }
                br.close();
                fi.close();
            } catch (IOException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static File getLog(Context context, Date selectedDate) {
        if (logFolder.exists()) {
            String tgl = DateUtils.dateToString("yyyyMMdd", selectedDate);
            logFile = new File(logFolder, tgl + "-" + App.getUser(context).getInitial() + ".txt");
            if (logFile.exists())
                return logFile;
            else return null;
        } else return null;
    }
}
