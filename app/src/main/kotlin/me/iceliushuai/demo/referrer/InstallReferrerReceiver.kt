package me.iceliushuai.demo.referrer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log

import com.google.android.gms.analytics.CampaignTrackingReceiver
import com.google.android.gms.analytics.HitBuilders

class InstallReferrerReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action

        if ("com.android.vending.INSTALL_REFERRER" == action) {
            val args = intent.extras ?: return

            CampaignTrackingReceiver().onReceive(context, intent)
            for (key in args.keySet()) {
                Log.d(LOG_TAG, key + " = " + args.get(key))
            }

            val referrer = args.getString("referrer")
            if (!TextUtils.isEmpty(referrer)) {
                saveReferrer(context, referrer)
                val referrerReceived = Intent(REFERRER_RECEIVED)
                referrerReceived.putExtra("referrer", referrer)
                LocalBroadcastManager.getInstance(context)
                        .sendBroadcast(referrerReceived)
                sendAnalytics(referrer)
            }
        }
    }

    companion object {

        private val LOG_TAG = "ReferrerReceiver"

        val REFERRER_RECEIVED = BuildConfig.APPLICATION_ID + ".action.REFERRER_RECEIVED"

        /**
         * Send GA Campaign Analytics

         * @param referrer
         */
        private fun sendAnalytics(referrer: String) {
            ReferrerApp.appTracker.send(
                    HitBuilders.EventBuilder()
                            .setCategory("Install")
                            .setAction("referrer_count")
                            .setLabel(referrer)
                            .setValue(1)
                            .build())
        }

        fun saveReferrer(context: Context, referrer: String) {
            val prefs = context.getSharedPreferences("referrer", Context.MODE_PRIVATE)
            prefs.edit()
                    .putString("referrer", referrer)
                    .apply()
        }

        @Nullable
        fun getSavedReferrer(context: Context): String? {
            val prefs = context.getSharedPreferences("referrer", Context.MODE_PRIVATE)
            return prefs.getString("referrer", null)
        }
    }
}
