package ir.mbaas.sdk.helper;

public interface AppConstants {
    // Note: emulator IP for host's localhost is 10.0.2.2
    String INSTANCE_URL = "http://mb-lab.mbaas.ir/api/v2";
    String GCM_SERVICE  = "MBAAS_PUSH";

    String GCM_REGISTER_API  = "register";
    String GCM_UPDATE_API  = "update";
    String GCM_DELIVER_API   = "delivery";
    String GCM_LOCATIONS_API = "locations";

    String AUTH_SVC = "user";
    String DB_SVC = "db/_table";
    // API key for your app goes here, see apps tab in admin console
    String API_KEY = "b48a0426f5943e7196c4523dc661a60144f0fe2c70deb3fb528d90ed33aa0f02";

    int VERSION_NUMBER = 1;

    String MBAAS_BASE_URL = "http://mbaas.ir/";

    String ACRA_PRIVATE_PROCESS_NAME = ":acra";

    //Push Notification constants
    String PN_BODY    = "Body";
    String PN_TITLE   = "Title";
    String PN_SENT_ID = "PushSentId";
    String PN_ID      = "Id";
    String PN_SUMMARY = "Summary";
    String PN_BUTTONS = "Buttons";
    String PN_IMAGES  = "Images";
    String PN_TICKER  = "Ticker";
    String PN_ACTION_TYPE  = "ActionType";
    String PN_ACTION_URL   = "ActionUrl";
    String PN_CUSTOM_DATA  = "CustomData";
    String PN_IS_SILENT    = "IsSilent";
}
