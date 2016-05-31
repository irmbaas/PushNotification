package ir.mbaas.sdk.helper;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by Mahdi on 5/30/2016.
 */
public interface GcmMessageListener {

    /**
     * Called when a downstream GCM message is received.  &lt;strong&gt; Runs on UI Thread &lt;strong/&gt;
     *
     * @param context An instance of the application {@link Context}
     * @param from    SenderID of the sender.
     * @param data    Data bundle containing message data as key/value pairs.
     *                For Set of keys use data.keySet().
     */
    void onMessageReceived(Context context, String from, Bundle data);
}
