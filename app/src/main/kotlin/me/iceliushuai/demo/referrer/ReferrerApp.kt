package me.iceliushuai.demo.referrer

import android.app.Application
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker

class ReferrerApp : Application() {
    companion object {
        var appTracker: Tracker by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        val tracker: Tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker)
        tracker.enableAdvertisingIdCollection(true)
        appTracker = tracker
    }
}




