package ir.mbaas.sdk.listeners;

import android.content.Context;

import ir.mbaas.sdk.models.MBaaSAppVersion;

/**
 * Created by Mehdi on 6/18/2016.
 */
public interface MBaaSListener {

    /**
     * Called when device register on MBaaS successfully.
     *
     * @param context An instance of the application {@link Context}
     * @param token   The value of the token.
     */
    void successRegistration(Context context, String token);

    /**
     * Called when device registration on MBaaS fails.
     *
     * @param context An instance of the application {@link Context}
     * @param token   The value of the token.
     */
    void failedRegistration(Context context, String token);

    /**
     * This method is always Called and returns version info
     *
     * @param context An instance of the application {@link Context}
     * @param mBaaSAppVersion   Last version data model on MBaaS.
     */
    void versionInfoAvailable(Context context, MBaaSAppVersion mBaaSAppVersion);
}
