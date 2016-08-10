package ir.mbaas.sdk.apis;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.ApiInvoker;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.logic.JSONNotificationBuilder;
import ir.mbaas.sdk.logic.NotificationBuilder;
import ir.mbaas.sdk.models.NotificationContent;
import ir.mbaas.sdk.models.NotificationContents;

/**
 * Created by Mehdi on 8/10/2016.
 */
public class DeletedPushNotifications extends BaseAsyncRequest {
    private String TAG = "DeletedPushNotifications";
    private String regId;
    private NotificationContents notificationContents;

    public DeletedPushNotifications(String regId) {
        verb = "POST";
        this.regId = regId;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "getDeletedPushNotifications";

        serviceName = AppConstants.MBAAS_SERVICE;
        endPoint = AppConstants.GCM_UPDATE_API;
        verb = "POST";

        requestBody = new JSONObject();
        requestBody.put("IMEI", MBaaS.device.getIMEI());
        requestBody.put("AppKey", MBaaS.appKey);
        requestBody.put("RegId", regId);
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
    }

    @Override
    protected void processResponse(String response) {
        try {
            notificationContents = (NotificationContents)
                    ApiInvoker.deserialize(response, "", NotificationContents.class);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCompletion(boolean success) {
        if (success && notificationContents != null && notificationContents.records != null &&
                notificationContents.records.size() > 0) {
            generateNotification();
            DeletedPushNotifications dpns = new DeletedPushNotifications(regId);
            dpns.execute();
        }
    }

    private void generateNotification() {
        //PrefUtil.putString(this, PrefUtil.LAST_PUSH_RECEIVED, data.toString());
        for (NotificationContent nc : notificationContents.records) {

            if (MBaaS.gcmMessageListener != null) {

                //MBaaS.gcmMessageListener.onMessageReceived(MBaaS.context, from, data);

                if (MBaaS.hideNotifications || nc.isHidden) {
                    StaticMethods.deliverPush(nc.pushSentId, nc.id);
                    return;
                }
            }

            JSONNotificationBuilder nBuilder = new JSONNotificationBuilder(MBaaS.context, nc);
            nBuilder.notifyPushAndDeliver();
        }
    }
}
