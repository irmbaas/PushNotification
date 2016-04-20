package ir.mbaas.push.helper;

public interface AppConstants {
    // Note: emulator IP for host's localhost is 10.0.2.2
    String INSTANCE_URL = "http://mb-lab.mbaas.ir/api/v2";
    String GCM_SERVICE  = "MBAAS_PUSH";

    String GCM_REGISTER_API  = "";
    String GCM_DELIVER_API   = "deliver";

    String AUTH_SVC = "user";
    String DB_SVC = "db/_table";
    // API key for your app goes here, see apps tab in admin console
    String API_KEY = "b48a0426f5943e7196c4523dc661a60144f0fe2c70deb3fb528d90ed33aa0f02";

    int VERSION_NUMBER = 1;
}
