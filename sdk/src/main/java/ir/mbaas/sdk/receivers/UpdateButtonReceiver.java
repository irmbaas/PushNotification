package ir.mbaas.sdk.receivers;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import java.util.Date;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.logic.PushActions;
import ir.mbaas.sdk.logic.UpdateActions;

public class UpdateButtonReceiver extends BroadcastReceiver {
    public UpdateButtonReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(AppConstants.APP_NOTIFICATION_ID, 0);
        UpdateActions.UpdateActionType actionType = UpdateActions.UpdateActionType.values()
                [intent.getIntExtra(AppConstants.UD_ACTION_TYPE, 0)];
        String downloadUrl  = intent.getStringExtra(AppConstants.UD_DOWNLOAD_URL);

        switch (actionType) {
            case Now:
                updateNow(context, downloadUrl);
                break;
            case Later:
                Date now = new Date();
                PrefUtil.putLong(context, PrefUtil.NEXT_UPDATE_TIME, now.getTime() +
                        AppConstants.UD_LATER_TIME);
                break;
            case Never:
                break;
        }

        NotificationManager manager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    private void updateNow(Context context, String url) {
        DownloadManager dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false);

        String appName = context.getResources().getString(context.getApplicationInfo().labelRes);
        String pkgName = context.getApplicationInfo().packageName;

        pkgName = pkgName.substring(pkgName.lastIndexOf(".") + 1);

        appName = (appName == null || appName.isEmpty()) ? pkgName : appName;

        String downloadTitle = String.format(context.getResources()
                .getString(R.string.app_update_title), appName);

        request.setTitle(downloadTitle);

        try {
            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS,
                    pkgName + ".apk");
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        long downloadReference = dm.enqueue(request);
        PrefUtil.putLong(context, PrefUtil.UPDATE_REFERENCE_ID, downloadReference);
    }
}
