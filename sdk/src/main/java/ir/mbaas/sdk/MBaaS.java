package ir.mbaas.sdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ir.mbaas.sdk.helper.GcmMessageListener;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.GoogleLocation;
import ir.mbaas.sdk.logic.InstanceIdHelper;
import ir.mbaas.sdk.mbaas.UpdateInfo;
import ir.mbaas.sdk.models.DeviceInfo;
import ir.mbaas.sdk.models.User;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaS {

    public static GoogleLocation googleLocation;
    public static DeviceInfo device;
    public static String appKey;
    public static Context context;
    public static GcmMessageListener gcmMessageListener;
    public static int versionCode = 1;

    private static MBaaS _instance;

    public MBaaS(Application app) {
        MBaaS.context = app;

        int count = PrefUtil.getInt(app, PrefUtil.APP_USE_COUNT) + 1;
        PrefUtil.putInt(app, PrefUtil.APP_USE_COUNT, count);

        InstanceIdHelper instanceIdHelper = new InstanceIdHelper(app);

        String senderId = StaticMethods.getSenderId(app);
        instanceIdHelper.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        device = StaticMethods.getDeviceInfo(app);
        appKey = StaticMethods.getAppKey(app);

        try {
            versionCode = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        googleLocation = new GoogleLocation(app);
    }

    public static void init(Application app) {
        MBaaS.init(app, null);
    }

    public static void init(Application app, GcmMessageListener gcmMessageListener) {

        if (StaticMethods.isACRASenderServiceProcess(android.os.Process.myPid())) {
            return;
        }

        MBaaS.gcmMessageListener = gcmMessageListener;

        if (_instance == null) {
            _instance = new MBaaS(app);
        }
    }

    public static void updateInfo(User user) {
        UpdateInfo uInfo = new UpdateInfo(context, device, user);
        uInfo.execute();
    }
}
