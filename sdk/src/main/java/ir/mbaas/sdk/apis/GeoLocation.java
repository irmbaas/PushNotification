package ir.mbaas.sdk.apis;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.PrefUtil;

/**
 * Created by Mahdi on 4/25/2016.
 */
public class GeoLocation extends BaseAsyncRequest {

    private String TAG = "GeoLocation";
    private Context ctx;
    private String geoLocations;

    public GeoLocation(Context ctx, String geoLocations) {
        this.ctx = ctx;
        this.geoLocations = geoLocations;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "sendLocations";

        serviceName = AppConstants.MBAAS_SERVICE;
        endPoint = AppConstants.GCM_LOCATIONS_API;
        contentType = "";
        verb = "POST";

        requestString = "GeoLocations={ \"locations\": [" + geoLocations + "]}";
    }

    @Override
    protected void processResponse(String response) throws ApiException, JSONException {
        Log.d(TAG, "Response Received.");
    }

    @Override
    protected void onCompletion(boolean success) {
        if(success){
            PrefUtil.putString(ctx, PrefUtil.GEO_LOCATIONS, "");
        }
    }
}
