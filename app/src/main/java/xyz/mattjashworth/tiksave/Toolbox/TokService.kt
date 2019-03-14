package xyz.mattjashworth.tiksave.toolbox

import android.app.*
import android.content.*
import android.os.Build
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import xyz.mattjashworth.tiksave.MainActivity
import xyz.mattjashworth.tiksave.Megaris
import xyz.mattjashworth.tiksave.R

class TokService : Service() {
    val clipBox = ArrayList<String>()


    companion object {
        val ACTION_PING = TokService::class.java.name + ".PING"
        val ACTION_PONG = TokService::class.java.name + ".PONG"

    }




    fun publicAddressSystem() {

        val subHeading = getString(R.string.heading)
        val heading = getString(R.string.sub_headings)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        var builder: Notification.Builder? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ChannelId = " ng.lekki.toksaver";
            val ChannelName = "Toksaver";
            val channel = NotificationChannel(ChannelId, ChannelName, NotificationManager.IMPORTANCE_LOW);
            val mNotificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
            mNotificationManager.createNotificationChannel(channel)
            builder = Notification.Builder(this, ChannelId)
        } else {
            builder = Notification.Builder(this)

        }

        builder!!.setContentTitle(heading)
                .setContentText(subHeading)
                .setSmallIcon(R.drawable.ic_smallie)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notification = builder.build()

        startForeground(190, notification)
    }

    override fun onCreate() {
        super.onCreate()

        publicAddressSystem()



    }


    override fun onBind(intent: Intent): IBinder? {
            // TODO: Return the communication channel to the service.
            throw UnsupportedOperationException("Not yet implemented")
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

            LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, IntentFilter(ACTION_PING));
            publicAddressSystem()

            val board = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            board.addPrimaryClipChangedListener {
                val mainText = board.primaryClip.getItemAt(0).text.toString()
                if (mainText.contains("tiktok.com/")) {

                    if (!clipBox.contains(mainText)) {


                        val dialogIntent = Intent(this, Megaris::class.java);
                        dialogIntent.putExtra(Intent.EXTRA_TEXT, mainText)
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent)


                    }


                } else {


                }

            }
            return super.onStartCommand(intent, flags, startId)
        }


        override fun onDestroy() {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            stopSelf()
            super.onDestroy()
        }


        private val mReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ACTION_PING) {
                    val manager = LocalBroadcastManager.getInstance(applicationContext)
                    manager.sendBroadcast(Intent(ACTION_PONG))
                }
            }
        }






}