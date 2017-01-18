package co.id.datascrip.mo_sam_div4_dts1;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationLibrary;
import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationLibraryConstants;
import co.id.datascrip.mo_sam_div4_dts1.littlefluffy.LocationReceiver;
import co.id.datascrip.mo_sam_div4_dts1.object.BEX;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class Global extends Application {

    public static final String ACTION_GPS_LISTENER = "android.location.PROVIDERS_CHANGED";
    public static final int GPS_SETTING = 1111;
    public static final String SISTEM = "DTS1";
    private static final boolean isLive = false;
    private static final String URL_DEMO = "https://intraweb.datascrip.co.id/demo/mo-sam-div4/data_android/";
    private static final String URL_LIVE = "https://intraweb.datascrip.co.id/mo-sam-div4/data_android/";
    public static File file;
    public static File photo_path;
    public static ArrayList<String> terms;
    public static int notifID = 1;
    public static String defaultTerms;
    public static String[] term = new String[]{"0", "3", "7", "10", "14", "21", "28", "30", "45",
            "60", "65", "70", "75", "90", "120", "150", "180", "6 X 30", "12 X 30"};
    private static LocationReceiver locReceiver;
    //private static Context context;
    private SessionManager sessionManager;

    public static Global getInstance(Context context) {
        return ((Global) context.getApplicationContext());
    }

    public static BEX getBEX(Context context) {
        return getInstance(context).sessionManager.getUserLogin();
    }

    public static SessionManager getSession(Context context) {
        return getInstance(context).sessionManager;
    }

    public static String getVersionText(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            version = "";
        }

        if (isLive)
            return "DTS1 v" + version + " Live";
        else return "DTS1 v" + version + " Demo";
    }

    public static void initLocationLibrary(Context context) {
        LocationLibrary.showDebugOutput(true);

        try {
            LocationLibrary.initialiseLibrary(context, 60 * 1000, 2 * 60 * 1000, "co.id.datacrip.mo_sam_div4_dts1");
            LocationLibrary.forceLocationUpdate(context.getApplicationContext());
        } catch (UnsupportedOperationException ex) {
            Log.d("MoSam_DIV4_DTS1", "UnsupportedOperationException thrown - the device doesn't have any location providers");
        }
    }

    public static void saveFile(Bitmap b, File file) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static LocationReceiver getReceiver() {
        if (locReceiver == null)
            locReceiver = new LocationReceiver();
        return locReceiver;
    }

    public static void registerReceiver(Context context) {
        final IntentFilter lftIntentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        getReceiver().register(context, lftIntentFilter);
        LocationLibrary.forceLocationUpdate(context);
    }

    public static void unregisterReceiver(Context context) {
        getReceiver().unregister(context);
    }

    public static String getURL(Context context) {
        String buildtipe;
        buildtipe = getProperty(context);
        buildtipe = (buildtipe == null) ? "Demo" : buildtipe;

        return (buildtipe.toLowerCase().trim().equals("demo")) ? URL_DEMO : URL_LIVE;
    }

    public static String getProperty(Context context) {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open("config.properties");
            properties.load(inputStream);
            return properties.getProperty("BUILD_TYPE");
        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static Retrofit CreateRetrofit(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        return new Retrofit.Builder()
                .baseUrl(getURL(context))
                .client(client)
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = new SessionManager(getApplicationContext());
        initLocationLibrary(this);
    }
}
