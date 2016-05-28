package ir.mbaas.sdk.mbaas;

import android.content.Context;

import org.json.JSONException;

import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.models.DeviceInfo;

/**
 * Created by Mahdi on 5/28/2016.
 */
public class UpdateInfo extends BaseAsyncRequest {

        private Context ctx;
        private DeviceInfo device;

        public UpdateInfo(Context ctx, DeviceInfo device) {
            this.ctx = ctx;
            this.device = device;
        }

        @Override
        protected void doSetup() throws ApiException, JSONException {
            callerName = "updateUserInfo";

            serviceName = AppConstants.GCM_SERVICE;
            endPoint = AppConstants.GCM_UPDATE_API;
            contentType = "application/json";
            verb = "POST";

            requestString = device.getUserInfoString();
            requestString += "&AppKey=" + StaticMethods.getAppKey(ctx);

            // need to include the API key and session token
            applicationApiKey = AppConstants.API_KEY;
        }

        @Override
        protected void processResponse(String response) throws ApiException, JSONException {
            int i = 2;
        }

        @Override
        protected void onCompletion(boolean success) {
            int j = 2;
        }
    }

