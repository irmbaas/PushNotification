package ir.mbaas.pushnotification;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import ir.mbaas.sdk.MBaaS;

/**
 * Created by Mahdi on 4/7/2016.
 */

@ReportsCrashes(
        formUri = "http://mbaas.ir/api/acra/78fec8fc"
)
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        MBaaS.init(this);
    }
}
