package ir.mbaas.sdk.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import ir.mbaas.sdk.R;
import ir.mbaas.sdk.models.Image;
import ir.mbaas.sdk.models.Images;

/**
 * Created by Mahdi on 5/11/2016.
 */
public class NotificationBuilderApi {

    public static void setNotificationIcon(Context ctx, NotificationCompat.Builder builder,
                                           Images images) {
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
}
