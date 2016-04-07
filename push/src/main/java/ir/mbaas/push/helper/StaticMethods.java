package ir.mbaas.push.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import ir.mbaas.push.R;

/**
 * Created by Mahdi on 4/7/2016.
 */
public class StaticMethods {

    public static String getSenderId(Context ctx) {
        try {
            ApplicationInfo ai = null;
            ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String senderId = bundle.getString(ctx.getResources().getString(R.string.senderid_metadata));;
            return senderId.substring(senderId.lastIndexOf("_") + 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
