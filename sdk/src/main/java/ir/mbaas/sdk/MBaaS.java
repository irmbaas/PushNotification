package ir.mbaas.sdk;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ir.mbaas.sdk.exceptions.MBaaSNotInitializedException;
import ir.mbaas.sdk.helper.GooglePlayServices;
import ir.mbaas.sdk.listeners.GcmMessageListener;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.listeners.GcmRegistrationListener;
import ir.mbaas.sdk.listeners.MBaaSListener;
import ir.mbaas.sdk.logic.InstanceIdHelper;
import ir.mbaas.sdk.apis.Registration;
import ir.mbaas.sdk.apis.UpdateInfo;
import ir.mbaas.sdk.models.DeviceInfo;
import ir.mbaas.sdk.models.User;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaS {

    public static DeviceInfo device;
    public static String appKey;
    public static Context context;
    public static MBaaSListener mBaaSListener;
    public static GcmMessageListener gcmMessageListener;
    public static GcmRegistrationListener gcmRegistrationListener;
    public static int versionCode = 1;
    public static boolean hasGooglePlayService = false;
    public static boolean hideNotifications = false;

    private static MBaaS _instance;

    public MBaaS(Application app) {
        MBaaS.context = app;

        int count = PrefUtil.getInt(app, PrefUtil.APP_USE_COUNT) + 1;
        PrefUtil.putInt(app, PrefUtil.APP_USE_COUNT, count);

        device = StaticMethods.getDeviceInfo(app);
        appKey = StaticMethods.getAppKey(app);

        try {
            versionCode = app.getPackageManager().getPackageInfo(app.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void init(Application app) {
        MBaaS.init(app, null, null, null, false);
    }

    public static void init(Application app, MBaaSListener mBaaSListener) {
        MBaaS.init(app, null, null, mBaaSListener, false);
    }

    public static void init(Application app, GcmMessageListener gcmMessageListener,
                            boolean hideNotifications) {
        init(app, gcmMessageListener, null, null, hideNotifications);
    }

    public static void init(Application app, GcmRegistrationListener gcmRegistrationListener) {
        init(app, null, gcmRegistrationListener, null, false);
    }

    public static void init(Application app,
                            GcmMessageListener gcmMessageListener,
                            GcmRegistrationListener gcmRegistrationListener,
                            MBaaSListener mBaaSListener,
                            boolean hideNotifications) {

        if (StaticMethods.isACRASenderServiceProcess(android.os.Process.myPid())) {
            return;
        }

        MBaaS.gcmMessageListener = gcmMessageListener;
        MBaaS.gcmRegistrationListener = gcmRegistrationListener;
        MBaaS.hideNotifications = hideNotifications;
        MBaaS.mBaaSListener = mBaaSListener;

        if (_instance == null) {
            _instance = new MBaaS(app);

            try {
                register();
            } catch (MBaaSNotInitializedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void register() throws MBaaSNotInitializedException {

        if(_instance == null)
            throw new MBaaSNotInitializedException();

        hasGooglePlayService = GooglePlayServices.checkGooglePlayServiceAvailability(MBaaS.context);

        if (hasGooglePlayService) {
            InstanceIdHelper instanceIdHelper = new InstanceIdHelper(MBaaS.context);

            String senderId = StaticMethods.getSenderId(MBaaS.context);
            instanceIdHelper.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } else {
            if (MBaaS.gcmRegistrationListener != null) {
                MBaaS.gcmRegistrationListener.onGooglePlayServiceUnavailable(MBaaS.context);
            }

            Registration regApi = new Registration(MBaaS.context, "", MBaaS.device,
                    hasGooglePlayService);
            regApi.execute();
        }
    }

    public static void updateInfo(User user) throws MBaaSNotInitializedException {

        if(_instance == null)
            throw new MBaaSNotInitializedException();

        UpdateInfo uInfo = new UpdateInfo(context, device, user);
        uInfo.execute();
    }
}
