package br.com.algorit.mangaalert.util;

import android.content.Context;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import br.com.algorit.mangaalert.job.BackgroundWorker;

public class Worker {

    private final Context context;

    public Worker(Context context) {
        this.context = context;
        worker();
    }

    private void worker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(BackgroundWorker.class, 20, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(context).enqueue(periodicWorkRequest);
    }
}
