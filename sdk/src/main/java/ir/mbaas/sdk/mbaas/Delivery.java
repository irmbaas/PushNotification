package ir.mbaas.sdk.mbaas;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;

/**
 * Created by Mahdi on 4/20/2016.
 */
public class Delivery extends BaseAsyncRequest {

    private String TAG = "Delivery";
    private Context ctx;
    private String PushSentId;
    private String guid;
    private String undelivered;
    private String failedDeliveries;

    public Delivery(Context ctx, String PushSentId, String guid) {
        this.ctx = ctx;
        this.PushSentId = PushSentId;
        this.guid = guid;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "pushDelivery";

        baseInstanceUrl = AppConstants.DIRECT_URL;
        serviceName = AppConstants.DIRECT_SERVICE;
        endPoint = AppConstants.GCM_DELIVER_API;
        contentType = "application/json";
        verb = "POST";

        undelivered = String.format(AppConstants.DELIVERY_JSON_FORMAT, PushSentId, guid);

        failedDeliveries = StaticMethods.handleUndeliveredPushes(ctx,
                AppConstants.FailedDeliveriesStatus.GET, "");

        if (failedDeliveries == null || failedDeliveries.isEmpty()) {
            requestBody = new JSONObject();
            requestBody.put("PushSentId", PushSentId);
            requestBody.put("Id", guid);
            return;
        }

        endPoint = AppConstants.GCM_BULK_DELIVER_API;
        requestString = "[" + failedDeliveries + "," + undelivered + "]";
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        Log.d(TAG, "Response Received.");
    }

    @Override
    protected void onCompletion(boolean success) {
        if (success) {
            StaticMethods.handleUndeliveredPushes(ctx, AppConstants.FailedDeliveriesStatus.DELETE,
                    failedDeliveries);
            Log.d(TAG, "Successful completion.");
        } else {
            StaticMethods.handleUndeliveredPushes(ctx,  AppConstants.FailedDeliveriesStatus.ADD,
                    undelivered);
        }
    }
}
