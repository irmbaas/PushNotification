package ir.mbaas.push.mbaas;

import android.content.Context;

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

    public Registration(Context ctx, String regId) {
        this.ctx = ctx;
        this.regId = regId;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "registerRegID";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_REGISTER_API;
        contentType = "";
        verb = "POST";

        DeviceInfo devInfo = StaticMethods.getDeviceInfo(ctx);

        requestBody = new JSONObject();
        requestBody.put("regid", regId);
        requestBody.put("IMEI", devInfo.getIMEI());

        // need to include the API key and session token
        applicationApiKey = AppConstants.API_KEY;
        sessionToken = PrefUtil.getString(ctx, PrefUtil.SESSION_TOKEN);
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        int i = 2;
    }

    @Override
    protected void onCompletion(boolean success) {
        if(success){
            CustomDialogs.Toast(ctx, R.string.successfull_registration);
        } else {
            CustomDialogs.Toast(ctx, R.string.registration_failed);
        }
    }
}
