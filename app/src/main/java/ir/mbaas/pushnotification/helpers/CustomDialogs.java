package ir.mbaas.pushnotification.helpers;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.Context;

import ir.mbaas.pushnotification.R;

/**
 * Created by Mahdi Moradi on 12/10/2014.
 */

public class CustomDialogs {

    public static void showOkMessage(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.create().show();
    }

    public static void showErrorAlert(Context context, int titleId, int msgId) {
        String title = context.getResources().getString(titleId);
        String msg   = context.getResources().getString(msgId);
        showErrorAlert(context, title, msg);
    }

    public static void showErrorAlert(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    public static void showWarningAlert(Context context, String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(android.R.string.cancel, null);
        builder.create().show();
    }

    public static void showMessageOKCancel(Context ctx, String message,
                                     DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(ctx)
                .setMessage(message)
                .setPositiveButton(R.string.ok, okListener)
                .setNegativeButton(R.string.cancel, null)
                .create()
                .show();
    }

    public static void Toast(Context ctx, int message) {
        Toast.makeText(ctx, ctx.getString(message), Toast.LENGTH_LONG).show();
    }

    public static void Toast(Context ctx, String message) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public static void dismissProgress(ProgressDialog progressDialog) {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            progressDialog = null;
        }
    }
}

