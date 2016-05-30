package ir.mbaas.pushnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.GcmMessageListener;

/**
 * Created by Mahdi on 5/30/2016.
 */
public class CustomMessageReceivedListener implements GcmMessageListener {

    @Override
    public void onMessageReceived(Context context, String from, Bundle data) {
        showCustomNotification(context, data);
    }

    public void showCustomNotification(Context context, Bundle data) {
        // Set Notification Title
        String title   = data.getString(AppConstants.PN_TITLE);
        // Set Notification Text
        String message = data.getString(AppConstants.PN_BODY);
        // Set Notification Ticker
        String ticker  = data.getString(AppConstants.PN_TICKER);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Send data to MainActivity Class
        intent.putExtras(data);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_mbaas)
                // Set Ticker Message
                .setTicker(ticker)
                // Set Title
                .setContentTitle(title)
                // Set Text
                .setContentText(message)
                // Set SubText
                .setSubText("CustomMessageReceivedListener")
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);

        // Create Notification Manager
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Random random = new Random();
        // Build Notification with Notification Manager
        notificationManager.notify(random.nextInt(), builder.build());
    }
}
