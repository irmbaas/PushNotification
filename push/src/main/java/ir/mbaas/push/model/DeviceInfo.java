package ir.mbaas.push.model;

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
            retObj.put("IMEI", IMEI);
            retObj.put("device_info", brand + "," + deviceName + "," + osVersion + "," + sdkVersion);
            retObj.put("userId", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retObj;
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
