package ir.mbaas.sdk.mbaas;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;

/**
 * Created by Mahdi on 4/20/2016.
 */
public class Delivery extends BaseAsyncRequest {

    private String TAG = "Delivery";
    private Context ctx;
    private String PushSentId;
    private String guid;

    public Delivery(Context ctx, String PushSentId, String guid) {
        this.ctx = ctx;
        this.PushSentId = PushSentId;
        this.guid = guid;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "pushDelivery";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_DELIVER_API;
        contentType = "";
        verb = "POST";

        requestString  = "PushSentId=" + PushSentId;
        requestString += "&Id=" + guid;

        // need to include the API key and session token
        applicationApiKey = AppConstants.API_KEY;
        //sessionToken = PrefUtil.getString(ctx, PrefUtil.SESSION_TOKEN);
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        Log.d(TAG, "Response Received.");
    }

    @Override
    protected void onCompletion(boolean success) {
        if(success){
            Log.d(TAG, "Successful completion.");
        }
    }
}
