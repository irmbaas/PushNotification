package ir.mbaas.sdk.logic;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.IdGenerator;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.models.NotificationButton;
import ir.mbaas.sdk.models.NotificationButtons;
import ir.mbaas.sdk.models.NotificationContent;
import ir.mbaas.sdk.models.NotificationImage;
import ir.mbaas.sdk.models.NotificationImages;

/**
 * Created by Mahdi on 5/11/2016.
 */
public class JSONNotificationBuilder {
    Context ctx;
    NotificationContent notificationContent;
    NotificationCompat.Builder builder;
    private String message;
    private String title;
    private String summary;
    private String ticker;
    private int notificationId;

    public JSONNotificationBuilder(Context ctx, NotificationContent notificationContent) {
        this.ctx  = ctx;
        notificationId = IdGenerator.generateIntegerId();

        this.builder = new NotificationCompat.Builder(ctx).setAutoCancel(true);

        setTexts();
        setIntent();
        setButtons();
        setImages();
        setSound();
    }

    private void setTexts() {
        message = notificationContent.body;
        title   = notificationContent.title;
        summary = notificationContent.summary;
        ticker  = notificationContent.ticker;

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

        Intent intent = PushActions.createContentAction(notificationContent.customData,
                notificationContent.actionUrl, notificationContent.actionType);

        if(intent == null)
            return;

        PendingIntent mainIntent = PendingIntent.getActivity(ctx, IdGenerator.generateIntegerId(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(mainIntent);
    }

    private void setButtons() {
        NotificationButtons buttons = notificationContent.buttons;

        if(buttons == null || buttons.records == null)
            return;

        for (NotificationButton button : buttons.records) {
            Intent intent = PushActions.createButtonAction(
                    notificationContent.customData,
                    button.actionUrl,
                    button.actionType,
                    notificationId);

            if (intent == null)
                continue;

            PendingIntent mainIntent = PendingIntent.getBroadcast(ctx,
                    IdGenerator.generateIntegerId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(button.iconResourceId, button.title, mainIntent);
        }
    }

    private void setImages() {
        boolean smallIconExists = false;
        NotificationImages images = notificationContent.images;

        if (images != null && images.records != null) {
            for (NotificationImage image : images.records) {
                if (image.type == NotificationImages.ImageType.Small) {
                    smallIconExists = true;
                    setNotificationIcon(image);
                } else if (image.type == NotificationImages.ImageType.Large) {
                    setBigPicture(image);
                }
            }
        }

        if (!smallIconExists) {
            setNotificationIcon(null);
        }
    }

    private void setSound() {
        if(!notificationContent.isSilent)
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
    }

    private void setNotificationIcon(NotificationImage image) {
        builder.setSmallIcon(ctx.getApplicationInfo().icon);

        Bitmap largeIcon;

        if (image != null && image.url != null && !image.url.isEmpty()) {
            largeIcon = StaticMethods.downloadImage(AppConstants.MBAAS_BASE_URL + image.url);
        } else {
            largeIcon = BitmapFactory.decodeResource(MBaaS.context.getResources(),
                    ctx.getApplicationInfo().icon);
        }

        if (largeIcon == null) {
            //TODO: throw Exception
            return;
        }

        builder.setLargeIcon(largeIcon);
    }

    private void setBigPicture(NotificationImage image) {
        NotificationCompat.BigPictureStyle bigPic = new NotificationCompat.BigPictureStyle();

        if (image != null && image.url != null && !image.url.isEmpty()) {
            Bitmap img = StaticMethods.downloadImage(AppConstants.MBAAS_BASE_URL + image.url);

            if (img == null) {
                return;
            }
            bigPic.setBigContentTitle(title);
            bigPic.setSummaryText(summary);
            bigPic.bigPicture(img);
            builder.setStyle(bigPic);
        }
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void notifyPushAndDeliver() {
        notifyPush();
        StaticMethods.deliverPush(notificationContent.pushSentId, notificationContent.id);
    }

    public void notifyPush() {
        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
