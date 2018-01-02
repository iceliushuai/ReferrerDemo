package me.iceliushuai.demo.referrer;

import android.app.Application;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;

/**
 * Created by yrickwong on 15/9/10.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //int Google Analytics
        AnalyticsTrackers.initialize(this);

        InstallReferrerClient.Builder builder = InstallReferrerClient.newBuilder(this);
        final InstallReferrerClient client = builder.build();
        client.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {

            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }
}
