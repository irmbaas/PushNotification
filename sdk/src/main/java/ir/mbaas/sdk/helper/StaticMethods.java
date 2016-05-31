package ir.mbaas.sdk.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.R;
import ir.mbaas.sdk.mbaas.Delivery;
import ir.mbaas.sdk.models.DeviceInfo;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class StaticMethods {

    static private final String TAG = "StaticMethods";

    public static String getSenderId(Context ctx) {
        String senderId = "";
        Bundle bundle = getMetaData(ctx);

        if(bundle == null)
            return senderId;

        senderId = bundle.getString(ctx.getResources().getString(R.string.push_metadata));
        return senderId.substring(senderId.lastIndexOf("_") + 1);
    }

    public static String getAppKey(Context ctx) {
        String appKey = "";
        Bundle bundle = getMetaData(ctx);

        if(bundle == null)
            return appKey;

        appKey = bundle.getString(ctx.getResources().getString(R.string.push_metadata));
        return appKey.substring(0, appKey.lastIndexOf("_"));
    }

    public static Bundle getMetaData(Context ctx) {
        try {
            ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            return ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static DeviceInfo getDeviceInfo(Context ctx) {
        TelephonyManager tm = (TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = StaticMethods.getUniqueID(ctx, tm);
        String deviceName = android.os.Build.MODEL;
        String brand = android.os.Build.BRAND;
        String osVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        int userId = 0;

        return new DeviceInfo(IMEI, deviceName, brand, osVersion, sdkVersion, userId);
    }

    public static Bitmap downloadImage(String url) {
        try {
            URL imgUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            return null;
        }
    }

    public static int getIconResourceByMaterialName(Context context, String name) {
        if(name == null || name.isEmpty())
            return 0; //to prevent null pointer exception

        String mName = "ic_" + name.replaceAll("[- ]", "_"); //TODO @ak, check name to be not null
        return context.getResources().getIdentifier(mName, "drawable", context.getPackageName());
    }

    public static String getUniqueID(Context ctx, TelephonyManager tm){
        String myAndroidDeviceId = tm.getDeviceId();

        if (myAndroidDeviceId == null) {
            myAndroidDeviceId = Settings.Secure.getString(ctx.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

        return myAndroidDeviceId;
    }

    static public boolean isACRASenderServiceProcess(int pid) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream("/proc/" + pid + "/cmdline"));
            StringBuilder processName = new StringBuilder();
            int c;
            while ((c = reader.read()) > 0) {
                processName.append((char) c);
            }
            Log.v(TAG, "My process name is: " + processName.toString());
            return processName != null && processName.toString()
                    .endsWith(AppConstants.ACRA_PRIVATE_PROCESS_NAME);
        } catch (Exception e) {
            // ignore
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        return false;
    }

    static public boolean deliverPush(String sentId, String id) {
        if(sentId == null || sentId.isEmpty() || id == null || id.isEmpty())
            return false;

        Delivery delivery = new Delivery(MBaaS.context, sentId, id);
        delivery.execute();

        return true;
    }
}
