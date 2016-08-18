package ir.mbaas.sdk.apis;

import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;

/**
 * Created by Mehdi on 8/10/2016.
 */
public class DeletedPushNotifications extends BaseAsyncRequest {
    private String TAG = "DeletedPushNotifications";
    private String regId;
    private String response;

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
        StaticMethods.sendException(e, Log.VERBOSE);
    }

    @Override
    protected void processResponse(String response) {
        this.response = response;
    }

    @Override
    protected void onCompletion(boolean success) {
        if (success && generateBulkNotifications()) {
            DeletedPushNotifications dpns = new DeletedPushNotifications(regId);
            dpns.execute();
        }
    }

    private boolean generateBulkNotifications() {
        try {
            if(response == null || response.isEmpty())
                return false;

            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject == null)
                return false;

            JSONArray messages = jsonObject.getJSONArray("Messages");

            if (messages == null || messages.length() == 0)
                return false;

            if (MBaaS.gcmMessageListener != null) {
                MBaaS.gcmMessageListener.onDeletedMessagesReceived(
                        MBaaS.context, MBaaS.senderId, messages);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            StaticMethods.sendException(e, Log.VERBOSE);
        } catch (Exception e) {
            e.printStackTrace();
            StaticMethods.sendException(e, Log.VERBOSE);
        }

        return true;
    }
}
