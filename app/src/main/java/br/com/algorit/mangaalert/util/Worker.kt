package br.com.algorit.mangaalert.util

import android.content.Context
import android.os.AsyncTask
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import br.com.algorit.mangaalert.job.BackgroundWorker
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class Worker(context: Context) {

    init {
        val weakContext = WeakReference(context)
        WorkerAsyncTask(weakContext).execute()
    }

    private class WorkerAsyncTask internal constructor(private val weakContext: WeakReference<Context>) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            val periodicWorkRequest = PeriodicWorkRequest.Builder(BackgroundWorker::class.java, 2, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()
            WorkManager.getInstance(weakContext.get()!!).enqueue(periodicWorkRequest)
            return null
        }
    }
}