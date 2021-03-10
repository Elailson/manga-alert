package br.com.algorit.mangaalert.job;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Random;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.retrofit.MangaService;
import br.com.algorit.mangaalert.roomdatabase.model.Manga;
import br.com.algorit.mangaalert.roomdatabase.repository.MangaRepository;

public class BackgroundWorker extends Worker {

    private final WeakReference<Context> weakContext;
    private final MangaRepository mangaRepository;

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.weakContext = new WeakReference<>(context);
        mangaRepository = new MangaRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        new VerifyAsyncTask(weakContext, mangaRepository).execute();
        return Result.success();
    }

//    private void verificaMangas() {
//        Log.i(BackgroundWorker.class.getCanonicalName(), "============================================ VERIFICA MANGAS ============================================");
//        MangaService mangaService = new MangaService();
//        mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
//            @Override
//            public void success(List<Manga> response) {
//                notifyManga(response);
//            }
//
//            @Override
//            public void fail(String erro) {
//                Log.e("BACKGROUND_WORKER", erro);
//            }
//        });
//    }
//
//    public void notifyManga(List<Manga> mangas) {
//        List<Manga> mangasPrediletos = mangaRepository.getAllChecked();
//        for (Manga predileto : mangasPrediletos) {
//            for (Manga manga : mangas) {
//                if (manga.getNome().equals(predileto.getNome()) && manga.getCapitulo() > predileto.getCapitulo()) {
//                    mangaRepository.updateCapitulo(manga);
////                    mangaDB.insertManga(mangas);
//
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "manga")
//                            .setSmallIcon(R.drawable.ic_manga_24)
//                            .setContentTitle(manga.getNome())
//                            .setContentText("Capitulo " + getNumber(manga.getCapitulo()) + " já disponível")
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                            .setContentIntent(buildPendingIntent());
//
//                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//
//                    notificationManager.notify(getIdNotification(), builder.build());
//                }
//            }
//        }
//    }
//
//    private int getIdNotification() {
//        Random random = new Random();
//        return random.nextInt(1000);
//    }
//
//    private PendingIntent buildPendingIntent() {
//        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
//        notificationIntent.setData(Uri.parse("https://mangalivre.net/"));
//        return PendingIntent.getActivity(context, 0, notificationIntent, 0);
//    }
//
//    private String getNumber(Double capitulo) {
//        String value = capitulo.toString();
//        if (value.endsWith("0")) {
//            int intValue = capitulo.intValue();
//            return Integer.toString(intValue);
//        }
//        return capitulo.toString();
//    }

    private static class VerifyAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> weakContext;
        private final MangaRepository mangaRepository;

        VerifyAsyncTask(WeakReference<Context> weakContext, MangaRepository mangaRepository) {
            this.weakContext = weakContext;
            this.mangaRepository = mangaRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i(BackgroundWorker.class.getCanonicalName(), "============================================ VERIFICA MANGAS ============================================");
            MangaService mangaService = new MangaService();
            mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
                @Override
                public void success(List<Manga> response) {
                    new NotifyAsyncTask(weakContext, mangaRepository).execute(response);
                }

                @Override
                public void fail(String erro) {
                    Log.e("BACKGROUND_WORKER", erro);
                }
            });
            return null;
        }
    }

    private static class NotifyAsyncTask extends AsyncTask<List<Manga>, Void, Void> {

        private final WeakReference<Context> weakContext;
        private final MangaRepository mangaRepository;

        NotifyAsyncTask(WeakReference<Context> weakContext, MangaRepository mangaRepository) {
            this.weakContext = weakContext;
            this.mangaRepository = mangaRepository;
        }

        @Override
        protected Void doInBackground(List<Manga>[] mangas) {
            List<Manga> mangasPrediletos = mangaRepository.getAllChecked();
            for (Manga predileto : mangasPrediletos) {
                for (Manga manga : mangas[0]) {
                    if (manga.getNome().equals(predileto.getNome()) && manga.getCapitulo() > predileto.getCapitulo()) {
                        mangaRepository.updateCapitulo(manga);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(weakContext.get(), "manga")
                                .setSmallIcon(R.drawable.ic_manga_24)
                                .setContentTitle(manga.getNome())
                                .setContentText("Capitulo " + getNumber(manga.getCapitulo()) + " já disponível")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setContentIntent(buildPendingIntent());

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(weakContext.get());

                        notificationManager.notify(getIdNotification(), builder.build());
                    }
                }
            }
            return null;
        }

        private int getIdNotification() {
            return new Random()
                    .nextInt(1000);
        }

        private PendingIntent buildPendingIntent() {
            Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
            notificationIntent.setData(Uri.parse("https://mangalivre.net/"));
            return PendingIntent.getActivity(weakContext.get(), 0, notificationIntent, 0);
        }

        private String getNumber(Double capitulo) {
            String value = capitulo.toString();
            if (value.endsWith("0")) {
                int intValue = capitulo.intValue();
                return Integer.toString(intValue);
            }
            return capitulo.toString();
        }
    }
}
