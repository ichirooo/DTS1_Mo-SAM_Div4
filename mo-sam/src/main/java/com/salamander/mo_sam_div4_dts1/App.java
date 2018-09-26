package com.salamander.mo_sam_div4_dts1;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.salamander.mo_sam_div4_dts1.activity.SplashScreen_Activity;
import com.salamander.mo_sam_div4_dts1.object.Kegiatan;
import com.salamander.mo_sam_div4_dts1.object.User;
import com.salamander.mo_sam_div4_dts1.sqlite.KegiatanSQLite;
import com.salamander.salamander_base_module.DialogUtils;
import com.salamander.salamander_base_module.util.Adapter_List_String;
import com.salamander.salamander_base_module.widget.SalamanderDialog;
import com.salamander.salamander_network.gson.GsonConverterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class App extends Application {

    public static final String TAG = "Mo-SAM-Div4";

    public static final String ACTION_GPS_LISTENER = "android.location.PROVIDERS_CHANGED";
    public static final int GPS_SETTING = 1111;
    public static final String CURRENCY_SYMBOL = "Rp. ";

    private static final boolean isLive = false;
    private static final String URL_DEMO = "http://dtswebapi.datascrip.co.id/demo/mo-sam-div4/android_data/";
    //private static final String URL_DEMO = "https://intraweb.datascrip.co.id/demo/mo-sam-div4/assets/NEW/DTS1/";
    private static final String URL_LIVE = "https://intraweb.datascrip.co.id/mo-sam-div4/android_data/";
    public static ArrayList<String> terms;
    public static int notifID = 1;
    public static String defaultTerms;
    //private static LocationReceiver locReceiver;
    //private static Context context;
    private SessionManager sessionManager;

    public static App getInstance(Context context) {
        return ((App) context.getApplicationContext());
    }

    public static User getUser(Context context) {
        return getSession(context).getUserLogin();
    }

    public static SessionManager getSession(Context context) {
        return getInstance(context).sessionManager;
    }

    public static String getVersion(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            version = "";
        }
        return version;
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

    public static boolean isGPSActive(Context context) {
        LocationManager locationManager = (LocationManager) (context.getSystemService(Context.LOCATION_SERVICE));
        assert locationManager != null;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void initLocationLibrary(Context context) {
        //LocationLibrary.showDebugOutput(true);

        try {
            //LocationLibrary.initialiseLibrary(context, 60 * 1000, 2 * 60 * 1000, "co.id.datacrip.mo_sam_div4_dts1");
            //LocationLibrary.forceLocationUpdate(context.getApplicationContext());
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
/*
    public static LocationReceiver getReceiver() {
        if (locReceiver == null)
            locReceiver = new LocationReceiver();
        return locReceiver;
    }*/

    public static void registerReceiver(Context context) {
        //final IntentFilter lftIntentFilter = new IntentFilter(LocationLibraryConstants.getLocationChangedPeriodicBroadcastAction());
        //getReceiver().register(context, lftIntentFilter);
        //LocationLibrary.forceLocationUpdate(context);
    }

    public static void unregisterReceiver(Context context) {
        //getReceiver().unregister(context);
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

    public static Retrofit createRetrofit(Context context) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);
        SSLContext sslContext = getSSLConfig(context);
        if (sslContext != null)
            client.sslSocketFactory(sslContext.getSocketFactory());
        return new Retrofit.Builder()
                .baseUrl(getURL(context))
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Retrofit createRetrofit(Context context, GsonConverterFactory gsonConverterFactory) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES);
        SSLContext sslContext = getSSLConfig(context);
        if (sslContext != null)
            client.sslSocketFactory(sslContext.getSocketFactory());
        return new Retrofit.Builder()
                .baseUrl(getURL(context))
                .client(client.build())
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    private static SSLContext getSSLConfig(Context context) { // throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = null;
        try {
            // Loading CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca;
            //InputStream cert = context.getResources().openRawResource(R.raw.datascrip_co_id);
            InputStream cert = new FileInputStream(getSession(context).getCertFile());
            try {
                ca = cf.generateCertificate(cert);
            } finally {
                if (cert != null)
                    cert.close();
            }

            // Creating a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Creating a TrustManager that trusts the CAs in our KeyStore.
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Creating an SSLSocketFactory that uses our TrustManager
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            Log.d("App", " => getSSLConfig([context])  => " + e.toString());
        }
        return sslContext;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);//, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            builder.connectTimeout(2, TimeUnit.MINUTES);
            builder.readTimeout(2, TimeUnit.MINUTES);
            builder.addInterceptor(interceptor);

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNotCheckedIn(Context context) {
        int id_kegiatan = getSession(context).getCheckedInKegiatan();
        if (id_kegiatan == 0)
            return true;
        else {
            String customer_name, tanggal = "";
            Kegiatan kegiatan = new KegiatanSQLite(context).get(id_kegiatan);
            customer_name = "<b>" + kegiatan.getSalesHeader().getCustomer().getName() + "</b>";
            tanggal = "<b>" + new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", getLocale()).format(new Date(getSession(context).getLastCheckIn())) + "</b>";
            DialogUtils.showErrorMessage(context, "Anda masih Check In di <br/>" + customer_name + " <br/>sejak : " + tanggal + "<br/>Silakan Check Out dulu untuk melanjutkan.", false);
            return false;
        }
    }

    public static Locale getLocale() {
        return new Locale("id");
    }

    public static String getApplicationName(Context context) {
        return context.getString(R.string.app_name);
    }

    public static boolean isConnectedToInternet(Context context, final OnTryAgain onTryAgain) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else {
            new SalamanderDialog(context)
                    .setDialogType(SalamanderDialog.DIALOG_ERROR)
                    .setMessage("Tidak terkoneksi ke Internet.\nSilakan check koneksi internet dan coba lagi.")
                    .setPositiveButton("Coba Lagi", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onTryAgain != null)
                                onTryAgain.onTryAgain();
                        }
                    })
                    .cancelable(!(context instanceof SplashScreen_Activity))
                    .show();
            return false;
        }
    }

    public static void setAdapter(Context context, final Adapter_List_String adapter, final OnPilih onPilih) {
        View custom_view = LayoutInflater.from(context).inflate(R.layout.listview_list_layout, null);

        final ListView listView = custom_view.findViewById(R.id.listview_list_listview);
        final LinearLayout ll_title = custom_view.findViewById(R.id.listview_ll_title);
        ll_title.setVisibility(View.GONE);

        listView.setAdapter(adapter);
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onPilih.onPilih(adapter.getItem(position));
                dialog.dismiss();
            }
        });
        dialog.setView(custom_view);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
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
        //initLocationLibrary(this);
    }

    public interface OnTryAgain {
        void onTryAgain();
    }

    public interface OnPilih {
        void onPilih(String selectedText);
    }
}
