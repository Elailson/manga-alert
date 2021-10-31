package br.com.algorit.manga_alert.util;

import android.content.Context;
import android.os.AsyncTask;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import br.com.algorit.manga_alert.job.BackgroundWorker;

public class Worker {

    public Worker(Context context) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        new WorkerAsyncTask(weakContext).execute();
    }

    private static class WorkerAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> weakContext;

        WorkerAsyncTask(WeakReference<Context> weakContext) {
            this.weakContext = weakContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(BackgroundWorker.class, 2, TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build();

            WorkManager.getInstance(weakContext.get()).enqueue(periodicWorkRequest);

            return null;
        }
    }
}
