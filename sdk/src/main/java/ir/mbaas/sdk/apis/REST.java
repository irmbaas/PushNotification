package ir.mbaas.sdk.apis;

import org.json.JSONException;
import org.json.JSONObject;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.dfapi.ApiException;
import ir.mbaas.sdk.dfapi.BaseAsyncRequest;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.listeners.IRestCallback;

/**
 * Created by Mehdi on 8/1/2016.
 */
public class REST extends BaseAsyncRequest {

    private String TAG = "REST";
    private JSONObject jsonObject;
    private String url;
    private IRestCallback irc;

    private Exception exception;
    private String response;

    public REST(String url, JSONObject jsonObject, IRestCallback irc) {
        this.jsonObject = jsonObject;
        this.url = url;
        this.irc = irc;
        verb = "POST";
    }

    @Override
    protected void doSetup() throws ApiException, JSONException {
        callerName = "callRestAPI";

        if(url == null)
            throw new RuntimeException ("REST URL must not be null.");

        if (url.startsWith(AppConstants.MBAAS_BASE_URL)) {
            baseInstanceUrl = url;
        } else {
            baseInstanceUrl = AppConstants.MBAAS_BASE_URL + url;
        }

        requestBody = jsonObject == null ? new JSONObject() : jsonObject;
        requestBody.put("IMEI", MBaaS.device.getIMEI());
        requestBody.put("AppKey", MBaaS.appKey);
    }

    @Override
    protected void onError(Exception e) {
        super.onError(e);
        this.exception = e;
    }

    @Override
    protected void processResponse(String response) {
       this.response = response;
    }

    @Override
    protected void onCompletion(boolean success) {
        if (irc == null)
            return;

        if(success)
            irc.onSuccess(response);
        else
            irc.onError(exception);
    }
}
