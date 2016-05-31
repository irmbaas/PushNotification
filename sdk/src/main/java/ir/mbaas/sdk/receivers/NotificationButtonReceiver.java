package ir.mbaas.sdk.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.logic.PushActions;

public class NotificationButtonReceiver extends BroadcastReceiver {
    public NotificationButtonReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(AppConstants.APP_NOTIFICATION_ID, 0);
        PushActions.ButtonActionType actionType = PushActions.ButtonActionType.values()
                        [intent.getIntExtra(AppConstants.PN_ACTION_TYPE, 0)];
        String customData = intent.getStringExtra(AppConstants.PN_CUSTOM_DATA);
        String actionUrl  = intent.getStringExtra(AppConstants.PN_ACTION_URL);

        switch (actionType) {
            case OpenApp:
                PackageManager pm = context.getPackageManager();
                Intent aIntent = pm.getLaunchIntentForPackage(context.getPackageName());
                aIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                aIntent.putExtra(AppConstants.PN_CUSTOM_DATA, customData);
                context.startActivity(aIntent);
                break;
            case OpenUrl:
                if (actionUrl != null && !actionUrl.isEmpty()) {
                    Intent wIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(actionUrl));
                    wIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(wIntent);
                }
        }

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }
}
