package me.iceliushuai.demo.referrer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends Activity {

    TextView referrerTv;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        referrerTv = findViewById(R.id.referrer);
        String referrer = InstallReferrerReceiver.getSavedReferrer(this);
        referrerTv.setText(referrer == null ? "Empty" : referrer);

        IntentFilter filter = new IntentFilter(InstallReferrerReceiver.REFERRER_RECEIVED);
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(getApplication());
        lbm.registerReceiver(mReferrerReceived, filter);
        mTracker = AnalyticsTrackers.getAppTracker();
    }

    private BroadcastReceiver mReferrerReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String referrer = intent.getStringExtra("referrer");
            referrerTv.setText(referrer == null ? "Empty" : referrer);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
