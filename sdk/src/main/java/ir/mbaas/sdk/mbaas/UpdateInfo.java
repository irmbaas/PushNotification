package ir.mbaas.sdk.mbaas;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.models.DeviceInfo;
import ir.mbaas.sdk.models.User;

/**
 * Created by Mahdi on 5/28/2016.
 */
public class UpdateInfo extends BaseAsyncRequest {

    private String TAG = "UpdateInfo";
    private Context ctx;
    private DeviceInfo device;
    private User user;

    public UpdateInfo(Context ctx, DeviceInfo device, User user) {
        this.ctx  = ctx;
        this.user = user;
        this.device = device;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "updateUserInfo";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_UPDATE_API;
        contentType = "application/json";
        verb = "POST";

        requestString  = user.toString();
        requestString += "&IMEI=" + device.getIMEI();
        requestString += "&AppKey=" + MBaaS.appKey;

        // need to include the API key and session token
        applicationApiKey = AppConstants.API_KEY;
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        Log.d(TAG, "Response Received.");
    }

    @Override
    protected void onCompletion(boolean success) {
        Log.d(TAG, "The request is completed.");
    }
}

