package com.salamander.salamander_logger;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.salamander.salamander_base_module.DateUtils;
import com.salamander.salamander_base_module.object.Tanggal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = LoggingExceptionHandler.class.getSimpleName();
    private final static String ERROR_FILE = "LoggingExceptionHandler.error";

    private final Context context;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public LoggingExceptionHandler(Context context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable throwable) {
        //try {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));

        StringBuilder errorReport = new StringBuilder();
        errorReport.append("\n\n******** ERROR **********\n");
        errorReport.append("Time : " + DateUtils.dateToString(Tanggal.FORMAT_DATETIME_FULL, new Date()) + "\n");

        String[] stackTrace = stringWriter.toString().split("\n");
        for (String stack : stackTrace) {
            if (stack.toLowerCase().contains(context.getPackageName()) || stack.toLowerCase().contains("exception"))
                errorReport.append(stack + "\n");
        }

        errorReport.append("******** DEVICE INFORMATION **********\n");
        errorReport.append("Brand : ");
        errorReport.append(Build.BRAND);
        errorReport.append("\nDevice : ");
        errorReport.append(Build.DEVICE);
        errorReport.append("\nModel : ");
        errorReport.append(Build.MODEL);
        errorReport.append("\nProduct : ");
        errorReport.append(Build.PRODUCT);
        errorReport.append("\nSDK : ");
        errorReport.append(Build.VERSION.SDK_INT);
        errorReport.append("\nRelease : ");
        errorReport.append(Build.VERSION.RELEASE);
        Log.e(getClass().getName().substring(1, 25), errorReport.toString());
            /*
            StackTraceElement[] stackTraceElementList = throwable.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElementList) {
                String packageName = context.getApplicationContext().getPackageName();
                if (stackTraceElement.toString().contains(packageName) && !stackTraceElement.toString().contains("<init>")) {
                    AppLog appLog = new AppLog();
                    appLog.setClassName(stackTraceElement.getClassName());
                    appLog.setMethodName(stackTraceElement.getMethodName());
                    appLog.setLineNumber(stackTraceElement.getLineNumber());
                    appLog.setException(throwable.getMessage());
                    new AppLogSQLite(context).Post(appLog);
                    break;
                }
            }
            */
        //} catch (Exception e) {
        //Log.e(TAG, "Exception AppLogger failed!", e);
        //}
        Toast.makeText(context, errorReport.toString(), Toast.LENGTH_LONG).show();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
        //throw new IllegalArgumentException();
    }
}
