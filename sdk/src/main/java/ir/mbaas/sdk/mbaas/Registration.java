package ir.mbaas.sdk.mbaas;

import android.content.Context;

import org.json.JSONException;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.R;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.CustomDialogs;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.models.DeviceInfo;

/**
 * Created by Mahdi on 4/10/2016.
 */
public class Registration extends BaseAsyncRequest {

    private Context ctx;
    private String regId;
    private String appKey;
    private DeviceInfo device;

    public Registration(Context ctx, String regId, DeviceInfo device) {
        this.ctx = ctx;
        this.regId = regId;
        this.appKey = StaticMethods.getAppKey(ctx);
        this.device = device;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "registerRegID";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_REGISTER_API;
        contentType = "application/json";
        verb = "POST";

        /*requestBody = devInfo.getDeviceInfoJson();
        requestBody.put("GCMRegId", regId);
        requestBody.put("AppKey", appKey);
        requestBody.put("GeoLocation", "TODO");*/
        requestString  = device.getDeviceInfoString();
        requestString += "&GCMRegId=" + regId;
        requestString += "&AppKey=" + appKey;
        requestString += "&AppVersion=" + MBaaS.versionCode;
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
