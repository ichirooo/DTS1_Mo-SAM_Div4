package com.salamander.mo_sam_div4_dts1;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.salamander.salamander_base_module.Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@com.bumptech.glide.annotation.GlideModule
public class MOSAMGlideModule extends AppGlideModule {

    private static SSLContext getSSLConfig(Context context) { // throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = null;
        try {
            // Loading CAs from an InputStream
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate ca;
            //InputStream cert = context.getResources().openRawResource(R.raw.datascrip_co_id);
            InputStream cert = new FileInputStream(App.getSession(context).getCertFile());
            try {
                ca = cf.generateCertificate(cert);
            } finally {
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
            //Log.e("Global", " => getSSLConfig([context])  => " + e.toString());
            Utils.showLog(MOSAMGlideModule.class.getSimpleName(), "getSSLConfig([context])", e.toString());
        }
        return sslContext;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES);
        SSLContext sslContext = getSSLConfig(context);
        if (sslContext != null)
            client.sslSocketFactory(sslContext.getSocketFactory());
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client.build()));
    }
}
