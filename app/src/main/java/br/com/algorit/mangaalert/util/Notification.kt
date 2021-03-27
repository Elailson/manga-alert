package br.com.algorit.mangaalert.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import java.lang.ref.WeakReference

class Notification(context: Context) {

    init {
        val weakContext = WeakReference(context)
        NotificationAsyncTask(weakContext).execute()
    }

    private class NotificationAsyncTask internal constructor(private val weakContext: WeakReference<Context>) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name: CharSequence = "MangaChannel"
                val description = "Channel for Manga Reminder"
                val channel = NotificationChannel("manga", name, NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = description
                val notificationManager = weakContext.get()!!.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
            return null
        }
    }
}