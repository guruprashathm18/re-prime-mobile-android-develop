package com.royalenfield.reprime.rest.web;

import android.content.Context;

import com.bosch.softtec.components.midgard.core.network.SslConfiguration;
import com.royalenfield.reprime.BuildConfig;
import com.royalenfield.reprime.R;
import com.royalenfield.reprime.application.REApplication;
import com.royalenfield.reprime.utils.REConstants;
import com.royalenfield.reprime.utils.REUtils;

import org.jetbrains.annotations.NotNull;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import com.royalenfield.reprime.utils.RELog;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

public class RESSLClient {

    /**
     * Creates OKHttpClient using certificate
     *
     * @param context : Context
     * @return OkHttpClient
     */
    public static OkHttpClient createClient(Context context, boolean isCertRequired) {
        OkHttpClient mOKHttpClient = null;
//        X509TrustManager trustManager = null;
//        try {
//            trustManager = createTrustManager(context);
//        } catch (GeneralSecurityException e) {
//            RELog.e(e);
//        }
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NotNull String s) {
               RELog.e("HTTP LOGGING", s);
            }
        });
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        //Don't remove the interceptor as TBT Ingress API is relying on this to add headers.
        Interceptor interceptorForHeader = new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request()
                        .newBuilder()
                        .addHeader(REConstants.KEY_APP_ID, "" + REConstants.APP_ID)
                        .addHeader(REConstants.KEY_CONTENT_TYPE, REConstants.CONTENT_TYPE)
                        .addHeader(REConstants.KEY_CUSTOM_LANG, REConstants.CUSTOM_LANG)
                        .addHeader(REConstants.KEY_CUSTOM_COUNTRY, REApplication.getInstance().Country.toLowerCase());

                if (REUtils.isUserLoggedIn()&&isCertRequired) {
                    requestBuilder.addHeader(REConstants.KEY_AUTHORIZATION, REApplication.getInstance().getUserTokenDetails());
                }

                return chain.proceed(requestBuilder.build());
            }
        };
        CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(
                        "www.royalenfield.com",
                        "sha256/e228a1055f7ce63c9da8339b02395d6fbcc0963a"
                ) .add(
                        "www.royalenfield.com",
                        "sha256/8b3c5b9b867d4be46d1cb5a01d45d67dc8e94082"
                )
                .build();

        OkHttpClient.Builder mOKHttpClientBuilder = new OkHttpClient.Builder();
        if (isCertRequired) {
            try {
                mOKHttpClientBuilder.certificatePinner(certificatePinner);
              //  mOKHttpClientBuilder.sslSocketFactory(createSocketFactory(trustManager), trustManager);
            } catch (Exception e) {
                RELog.e(e);
            }
        }
        if (BuildConfig.DEBUG) {
            mOKHttpClientBuilder.addInterceptor(interceptor);
        }
        mOKHttpClientBuilder.addInterceptor(interceptorForHeader);
        mOKHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);
        mOKHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);
        List<Protocol> protocols = new ArrayList<>();
        protocols.add(Protocol.HTTP_1_1);
        mOKHttpClientBuilder.protocols(protocols);
        mOKHttpClient = mOKHttpClientBuilder.build();
        mOKHttpClient.retryOnConnectionFailure();
        return mOKHttpClient;
    }

    private static SSLSocketFactory createSocketFactory(X509TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException {
        // Create an SSLContext that uses our TrustManager
        SSLContext mSSLContext = SSLContext.getInstance("TLS");
        mSSLContext.init(null, new X509TrustManager[]{trustManager}, null);
        return mSSLContext.getSocketFactory();
    }

    private static X509TrustManager createTrustManager(Context context) throws GeneralSecurityException {
        InputStream mCertificateInputStream,mCertificateInputStream2;
        SSLContext mSSLContext;
        mCertificateInputStream = context.getResources().openRawResource(R.raw.digicertca); // This file includes three keys Root, Intermediate & royalenfield.com
        mCertificateInputStream2 = context.getResources().openRawResource(R.raw.royalenfield); // This file includes three keys Root, Intermediate & royalenfield.com
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(mCertificateInputStream);
        Collection<? extends Certificate> certificates2 = certificateFactory.generateCertificates(mCertificateInputStream2);

        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        if (certificates2.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }
        // Create a KeyStore containing our trusted CAs
        char[] pass = "password".toCharArray(); // Any password will work.
        KeyStore keyStore = newEmptyKeyStore(pass);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
//                Log.e("Certificates"," :" +certificate);
        }
        for (Certificate certificate : certificates2) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
//                Log.e("Certificates"," :" +certificate);
        }
        // Create a TrustManager that trusts the CAs in our KeyStore
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        mSSLContext = SSLContext.getInstance("TLS");
        mSSLContext.init(null, trustManagerFactory.getTrustManagers(), null);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }


    /**
     * Retruns SslConfiguration Used to pass to Navigation library
     *
     * @return SslConfiguration
     * @throws GeneralSecurityException
     */
    public static SslConfiguration createSslConfiguration() throws GeneralSecurityException {
        X509TrustManager trustManager = RESSLClient.createTrustManager(REApplication.getAppContext());
        return new SslConfiguration(RESSLClient.createSocketFactory(trustManager),
                trustManager);
    }

    /**
     * Creates an empty keystore
     *
     * @param password : password for keystore
     * @return : Keystore
     * @throws GeneralSecurityException :GeneralSecurityException
     */
    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password); // By convention, 'null' creates an empty key store.
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
