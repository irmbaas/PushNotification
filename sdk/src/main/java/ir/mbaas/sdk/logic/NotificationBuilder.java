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
import ir.mbaas.sdk.R;
import ir.mbaas.sdk.helper.AppConstants;
import ir.mbaas.sdk.helper.IdGenerator;
import ir.mbaas.sdk.helper.StaticMethods;
import ir.mbaas.sdk.models.NotificationButton;
import ir.mbaas.sdk.models.NotificationButtons;
import ir.mbaas.sdk.models.NotificationImage;
import ir.mbaas.sdk.models.NotificationImages;

/**
 * Created by Mahdi on 5/11/2016.
 */
public class NotificationBuilder {
    Context ctx;
    Bundle data;
    NotificationCompat.Builder builder;
    private String message;
    private String title;
    private String summary;
    private String ticker;
    private int notificationId;

    public NotificationBuilder(Context ctx, Bundle data) {
        this.ctx  = ctx;
        this.data = data;
        notificationId = IdGenerator.generateIntegerId();

        this.builder = new NotificationCompat.Builder(ctx).setAutoCancel(true);

        setTexts();
        setIntent();
        setButtons();
        setImages();
        setSound();
    }

    private void setTexts() {
        message = data.getString(AppConstants.PN_BODY);
        title   = data.getString(AppConstants.PN_TITLE);
        summary = data.getString(AppConstants.PN_SUMMARY);
        ticker  = data.getString(AppConstants.PN_TICKER);

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

        Intent intent = PushActions.createContentAction(
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
        NotificationButtons buttons = NotificationButtons.fromJson(data.getString(AppConstants.PN_BUTTONS));

        if(buttons == null)
            return;

        for (NotificationButton button : buttons.records) {
            Intent intent = PushActions.createButtonAction(
                    data.getString(AppConstants.PN_CUSTOM_DATA),
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
        NotificationImages images = NotificationImages.fromJson(data.getString(AppConstants.PN_IMAGES));

        if (images != null && images.records.size() > 0) {
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
        String silenStr = data.getString(AppConstants.PN_IS_SILENT);
        boolean isSilent = silenStr != null && silenStr.equalsIgnoreCase("true");

        if(!isSilent)
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

        String sentId  = data.getString(AppConstants.PN_SENT_ID);
        String id      = data.getString(AppConstants.PN_ID);
        StaticMethods.deliverPush(sentId, id);
    }

    public void notifyPush() {
        NotificationManager notificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
