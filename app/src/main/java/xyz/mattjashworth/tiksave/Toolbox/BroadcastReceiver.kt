package xyz.mattjashworth.tiksave

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import xyz.mattjashworth.tiksave.toolbox.TokService

class OnBoot : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val serviceIntent = Intent(context, TokService::class.java)
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}