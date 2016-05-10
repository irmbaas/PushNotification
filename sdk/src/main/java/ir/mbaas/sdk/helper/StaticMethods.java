package ir.mbaas.sdk.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.model.DeviceInfo;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class StaticMethods {

    public static Class<?> cls;

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
        String IMEI = tm.getDeviceId();
        String deviceName = android.os.Build.MODEL;
        String brand = android.os.Build.BRAND;
        String osVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        int userId = 0;

        return new DeviceInfo(IMEI, deviceName, brand, osVersion, sdkVersion, userId);
    }
}
