package de.tum.in.tca.ticketcheck.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import de.tum.in.tca.ticketcheck.BuildConfig;
import de.tum.in.tca.ticketcheck.utils.Utils;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static de.tum.in.tca.ticketcheck.utils.Const.API_HOSTNAME;


public final class Helper {
    private static final String TAG = "TUM_API_CALL";
    private static final int HTTP_TIMEOUT = 25000;
    private static OkHttpClient client;

    public static OkHttpClient getOkHttpClient(Context c) {
        if (client != null) {
            return client;
        }

        final CertificatePinner certificatePinner = new CertificatePinner.Builder()
                .add(API_HOSTNAME, "sha256/dVphPQ9xG7woPpEKXrNalw4eMUQ4Fw9r3OXTzxfuL5A=") //Fakultaet fuer Informatik
                .add(API_HOSTNAME, "sha256/SwdQoHL7SB/6o12XsIhbQJ9bANVnbrJoHTLzlu/qXT0=") //Technische Universitaet Muenchen
                .add(API_HOSTNAME, "sha256/VzL+FtAKvzb4N5igmFJyv83GD7CBK7Yyw+R6XdRRfmg=") //DFN-Verein PCA Global
                .add(API_HOSTNAME, "sha256/0d4q5hyN8vpiOWYWPUxz1GC/xCjldYW+a/65pWMj0bY=") //Deutsche Telekom Root CA 2
                .add(API_HOSTNAME, "sha256/YLh1dUR9y6Kja30RrAn7JKnbQG/uEtLMkBgFF2Fuihg=") //Let's Encrypt Authority X3
                .add(API_HOSTNAME, "sha256/Vjs8r4z+80wjNcr1YKepWQboSIRi63WsWXhIMN+eWys=") //LE Cross Sign: DST Root CA X3

                .build();

        //We want to persist our cookies through app session
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(c));

        //Start building the http client
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .certificatePinner(certificatePinner);

        //Add the device identifying header
        builder.addInterceptor(Helper.getDeviceInterceptor(c));

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new ChaosMonkeyInterceptor());
        }

        builder.addInterceptor(new ConnectivityInterceptor(c));

        builder.connectTimeout(Helper.HTTP_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(Helper.HTTP_TIMEOUT, TimeUnit.MILLISECONDS);

        builder.addNetworkInterceptor(new TumHttpLoggingInterceptor(Utils::log));

        //Save it to the static onResult and return
        client = builder.build();
        return client;
    }

    private static Interceptor getDeviceInterceptor(final Context c) {
        //Clearly identify all requests from this app
        final StringBuilder userAgent = new StringBuilder("TCA Client ");
        userAgent.append(Utils.getAppVersion(c));

        return chain -> {
            Utils.log("Fetching: " + chain.request()
                                          .url()
                                          .toString());
            Request.Builder newRequest = chain.request()
                                              .newBuilder()
                                              .addHeader("X-DEVICE-ID", AuthenticationManager.getDeviceID(c))
                                              .addHeader("User-Agent", userAgent.toString())
                                              .addHeader("X-ANDROID-VERSION", Build.VERSION.RELEASE);
            try {
                newRequest.addHeader("X-APP-VERSION", c.getPackageManager()
                                                       .getPackageInfo(c.getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) { //NOPMD
                //We don't care. In that case we simply don't send the information
            }

            return chain.proceed(newRequest.build());
        };
    }

}
