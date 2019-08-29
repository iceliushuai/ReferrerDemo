package me.iceliushuai.demo.referrer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends Activity {

    TextView referralTv;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        referralTv = findViewById(R.id.referral);

        String referral = InstallReferrerReceiver.getSavedReferral(this);
        updateReferralText(referral);

        IntentFilter filter = new IntentFilter(InstallReferrerReceiver.REFERRER_RECEIVED);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getApplication());
        lbm.registerReceiver(mReferrerReceived, filter);
        mTracker = AnalyticsTrackers.getAppTracker();
    }

    public void updateReferralText(@Nullable String referral) {
        if (TextUtils.isEmpty(referral)) {
            referralTv.setText(R.string.referral_empty);
        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder(getString(R.string.referral_tips));
            final int start = ssb.length();
            ssb.append(referral);
            final int end = ssb.length();

            ssb.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.primary)),
                    start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            referralTv.setText(ssb);
        }
    }

    private BroadcastReceiver mReferrerReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String referral = intent.getStringExtra("referrer");
            updateReferralText(referral);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("MainActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getApplication());
        lbm.unregisterReceiver(mReferrerReceived);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new AlertDialog
                    .Builder(this)
                    .setTitle(R.string.app_name)
                    .setMessage(getString(R.string.versions, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE))
                    .setPositiveButton(android.R.string.ok, null)
                    .create()
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
