package ir.mbaas.sdk.logic;

import android.content.Intent;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.models.MBaaSAppVersion;
import ir.mbaas.sdk.receivers.NotificationButtonReceiver;
import ir.mbaas.sdk.receivers.UpdateButtonReceiver;

/**
 * Created by Mehdi on 6/21/2016.
 */
public class UpdateActions {
    public enum UpdateActionType {
        Now, Later, Never;
    }

    static public Intent createUpdateButtonAction(UpdateActionType updateActionType,
                                                  MBaaSAppVersion appVersion,
                                                  int notificationId) {
        Intent intent = new Intent(MBaaS.context, UpdateButtonReceiver.class);
        intent.putExtra(AppConstants.UD_ACTION_TYPE, updateActionType);
        intent.putExtra(AppConstants.UD_DOWNLOAD_URL, appVersion.downloadUrl);
        intent.putExtra(AppConstants.APP_NOTIFICATION_ID, notificationId);

        return intent;
    }
}
