/*
Copyright 2015 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package ir.mbaas.sdk.logic;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import ir.mbaas.sdk.MBaaS;
import ir.mbaas.sdk.helper.PrefUtil;
import ir.mbaas.sdk.apis.Registration;
import ir.mbaas.sdk.helper.StaticMethods;

public class InstanceIdHelper {
    private String TAG = "InstanceIdHelper";
    private final Context mContext;
    private String senderId;
    private String scope;
    private Bundle extras;

    public InstanceIdHelper(Context context) {
        mContext = context;
    }

    /**
     * Get a Instance ID authorization Token
     */
    public void getToken(final String senderId, final String scope, final Bundle extras) {
        this.senderId = senderId;
        this.scope = scope;
        this.extras = extras;

        GetTokenTask getTokenTask = new GetTokenTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getTokenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            getTokenTask.execute();
        }
    }

    private class GetTokenTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                InstanceID instanceID = InstanceID.getInstance(mContext);
                String token = instanceID.getToken(senderId, scope, extras);
                Log.d(TAG, " senderId: " + senderId + " token: " + token);
                return token;
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String token) {
            String localToken = PrefUtil.getString(mContext, PrefUtil.REGISTRATION_ID);

            if (token != null) {

                if (MBaaS.gcmRegistrationListener != null) {
                    MBaaS.gcmRegistrationListener.onTokenAvailable(MBaaS.context, token,
                            !localToken.equals(token));
                }

                PrefUtil.putString(mContext, PrefUtil.REGISTRATION_ID, token);
            } else {
                token = "";
            }

            Registration regApi = new Registration(mContext, token, MBaaS.device,
                    MBaaS.hasGooglePlayService);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                regApi.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                regApi.execute();
            }
        }
    }

    public String getInstanceId() {
        return InstanceID.getInstance(mContext).getId();
    }

    public long getCreationTime() {
        return InstanceID.getInstance(mContext).getCreationTime();
    }
}
