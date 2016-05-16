package ir.mbaas.sdk.service;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.logic.NotificationBuilder;
import ir.mbaas.sdk.helper.PrefUtil;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaSGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        PrefUtil.putString(this, PrefUtil.LAST_PUSH_RECEIVED, data.toString());

        NotificationBuilder nBuilder = new NotificationBuilder(MBaaS.context, data);
        nBuilder.notifyPushAndDeliver();
    }
}
