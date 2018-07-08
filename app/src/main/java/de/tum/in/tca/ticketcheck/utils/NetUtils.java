package de.tum.in.tca.ticketcheck.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.tum.in.tca.ticketcheck.api.Helper;
import okhttp3.OkHttpClient;

public class NetUtils {
    private final Context mContext;
    private final OkHttpClient client;

    public NetUtils(Context context) {
        //Manager caches all requests
        mContext = context;

        //Set our max wait time for each request
        client = Helper.getOkHttpClient(context);
    }
    /**
     * Check if a network connection is available or can be available soon
     *
     * @return true if available
     */
    public static boolean isConnected(Context con) {
        ConnectivityManager cm = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
