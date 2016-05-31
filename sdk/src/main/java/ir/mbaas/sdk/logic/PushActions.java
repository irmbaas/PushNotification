package ir.mbaas.sdk.logic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.receivers.NotificationButtonReceiver;

/**
 * Created by Mahdi on 5/15/2016.
 */
public class PushActions {

    public enum ContentActionType {
        None, OpenApp, OpenUrl;
    }

    public enum ButtonActionType {
        OpenApp(0), OpenUrl(1);

        private final int value;

        private ButtonActionType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    static public Intent createContentAction(String customData, String actionUrl,
                                             String actionType) {
        int actionTypeInt = 0;

        try {
            actionTypeInt = Integer.parseInt(actionType);
        } catch (NumberFormatException nfe) {
        }

        return createContentAction(customData, actionUrl, ContentActionType.values()[actionTypeInt]);
    }

    static public Intent createContentAction(String customData, String actionUrl,
                                      ContentActionType actionType) {
        Intent intent = null;

        switch (actionType) {
            case OpenApp:
                PackageManager pm = MBaaS.context.getPackageManager();
                intent = pm.getLaunchIntentForPackage(MBaaS.context.getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra(AppConstants.PN_CUSTOM_DATA, customData);
                break;
            case OpenUrl:
                if (actionUrl != null && !actionUrl.isEmpty()) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(actionUrl));
                }
        }

        return intent;
    }

    static public Intent createButtonAction(String customData, String actionUrl,
                                            ButtonActionType actionType, int notificationId) {
        Intent intent = new Intent(MBaaS.context, NotificationButtonReceiver.class);

        intent.putExtra(AppConstants.PN_CUSTOM_DATA, customData);
        intent.putExtra(AppConstants.PN_ACTION_URL, actionUrl);
        intent.putExtra(AppConstants.PN_ACTION_TYPE, actionType.getValue());
        intent.putExtra(AppConstants.APP_NOTIFICATION_ID,notificationId);

        return intent;
    }
}
