package ir.mbaas.sdk.apis;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
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

        serviceName = AppConstants.MBAAS_SERVICE;
        endPoint = AppConstants.GCM_UPDATE_API;
        verb = "POST";

        requestBody = user.toJSON();
        requestBody.put("IMEI", device.getIMEI());
        requestBody.put("AppKey", MBaaS.appKey);
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

