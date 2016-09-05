package ir.mbaas.sdk.helper;

import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
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
import ir.mbaas.sdk.apis.Delivery;
import ir.mbaas.sdk.models.DeviceInfo;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class StaticMethods {

    static private final String TAG = "StaticMethods";

    public static String getSenderId(Context ctx) {
        try {
            Bundle bundle = getMetaData(ctx);

            String senderId = bundle.getString(ctx.getResources().getString(R.string.push_metadata));
            return senderId.substring(senderId.lastIndexOf("_") + 1);
        } catch (Exception exc) {
            throw new RuntimeException("No MBaaS token is provided," +
                    " please check AndroidManifest.xml.");
        }
    }

    public static String getAppKey(Context ctx) {
        try {
            Bundle bundle = getMetaData(ctx);

            String appKey = bundle.getString(ctx.getResources().getString(R.string.push_metadata));
            return appKey.substring(0, appKey.lastIndexOf("_"));
        } catch (Exception exc) {
            throw new RuntimeException("No MBaaS token is provided," +
                    " please check AndroidManifest.xml.");
        }
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
        String softIMEI = getSoftIMEI(ctx);
        String IMEI = StaticMethods.getIMEI(ctx, softIMEI);
        String deviceName = android.os.Build.MODEL;
        String brand = android.os.Build.BRAND;
        String osVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        int userId = 0;

        return new DeviceInfo(IMEI, deviceName, brand, osVersion, sdkVersion, userId, softIMEI);
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

    public static String getIMEI(Context ctx, String softIMEI) {
        String imei = PrefUtil.getString(ctx, PrefUtil.DEVICE_IMEI);

        if (imei != null && !imei.isEmpty() && !imei.equalsIgnoreCase(softIMEI))
            return imei;

        if (PermissionChecker.hasReadPhoneStatePermission(ctx)) {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        }

        if (imei == null || imei.isEmpty()) {
            imei = softIMEI;
        }

        PrefUtil.putString(ctx, PrefUtil.DEVICE_IMEI, imei);

        return imei;
    }

    public static String getSoftIMEI(Context ctx) {
        String softIMEI = PrefUtil.getString(ctx, PrefUtil.DEVICE_SOFT_IMEI);

        if (softIMEI != null && !softIMEI.isEmpty())
            return softIMEI;

        softIMEI = new DeviceUuidFactory(ctx).getDeviceUuid().toString();

        PrefUtil.putString(ctx, PrefUtil.DEVICE_SOFT_IMEI, softIMEI);

        return softIMEI;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            delivery.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            delivery.execute();
        }

        return true;
    }

    public static void createNotification(Context ctx, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx);
        builder.setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(ctx.getApplicationInfo().icon)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager)
                ctx.getSystemService(ctx.NOTIFICATION_SERVICE);

        notificationManager.notify(IdGenerator.generateIntegerId(), builder.build());
    }

    public static synchronized String handleUndeliveredPushes(Context ctx,
                                                              AppConstants.FailedDeliveriesStatus fds,
                                                              String customStr) {
        String separator = ",";
        String undeliveredPushes = PrefUtil.getString(ctx, PrefUtil.UNDELIVERED_PUSHES);

        switch (fds) {
            case DELETE:
                if (undeliveredPushes.equalsIgnoreCase(customStr)) {
                    undeliveredPushes = "";
                } else {
                    undeliveredPushes = undeliveredPushes.replace(customStr, "");
                    undeliveredPushes = undeliveredPushes.startsWith(",") ?
                            undeliveredPushes.substring(1) : undeliveredPushes;
                }

                PrefUtil.putString(ctx, PrefUtil.UNDELIVERED_PUSHES, undeliveredPushes);
                return undeliveredPushes;
            case ADD:
                if(undeliveredPushes.length() > 2048)
                    undeliveredPushes = "";

                undeliveredPushes += undeliveredPushes.isEmpty() ? customStr : separator + customStr;
                PrefUtil.putString(ctx, PrefUtil.UNDELIVERED_PUSHES, undeliveredPushes);
                return undeliveredPushes;
            case GET:
                return undeliveredPushes;
        }

        return undeliveredPushes;
    }

    public static void sendException(Exception exc, int priority) {
        if (MBaaS.mBaaSListener != null) {
            MBaaS.mBaaSListener.exception(MBaaS.context, exc, priority);
        }
    }
}
