package ir.mbaas.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Mehdi on 6/18/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MBaaSAppVersion {

    @JsonProperty("AutoUpdate")
    public boolean autoUpdate;

    @JsonProperty("VersionName")
    public String versionName;

    @JsonProperty("VersionNumber")
    public int versionCode;

    @JsonProperty("DownloadLink")
    public String downloadUrl;

    @JsonProperty("IconLink")
    public String iconUrl;

    @JsonProperty("AppName")
    public String appName;
}
