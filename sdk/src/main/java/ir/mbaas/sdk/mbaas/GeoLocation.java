package ir.mbaas.sdk.mbaas;

import android.content.Context;

import org.json.JSONException;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.PrefUtil;

/**
 * Created by Mahdi on 4/25/2016.
 */
public class GeoLocation extends BaseAsyncRequest {
    private Context ctx;
    private String geoLocations;

    public GeoLocation(Context ctx, String geoLocations) {
        this.ctx = ctx;
        this.geoLocations = geoLocations;
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "sendLocations";

        serviceName = AppConstants.GCM_SERVICE;
        endPoint = AppConstants.GCM_LOCATIONS_API;
        contentType = "";
        verb = "POST";

        requestString = "GeoLocations={ \"locations\": [" + geoLocations + "]}";

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
            PrefUtil.putString(ctx, PrefUtil.GEO_LOCATIONS, "");
        }
    }
}
