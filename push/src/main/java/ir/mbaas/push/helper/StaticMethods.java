package ir.mbaas.push.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import ir.mbaas.push.R;
import ir.mbaas.push.model.DeviceInfo;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class StaticMethods {

    public static String getSenderId(Context ctx) {
        try {
            ApplicationInfo ai = null;
            ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String senderId = bundle.getString(ctx.getResources().getString(R.string.senderid_metadata));;
            return senderId.substring(senderId.lastIndexOf("_") + 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
