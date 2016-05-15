package ir.mbaas.sdk.logic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import ir.mbaas.sdk.SDK;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.IdGenerator;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.mbaas.Delivery;
import ir.mbaas.sdk.models.Button;
import ir.mbaas.sdk.models.Buttons;
import ir.mbaas.sdk.models.Image;
import ir.mbaas.sdk.models.Images;

/**
 * Created by Mahdi on 5/11/2016.
 */
public class NotificationBuilder {
    Context ctx;
    Bundle data;
    NotificationCompat.Builder builder;

    public NotificationBuilder(Context ctx, Bundle data) {
        this.ctx  = ctx;
        this.data = data;

        this.builder = new NotificationCompat.Builder(ctx).setAutoCancel(true);

        setTexts();
        setIntent();
        setButtons();
        setImages();
        setSound();
    }

    private void setTexts() {
        String message = data.getString(AppConstants.PN_BODY);
        String title   = data.getString(AppConstants.PN_TITLE);
        String summary = data.getString(AppConstants.PN_SUMMARY);
        String ticker  = data.getString(AppConstants.PN_TICKER);

        builder.setContentTitle(title)
                .setTicker(ticker)
                .setSubText(summary)
                .setContentText(message);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText(summary);

        builder.setStyle(bigText);
    }

    private void setIntent() {

        Intent intent = PushActions.createAction(
                data.getString(AppConstants.PN_CUSTOM_DATA),
                data.getString(AppConstants.PN_ACTION_URL),
                data.getString(AppConstants.PN_ACTION_TYPE));

        if(intent == null)
            return;

        PendingIntent mainIntent = PendingIntent.getActivity(ctx, IdGenerator.generateIntegerId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(mainIntent);
    }

    private void setButtons() {
        Buttons buttons = Buttons.fromJson(data.getString(AppConstants.PN_BUTTONS));

        for (Button button : buttons.records) {
            Intent intent = PushActions.createAction(
                    data.getString(AppConstants.PN_CUSTOM_DATA),
                    button.actionUrl,
                    button.actionType);

            if (intent == null)
                continue;

            PendingIntent mainIntent = PendingIntent.getActivity(ctx, IdGenerator.generateIntegerId(),
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(button.iconResourceId, button.title, mainIntent);
        }
    }

    private void setImages() {
        Images images = Images.fromJson(data.getString(AppConstants.PN_IMAGES));

        if(images.records.size() > 0)
            setNotificationIcon(images);
    }

    private void setSound() {
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
    }

    private void setNotificationIcon(Images images) {
        Image image = images.records.size() > 0 ? images.records.get(0) : null;
        boolean hasLargeIcon = (image != null && image.url != null && !image.url.isEmpty());

        builder.setSmallIcon(ctx.getApplicationInfo().icon);

        if (hasLargeIcon) {
            Bitmap img = StaticMethods.downloadImage(AppConstants.MBAAS_BASE_URL + image.url);

            if (img == null) {
                //TODO: throw Exception
                return;
            }
            builder.setLargeIcon(img);
        }
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void notifyPushAndDeliver() {
        notifyPush();
        deliverPush();
    }

    public void notifyPush() {
        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(IdGenerator.generateIntegerId(), builder.build());
    }

    public void deliverPush() {
        String sentId  = data.getString(AppConstants.PN_SENT_ID);
        String id      = data.getString(AppConstants.PN_ID);

        if(sentId == null || sentId.isEmpty() || id == null || id.isEmpty())
            return;

        Delivery delivery = new Delivery(ctx, sentId, id);
        delivery.execute();
    }
}
