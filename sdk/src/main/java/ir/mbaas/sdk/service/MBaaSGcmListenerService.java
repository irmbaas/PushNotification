package ir.mbaas.sdk.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.helper.NotificationBuilderApi;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.mbaas.Delivery;
import ir.mbaas.sdk.models.Button;
import ir.mbaas.sdk.models.Buttons;
import ir.mbaas.sdk.models.Images;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class MBaaSGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        PrefUtil.putString(this, PrefUtil.LAST_PUSH_RECEIVED, data.toString());
        Intent intent = new Intent(this, StaticMethods.cls);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        String message = data.getString("Body");
        String title   = data.getString("Title");
        String sentId  = data.getString("PushSentId");
        String id      = data.getString("Id");
        String summary = data.getString("Summary");

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setTicker(summary)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        Buttons buttons = Buttons.fromJson(data.getString("Buttons"));

        for (Button button : buttons.records) {
            mBuilder.addAction(0, button.title, pIntent);
        }

        Images images = Images.fromJson(data.getString("Images"));

        if(images.records.size() > 0)
            NotificationBuilderApi.setNotificationIcon(getApplicationContext(), mBuilder, images);

        notificationManager.notify(1, mBuilder.build());

        Delivery delivery = new Delivery(this, sentId, id);
        delivery.execute();
    }
}
