package de.wichura.lks.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.wichura.lks.mainactivity.Constants;

import static de.wichura.lks.mainactivity.Constants.SHARED_PREFS_USER_INFO;

/**
 * Created by ich on 06.11.2016.
 * Camper App
 */

public class Utility {

    private Activity activity;

    public Utility (Activity activity) {
        this.activity = activity;
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    //TODO: cent nicht beachtet
    public static String getFormatedPrice(String price) {
        String [] buffer = price.split(".");
        return buffer[0]+"."+"00 "+ "€";

    }

    public String getUserToken() {
        return activity.getSharedPreferences(SHARED_PREFS_USER_INFO, 0).getString(Constants.USER_TOKEN, "");
    }
}
