package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mehdi on 6/18/2016.
 */
public class MBaaSRegistrationResponse {

    @JsonProperty("AppVersion")
    public MBaaSAppVersion appVersion;
}
