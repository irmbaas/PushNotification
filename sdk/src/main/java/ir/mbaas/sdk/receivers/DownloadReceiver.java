package ir.mbaas.sdk.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ir.mbaas.sdk.helper.PrefUtil;

public class DownloadReceiver extends BroadcastReceiver {
    private String TAG = "DownloadReceiver";

    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //check if the broadcast message is for our Enqueued download
        long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long downloadReference = PrefUtil.getLong(context, PrefUtil.UPDATE_REFERENCE_ID, 0);

        if(downloadReference == referenceId){
            DownloadManager dm = (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);
            Log.v(TAG, "Downloading of the new app version complete");
            //start the installation of the latest version
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setDataAndType(dm.getUriForDownloadedFile(downloadReference),
                    "application/vnd.android.package-archive");
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(installIntent);
        }
    }
}
