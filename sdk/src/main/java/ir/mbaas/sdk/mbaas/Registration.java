package ir.mbaas.sdk.mbaas;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import java.util.Date;
import java.util.Random;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.R;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.IdGenerator;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.logic.PushActions;
import ir.mbaas.sdk.logic.UpdateActions;
import ir.mbaas.sdk.models.DeviceInfo;
import ir.mbaas.sdk.models.MBaaSRegistrationResponse;

/**
 * Created by Mahdi on 4/10/2016.
 */
public class Registration extends BaseAsyncRequest {

    private Context context;
    private String regId;
    private DeviceInfo device;
    private boolean hasGooglePlayService;
    private MBaaSRegistrationResponse mBaaSResponse;

    public Registration(Context context, String regId, DeviceInfo device,
                        boolean hasGooglePlayService) {
        this.context = context;
        this.regId = regId;
        this.device = device;
        this.hasGooglePlayService = hasGooglePlayService;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "registerRegID";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_REGISTER_API;
        contentType = "application/json";
        verb = "POST";

        requestString  = device.getDeviceInfoString();
        requestString += "&GCMRegId=" + regId;
        requestString += "&AppKey=" + MBaaS.appKey;
        requestString += "&AppVersion=" + MBaaS.versionCode;
        requestString += "&UseCount=" + PrefUtil.getInt(context, PrefUtil.APP_USE_COUNT);
        requestString += "&HasGooglePlayService=" + String.valueOf(hasGooglePlayService);
        /*requestString += "&GeoLocation=" + TODO;*/

        // need to include the API key and session token
        applicationApiKey = AppConstants.API_KEY;
        //sessionToken = PrefUtil.getString(context, PrefUtil.SESSION_TOKEN);
    }

    @Override
    protected void processResponse(String response) {
        try {
            mBaaSResponse = (MBaaSRegistrationResponse)
                    ApiInvoker.deserialize(response, "", MBaaSRegistrationResponse.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCompletion(boolean success) {
        if (success) {
            PrefUtil.putInt(context, PrefUtil.APP_USE_COUNT, 0);
            if(MBaaS.gcmRegistrationListener != null)
                MBaaS.gcmRegistrationListener.successRegistrationOnMBaaS(MBaaS.context, regId);

            if (MBaaS.mBaaSListener != null) {
                MBaaS.mBaaSListener.successRegistration(MBaaS.context, regId);

                if (mBaaSResponse.appVersion != null)
                    MBaaS.mBaaSListener.versionInfoAvailable(MBaaS.context, mBaaSResponse.appVersion);
            } else {
                showNewVersionNotification();
            }
        } else {
            if (MBaaS.gcmRegistrationListener != null)
                MBaaS.gcmRegistrationListener.failedRegistrationOnMBaaS(MBaaS.context, regId);

            if (MBaaS.mBaaSListener != null) {
                MBaaS.mBaaSListener.failedRegistration(MBaaS.context, regId);
            }
        }
    }

    private boolean showNewVersionNotification() {
        if (mBaaSResponse.appVersion == null || !mBaaSResponse.appVersion.autoUpdate)
            return false;

        Date now = new Date();
        long updateTime = PrefUtil.getLong(context, PrefUtil.NEXT_UPDATE_TIME, now.getTime());

        if(now.getTime() < updateTime)
            return false;

        String title   = context.getResources().getString(R.string.app_update_title);
        String ticker  = title;
        String message = context.getResources().getString(R.string.app_update_text);
        String update  = context.getResources().getString(R.string.app_update_btn);
        String later   = context.getResources().getString(R.string.app_later_btn);
        int notificationId = IdGenerator.generateIntegerId();

        // Update Button Intent
        Intent uIntent = UpdateActions.createUpdateButtonAction(UpdateActions.UpdateActionType.Now,
                mBaaSResponse.appVersion, notificationId);

        PendingIntent uMainIntent = PendingIntent.getBroadcast(context,
                IdGenerator.generateIntegerId(), uIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Later Button Intent
        Intent lIntent = UpdateActions.createUpdateButtonAction(UpdateActions.UpdateActionType.Later,
                mBaaSResponse.appVersion, notificationId);

        PendingIntent lMinIntent = PendingIntent.getBroadcast(context,
                IdGenerator.generateIntegerId(), lIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Creating Notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MBaaS.context)
                // Set Icon
                .setSmallIcon(context.getApplicationInfo().icon)
                // Set Ticker Message
                .setTicker(ticker)
                // Set Title
                .setContentTitle(title)
                // Set Text
                .setContentText(message)
                // Set SubText
                //.setSubText("")
                // Set PendingIntent into Notification
                .setContentIntent(uMainIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        builder.addAction(R.drawable.ic_history, later, lMinIntent);
        builder.addAction(R.drawable.ic_file_download, update, uMainIntent);

        // Create Notification Manager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId, builder.build());

        return true;
    }
}
