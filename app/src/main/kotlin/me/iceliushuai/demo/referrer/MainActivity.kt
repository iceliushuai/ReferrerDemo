package me.iceliushuai.demo.referrer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mTracker: Tracker = ReferrerApp.appTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val referrer = InstallReferrerReceiver.getSavedReferrer(this)
        tvReferrer.text = referrer ?: "Empty"

        LocalBroadcastManager
                .getInstance(application)
                .registerReceiver(mReferrerReceived, IntentFilter(InstallReferrerReceiver.REFERRER_RECEIVED))
    }

    private val mReferrerReceived = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val referrer = intent.getStringExtra("referrer")
            tvReferrer.text = referrer ?: "Empty"
        }
    }


    override fun onResume() {
        super.onResume()
        mTracker.setScreenName("MainActivity")
        mTracker.send(HitBuilders.ScreenViewBuilder().build())
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager
                .getInstance(application)
                .unregisterReceiver(mReferrerReceived)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
