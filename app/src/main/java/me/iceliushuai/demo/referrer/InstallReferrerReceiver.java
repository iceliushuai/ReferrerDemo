package me.iceliushuai.demo.referrer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.analytics.HitBuilders;

public class InstallReferrerReceiver {

    public static final String REFERRER_RECEIVED = BuildConfig.APPLICATION_ID + ".action.REFERRER_RECEIVED";

    public static void setup(Context context) {
        InstallReferrerClient client = InstallReferrerClient.newBuilder(context)
                .build();
        client.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                    try {
                        ReferrerDetails details = client.getInstallReferrer();
                        String referrer = details.getInstallReferrer();

                        if (!TextUtils.isEmpty(referrer)) {
                            saveReferrer(context, referrer);
                            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
                            Intent referrerReceived = new Intent(REFERRER_RECEIVED);
                            referrerReceived.putExtra("referrer", referrer);
                            lbm.sendBroadcast(referrerReceived);
                            sendAnalytics(referrer);
                        }
                    } catch (RemoteException e) {
                        // omit exception
                    }
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }

    /**
     * Send GA Campaign Analytics
     *
     * @param referrer
     */
    private static void sendAnalytics(String referrer) {
        AnalyticsTrackers.getAppTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Install")
                .setAction("referrer_count")
                .setLabel(referrer)
                .setValue(1)
                .build());
    }

    public static void saveReferrer(Context context, String referrer) {
        SharedPreferences prefs = context.getSharedPreferences("referrer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("referrer", referrer);
        editor.apply();
    }

    public static String getSavedReferrer(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("referrer", Context.MODE_PRIVATE);
        return prefs.getString("referrer", null);
    }
}
