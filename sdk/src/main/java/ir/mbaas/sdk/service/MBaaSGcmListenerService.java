package ir.mbaas.sdk.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.NotificationBuilder;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.mbaas.Delivery;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaSGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        PrefUtil.putString(this, PrefUtil.LAST_PUSH_RECEIVED, data.toString());
        String hiddenStr = data.getString(AppConstants.PN_IS_HIDDEN);
        boolean isHidden = hiddenStr != null && hiddenStr.equalsIgnoreCase("true");

        if (MBaaS.gcmMessageListener != null) {
            String sentId  = data.getString(AppConstants.PN_SENT_ID);
            String id      = data.getString(AppConstants.PN_ID);
            StaticMethods.deliverPush(sentId, id);

            MBaaS.gcmMessageListener.onMessageReceived(MBaaS.context, from, data);

            if (MBaaS.hideNotifications || isHidden)
                return;
        }

        NotificationBuilder nBuilder = new NotificationBuilder(MBaaS.context, data);
        nBuilder.notifyPushAndDeliver();
    }
}
