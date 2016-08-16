package ir.mbaas.sdk.apis;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.logic.NotificationBuilder;

/**
 * Created by Mehdi on 8/10/2016.
 */
public class DeletedPushNotifications extends BaseAsyncRequest {
    private String TAG = "DeletedPushNotifications";
    private String regId;
    private JSONObject jsonObject;

    public DeletedPushNotifications(String regId) {
        verb = "POST";
        this.regId = regId;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "getDeletedPushNotifications";

        serviceName = AppConstants.MBAAS_SERVICE;
        endPoint = AppConstants.GCM_DELETED_API;
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
            jsonObject = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCompletion(boolean success) {
        if (success && jsonObject != null && generateNotification()) {
            DeletedPushNotifications dpns = new DeletedPushNotifications(regId);
            dpns.execute();
        }
    }

    private boolean generateNotification() {
        Bundle data = null;

        try {
            JSONArray messages = jsonObject.getJSONArray("Messages");
            int length = messages.length();

            if(length == 0)
                return false;

            for(int idx = 0; idx < length; idx++) {
                data = getBundle(messages.getJSONObject(idx));

                String hiddenStr = data.getString(AppConstants.PN_IS_HIDDEN);
                boolean isHidden = hiddenStr != null && hiddenStr.equalsIgnoreCase("true");

                if (MBaaS.gcmMessageListener != null) {
                    MBaaS.gcmMessageListener.onMessageReceived(MBaaS.context, MBaaS.senderId, data);
                }

                if (!MBaaS.hideNotifications && !isHidden) {
                    NotificationBuilder nBuilder = new NotificationBuilder(MBaaS.context, data);
                    nBuilder.notifyPush();
                }

                Thread.sleep(500);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(data != null)
            PrefUtil.putString(MBaaS.context, PrefUtil.LAST_PUSH_RECEIVED, data.toString());

        return true;
    }

    public Bundle getBundle(JSONObject message) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString(AppConstants.PN_ID, message.getString(AppConstants.PN_ID));
            bundle.putString(AppConstants.PN_SENT_ID, message.getString(AppConstants.PN_SENT_ID));
            bundle.putString(AppConstants.PN_TITLE, message.getString(AppConstants.PN_TITLE));
            bundle.putString(AppConstants.PN_BODY, message.getString(AppConstants.PN_BODY));
            bundle.putString(AppConstants.PN_TICKER, message.getString(AppConstants.PN_TICKER));
            bundle.putString(AppConstants.PN_SUMMARY, message.getString(AppConstants.PN_SUMMARY));
            bundle.putString(AppConstants.PN_BUTTONS,
                    message.getString(AppConstants.PN_BUTTONS).toString());
            bundle.putString(AppConstants.PN_IMAGES,
                    message.getString(AppConstants.PN_IMAGES).toString());
            bundle.putString(AppConstants.PN_IS_HIDDEN,
                    message.getString(AppConstants.PN_IS_HIDDEN));
            bundle.putString(AppConstants.PN_IS_SILENT,
                    message.getString(AppConstants.PN_IS_SILENT));
            bundle.putString(AppConstants.PN_CUSTOM_DATA,
                    message.getString(AppConstants.PN_CUSTOM_DATA));
            bundle.putString(AppConstants.PN_ACTION_TYPE,
                    message.getString(AppConstants.PN_ACTION_TYPE));
            bundle.putString(AppConstants.PN_ACTION_URL,
                    message.getString(AppConstants.PN_ACTION_URL));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bundle;
    }
}
