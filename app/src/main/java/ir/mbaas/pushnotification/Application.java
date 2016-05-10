package ir.mbaas.pushnotification;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import ir.mbaas.sdk.Push;

/**
 * Created by Mahdi on 4/7/2016.
 */

@ReportsCrashes(
        formUri = "http://acra.mbaas.ir/api/acra/1f656f69"
)
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        Push.initialize(this, MainActivity.class);
    }
}
