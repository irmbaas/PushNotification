package ir.mbaas.sdk.receivers;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.io.File;

import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;

public class DownloadReceiver extends BroadcastReceiver {
    private String TAG = "DownloadReceiver";

    public DownloadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadID);
                DownloadManager dm =
                        (DownloadManager)context.getSystemService(context.DOWNLOAD_SERVICE);

                Cursor downloadResult = dm.query(query);

                if (downloadResult.moveToFirst()) {
                    int statusColumnIndex =
                            downloadResult.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    int status = downloadResult.getInt(statusColumnIndex);

                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        //download completed successfully
                        int localFileNameId =
                                downloadResult.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);

                        String downloadPathFile = downloadResult.getString(localFileNameId);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(Uri.fromFile(new File(downloadPathFile)),
                                "application/vnd.android.package-archive");
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(installIntent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            StaticMethods.sendException(e, Log.ERROR);
        }
    }
}
