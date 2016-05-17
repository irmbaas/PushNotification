package ir.mbaas.sdk;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.GoogleLocation;
import ir.mbaas.sdk.logic.InstanceIdHelper;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaS {

    public static GoogleLocation googleLocation;
    public static Context context;

    public static void init(Application app) {

        if (StaticMethods.isACRASenderServiceProcess(android.os.Process.myPid())) {
            return;
        }

        MBaaS.context = app;

        int count = PrefUtil.getInt(app, PrefUtil.APP_USE_COUNT) + 1;
        PrefUtil.putInt(app, PrefUtil.APP_USE_COUNT, count);

        InstanceIdHelper instanceIdHelper = new InstanceIdHelper(app);

        String senderId = StaticMethods.getSenderId(app);
        instanceIdHelper.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        googleLocation = new GoogleLocation(app);
    }
}
