package ir.mbaas.push.mbaas;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.push.R;
import ir.mbaas.push.dfapi.ApiException;
import ir.mbaas.push.dfapi.BaseAsyncRequest;
import ir.mbaas.push.helper.AppConstants;
import ir.mbaas.push.helper.CustomDialogs;
import ir.mbaas.push.helper.PrefUtil;
import ir.mbaas.push.helper.StaticMethods;
import ir.mbaas.push.model.DeviceInfo;

/**
 * Created by Mahdi on 4/10/2016.
 */
public class Registration extends BaseAsyncRequest {

    private Context ctx;
    private String regId;
    private String appKey;

    public Registration(Context ctx, String regId) {
        this.ctx = ctx;
        this.regId = regId;
        this.appKey = StaticMethods.getAppKey(ctx);
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "registerRegID";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_REGISTER_API;
        contentType = "application/json";
        verb = "POST";

        DeviceInfo devInfo = StaticMethods.getDeviceInfo(ctx);

        /*requestBody = devInfo.getDeviceInfoJson();
        requestBody.put("GCMRegId", regId);
        requestBody.put("AppKey", appKey);
        requestBody.put("GeoLocation", "TODO");*/
        requestString = devInfo.getDeviceInfoString();
        requestString += "&GCMRegId=" + regId;
        requestString += "&AppKey=" + appKey;
        requestString += "&UseCount=" + PrefUtil.getInt(ctx, PrefUtil.APP_USE_COUNT);

        // need to include the API key and session token
        applicationApiKey = AppConstants.API_KEY;
        //sessionToken = PrefUtil.getString(ctx, PrefUtil.SESSION_TOKEN);
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        int i = 2;
    }

    @Override
    protected void onCompletion(boolean success) {
        if(success){
            CustomDialogs.Toast(ctx, R.string.successfull_registration);
            PrefUtil.putInt(ctx, PrefUtil.APP_USE_COUNT, 0);
        } else {
            CustomDialogs.Toast(ctx, R.string.registration_failed);
        }
    }
}
