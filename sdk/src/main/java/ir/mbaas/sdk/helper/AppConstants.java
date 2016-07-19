package ir.mbaas.sdk.helper;

public interface AppConstants {
    // Note: emulator IP for host's localhost is 10.0.2.2
    String INSTANCE_URL = "http://push.mbaas.ir/api/v2";
    String GCM_SERVICE  = "mbaas_push";

    String DIRECT_URL = "http://mbaas.ir";
    String DIRECT_SERVICE  = "api/push";

    String GCM_REGISTER_API  = "register";
    String GCM_UPDATE_API  = "update";
    String GCM_DELIVER_API   = "delivery";
    String GCM_BULK_DELIVER_API   = "BulkDelivery";
    String GCM_LOCATIONS_API = "locations";

    String API_KEY = "dc7dec8087f417c0c4212309415804690abe38127dfb9bfa5c442633170b068f";

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
    String PN_IS_HIDDEN    = "IsHidden";

    String APP_NOTIFICATION_ID = "notification_id";

    String UD_ACTION_TYPE = "UpdateActionType";
    String UD_DOWNLOAD_URL= "UpdateDownloadUrl";
    long UD_LATER_TIME = 1 * 24 * 3600 * 1000; // Epoch = Days * Hours * Seconds * 1000

    String APK_EXTENSION = ".apk";

    String DELIVERY_JSON_FORMAT = "{\"PushSentId\":\"%1$s\",\"Id\":\"%2$s\"}";

    public enum FailedDeliveriesStatus {
        NONE,
        GET,
        ADD,
        DELETE
    }
}
