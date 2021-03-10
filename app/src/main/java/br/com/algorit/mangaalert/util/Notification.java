package br.com.algorit.mangaalert.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import java.lang.ref.WeakReference;

public class Notification {

    public Notification(Context context) {
        WeakReference<Context> weakContext = new WeakReference<>(context);
        new NotificationAsyncTask(weakContext).execute();
    }


    private static class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> weakContext;

        NotificationAsyncTask(WeakReference<Context> weakContext) {
            this.weakContext = weakContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "MangaChannel";
                String description = "Channel for Manga Reminder";
                NotificationChannel channel = new NotificationChannel("manga", name, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setDescription(description);

                NotificationManager notificationManager = weakContext.get().getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            return null;
        }
    }
}
