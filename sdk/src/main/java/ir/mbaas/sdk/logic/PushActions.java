package ir.mbaas.sdk.logic;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import ir.mbaas.sdk.SDK;
import ir.mbaas.sdk.helper.AppConstants;

/**
 * Created by Mahdi on 5/15/2016.
 */
public class PushActions {

    public enum ActionType {
        None, OpenApp, OpenUrl
    }

    static public Intent createAction(String customData, String actionUrl, String actionType) {
        int actionTypeInt = 0;

        try {
            actionTypeInt = Integer.parseInt(actionType);
        } catch (NumberFormatException nfe) {
        }

        return createAction(customData, actionUrl, ActionType.values()[actionTypeInt]);
    }

    static public Intent createAction(String customData, String actionUrl, ActionType actionType) {
        Intent intent = null;

        switch (actionType) {
            case None:
                break;
            case OpenApp:
                PackageManager pm = SDK.context.getPackageManager();
                intent = pm.getLaunchIntentForPackage(SDK.context.getPackageName());
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
}
