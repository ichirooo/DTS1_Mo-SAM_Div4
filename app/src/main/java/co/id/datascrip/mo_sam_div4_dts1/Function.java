package co.id.datascrip.mo_sam_div4_dts1;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.id.datascrip.mo_sam_div4_dts1.custom.ExceptionDialog;
import co.id.datascrip.mo_sam_div4_dts1.custom.TimeOutDialog;
import co.id.datascrip.mo_sam_div4_dts1.custom.UnknownHostExceptionDialog;
import co.id.datascrip.mo_sam_div4_dts1.process.Process_Send_Position;
import co.id.datascrip.mo_sam_div4_dts1.sqlite.ConstSQLite;

public class Function {

    private final File backupFolder = new File(Environment.getExternalStorageDirectory(), "DataLog/DTS1");
    private final File dataFolder = Environment.getDataDirectory();
    Context context;
    private StringBuilder isiText;
    private File logFile;
    private String time;
    private Intent intent;
    private PendingIntent pIntent;

    public Function() {
        isiText = new StringBuilder();
        SimpleDateFormat f = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String d = f.format(new Date().getTime());
        time = ft.format(new Date().getTime());
        logFile = new File(backupFolder, d + ".txt");
    }

    public Function(Context context) {
        this.context = context;
        isiText = new StringBuilder();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String d = f.format(new Date().getTime());
        time = ft.format(new Date().getTime());
        logFile = new File(backupFolder, d + ".txt");
    }

    /*
        public void startPositionService(Kegiatan kegiatan) {
            intent = null;
            pIntent = null;
            intent = new Intent(context, Position.class);
            intent.putExtra("bex", Global.getBEX().getEmpNo());
            intent.putExtra("id_kegiatan", kegiatan.getID());
            intent.setAction(Global.getBEX().getInitial());
            pIntent = PendingIntent.getService(context, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Const.POSITION_UPDATE, pIntent);
            new Function().writeToText("test", "test");
        }

        public void stopPositionService() {
            intent = new Intent(context, Position.class);
            intent.setAction(Global.getBEX().getInitial());
            pIntent = PendingIntent.getService(context, 0, intent, 0);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pIntent);
            if (pIntent != null) {
                pIntent.cancel();
            }
        }
    */
    public static void sendLocation(Context context, int id_kegiatan) {
        SessionManager sessionManager = new SessionManager(context);

        double latitude = sessionManager.getLastLocation().latitude;
        double longitude = sessionManager.getLastLocation().longitude;

        if (latitude != 0 && longitude != 0) {
            /*
            new Proses_Send_Position(context).execute(String.valueOf(id_kegiatan),
                    String.valueOf(Global.getBEX().getEmpNo()),
                    String.valueOf(latitude),
                    String.valueOf(longitude));
            */
            new Process_Send_Position().Send_Position(context, String.valueOf(id_kegiatan),
                    String.valueOf(Global.getBEX(context).getInitial()),
                    String.valueOf(Global.getBEX(context).getEmpNo()),
                    String.valueOf(latitude),
                    String.valueOf(longitude));
            sessionManager.setLastLocation(new Date(), (float) latitude, (float) longitude);
        }
    }

    public void writeToText(String action, String text) {
        try {
            if (!backupFolder.exists()) {
                backupFolder.mkdirs();
            }

            readFromText();
            isiText.append(time);
            isiText.append('\n');
            isiText.append(action + " : ");
            isiText.append('\n');
            isiText.append(text);
            isiText.append('\n');

            try {

                if (!logFile.exists()) {
                    logFile.createNewFile();
                }

                FileOutputStream fo = new FileOutputStream(logFile);
                OutputStreamWriter writer = new OutputStreamWriter(fo);
                writer.append(isiText);
                writer.close();
                fo.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void readFromText() {
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
                // TODO Auto-generated catch block
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkException(boolean rto, boolean uh, boolean exc, String exception) {
        if (rto) {
            new TimeOutDialog(context).show();
            return true;
        } else if (uh) {
            new UnknownHostExceptionDialog(context).show();
            return true;
        } else if (exc) {
            new ExceptionDialog(context, exception).show();
            return true;
        } else
            return false;
    }

    public void backupDatabase() {
        try {
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

            new Function(context).writeToText("backup", "Suckseed");
            Toast.makeText(context, "Back Up Successfully", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            // TODO: handle exception
            new Function(context).writeToText("backup", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void restoreDatabase() {
        final EditText pwd = new EditText(context);
        pwd.setSingleLine();
        pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

        new AlertDialog.Builder(context)
                .setTitle("Password")
                .setView(pwd)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        if (pwd.getText().toString().equals("20102014")) {
                            try {
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

                                new Function(context).writeToText("restore", "Suckseed");
                                Toast.makeText(context, "Restored Successfully", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                // TODO: handle exception
                                new Function(context).writeToText("restore", e.toString());
                                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }
}
