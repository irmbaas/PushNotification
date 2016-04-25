package ir.mbaas.push;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import ir.mbaas.push.helper.PrefUtil;
import ir.mbaas.push.helper.StaticMethods;
import ir.mbaas.push.logic.GoogleLocation;
import ir.mbaas.push.logic.InstanceIdHelper;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class Push {

    public static GoogleLocation googleLocation;

    public static void initialize(Context ctx) {

        int count = PrefUtil.getInt(ctx, PrefUtil.APP_USE_COUNT) + 1;
        PrefUtil.putInt(ctx, PrefUtil.APP_USE_COUNT, count);

        InstanceIdHelper instanceIdHelper = new InstanceIdHelper(ctx);

        String senderId = StaticMethods.getSenderId(ctx);
        instanceIdHelper.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

        googleLocation = new GoogleLocation(ctx);
    }
}
