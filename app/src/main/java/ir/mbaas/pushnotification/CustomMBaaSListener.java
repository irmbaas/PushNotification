package ir.mbaas.pushnotification;

import android.content.Context;
import android.util.Log;

import org.acra.ACRA;

import ir.mbaas.sdk.helper.CustomDialogs;
import ir.mbaas.sdk.listeners.MBaaSListener;
import ir.mbaas.sdk.models.MBaaSAppVersion;

/**
 * Created by Mehdi on 6/18/2016.
 */
public class CustomMBaaSListener implements MBaaSListener {
    @Override
    public void successRegistration(Context context, String token) {
        CustomDialogs.Toast(context, R.string.mbaas_successfull_registration);
    }

    @Override
    public void failedRegistration(Context context, String token) {
        CustomDialogs.Toast(context, R.string.mbaas_registration_failed);
    }

    @Override
    public void versionInfoAvailable(Context context, MBaaSAppVersion mBaaSAppVersion) {
        int i = 3;
    }

    @Override
    public void exception(Context context, Exception exc, int priority) {
        ACRA.getErrorReporter().handleSilentException(exc);
    }
}
