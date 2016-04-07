package ir.mbaas.push.service;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import ir.mbaas.push.helper.StaticMethods;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaSInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        try {
            String senderId = StaticMethods.getSenderId(getApplicationContext());
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            //send token to app server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
