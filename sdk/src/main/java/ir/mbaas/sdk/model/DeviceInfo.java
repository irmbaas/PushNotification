package ir.mbaas.sdk.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mahdi on 2/20/2016.
 */
public class DeviceInfo {
    String IMEI;
    String deviceName;
    String brand;
    String osVersion;
    int sdkVersion;
    int userId;

    public DeviceInfo(String IMEI, String deviceName, String brand, String osVersion,
                      int sdkVersion, int userId) {
        this.IMEI = IMEI;
        this.deviceName = deviceName;
        this.brand = brand;
        this.osVersion = osVersion;
        this.sdkVersion = sdkVersion;
        this.userId = userId;
    }

    public JSONObject getDeviceInfoJson() {
        JSONObject retObj = new JSONObject();
        try {
            retObj.put("MbaasUserId", userId);
            retObj.put("IMEI", IMEI);
            retObj.put("OSName", "Android");
            retObj.put("OSVersion", osVersion);
            retObj.put("Manufacture", brand);
            retObj.put("DeviceModel", deviceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retObj;
    }

    public String getDeviceInfoString() {
        String retVal = "MbaasUserId=" + userId;
        retVal += "&IMEI=" + IMEI;
        retVal += "&OSName=" + "Android";
        retVal += "&OSVersion=" + osVersion;
        retVal += "&Manufacture=" + brand;
        retVal += "&DeviceModel=" + deviceName;
        return retVal;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
