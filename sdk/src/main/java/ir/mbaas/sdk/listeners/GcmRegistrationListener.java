package ir.mbaas.sdk.listeners;

import android.content.Context;

/**
 * Created by Mahdi on 6/9/2016.
 */
public interface GcmRegistrationListener {
    /**
     * Called when GCM tokens are available. If the callback is added to the helper with a STICKY
     * request then it would be immediately triggered with the value of updated as false
     *
     * @param context An instance of the Application context
     * @param token   The value of the token.
     * @param updated A boolean denotes whether this token was recently updated or is an existing
     *                token
     */
    void onTokenAvailable(Context context, String token, boolean updated);

    /**
     * Called when GCM tokens are deleted.
     *
     * @param context An instance of the application {@link Context}
     */
    void onTokenDeleted(Context context);

    void successRegistrationOnMBaaS(Context context, String token);

    void failedRegistrationOnMBaaS(Context context, String token);
}
