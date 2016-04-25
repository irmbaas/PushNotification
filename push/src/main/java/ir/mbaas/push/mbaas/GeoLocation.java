package ir.mbaas.push.mbaas;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.push.dfapi.ApiException;
import ir.mbaas.push.dfapi.BaseAsyncRequest;
import ir.mbaas.push.helper.AppConstants;
import ir.mbaas.push.helper.PrefUtil;

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
