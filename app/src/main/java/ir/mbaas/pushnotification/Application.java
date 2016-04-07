package ir.mbaas.pushnotification;

import ir.mbaas.push.Push;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Push.initialize(this);
    }
}
