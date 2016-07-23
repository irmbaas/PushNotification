package ir.mbaas.sdk.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import ir.mbaas.sdk.R;

public class GooglePlayServices {

    private static String TAG = "GooglePlayServices";

    private static int googlePlayServicesVersionCode;
    private static String googlePlayServicesVersionName;

    /***
     * get google play services version code
     *
     * @param context app context
     * @return version code
     */
    public static int getGooglePlayServicesVersionCode(Context context) {
        if (googlePlayServicesVersionCode != -1) {
            try {
                googlePlayServicesVersionCode =
                        context.getPackageManager().getPackageInfo("com.google.android.gms", 0)
                                .versionCode;
            } catch (PackageManager.NameNotFoundException exc) {
                Log.e(TAG, exc.getMessage());
            }
        }
        return googlePlayServicesVersionCode;
    }

    /***
     * get google play services version name
     *
     * @param context app context
     * @return version name
     */
    public static String getGooglePlayServicesVersionName(Context context) {
        if (googlePlayServicesVersionName == null) {
            try {
                googlePlayServicesVersionName =
                        context.getPackageManager().getPackageInfo("com.google.android.gms", 0)
                                .versionName;
            } catch (PackageManager.NameNotFoundException exc) {
                Log.e(TAG, exc.getMessage());
            }
        }
        return googlePlayServicesVersionName;
    }

    /***
     * Check is client's device support networkManager
     *
     * @param context app context
     * @return boolean
     */
    public static boolean isGcmNetworkManagerSupported(Context context) {
        return getGooglePlayServicesVersionCode(context) >= 7500000;
    }

    /***
     * Check google play services is installed or updated
     *
     * @param context    app context
     * @return
     */

    public static boolean checkGooglePlayServiceAvailability(final Context context) {

        try {
            int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo("com.google.android.gms", 0);
            if (ai.enabled) {
                if ((status == ConnectionResult.SUCCESS) ||
                        (status == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) ||
                        (status == ConnectionResult.SERVICE_UPDATING)) {
                    return true;
                }
            }
        } catch (Exception exc) {
            Log.e(TAG, exc.getMessage());
        }

        return false;
    }
}
