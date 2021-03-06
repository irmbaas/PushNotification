package ir.mbaas.sdk.service;

import android.os.AsyncTask;
import android.os.Build;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.apis.Registration;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaSInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        try {
            String senderId = StaticMethods.getSenderId(MBaaS.context);
            InstanceID instanceID = InstanceID.getInstance(MBaaS.context);
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            String localToken =
                    PrefUtil.getString(MBaaS.context, PrefUtil.REGISTRATION_ID);

            if(localToken.equals(token))
                return;

            PrefUtil.putString(MBaaS.context, PrefUtil.REGISTRATION_ID, token);
            Registration regApi = new Registration(MBaaS.context, token, MBaaS.device,
                    MBaaS.hasGooglePlayService);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                regApi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                regApi.execute();
            }
            //send token to app server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
