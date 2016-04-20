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
 * Created by Mahdi on 4/20/2016.
 */
public class Delivery extends BaseAsyncRequest {
    private Context ctx;
    private String pushSendId;

    public Delivery(Context ctx, String pushSendId) {
        this.ctx = ctx;
        this.pushSendId = pushSendId;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "pushDelivery";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_DELIVER_API;
        contentType = "";
        verb = "POST";

        requestBody = new JSONObject();
        requestBody.put("pushSendId", pushSendId);

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
            int i = 3;
        }
    }
}
