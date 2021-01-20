package br.com.algorit.mangaalert.job;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.database.MangaDB;
import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.retrofit.MangaService;

public class BackgroundWorker extends Worker {

    private final Context context;
    private MangaDB mangaDB;

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        mangaDB = new MangaDB(this.context);
    }

    @NonNull
    @Override
    public Result doWork() {
        verificaMangas();
        return Result.retry();
    }

    private void verificaMangas() {
        MangaService mangaService = new MangaService();
        mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
            @Override
            public void success(List<Manga> response) {
                notifyManga(response);
            }

            @Override
            public void fail(String erro) {
                Log.e("BACKGROUND_WORKER", erro);
            }
        });
    }

    public void notifyManga(List<Manga> mangas) {
        List<Manga> mangasPrediletos = getSqliteMangas();
        for (Manga predileto : mangasPrediletos) {
            for (Manga manga : mangas) {
                if (manga.getNome().equals(predileto.getNome()) && manga.getCapitulo() > predileto.getCapitulo()) {
                    mangaDB.insertManga(mangas);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "manga")
                            .setSmallIcon(R.drawable.ic_manga_24)
                            .setContentTitle(manga.getNome())
                            .setContentText("Capitulo " + getNumber(manga.getCapitulo()) + " já disponível")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(buildPendingIntent());

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                    notificationManager.notify(getIdNotification(), builder.build());
                }
            }
        }
    }

    private int getIdNotification() {
        Random random = new Random();
        return random.nextInt(1000);
    }

    private PendingIntent buildPendingIntent() {
        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        notificationIntent.setData(Uri.parse("https://mangalivre.net/"));
        return PendingIntent.getActivity(context, 0, notificationIntent, 0);
    }

    private List<Manga> getSqliteMangas() {
        List<Manga> mangasPrediletos = mangaDB.getPrediletos();
        List<Manga> mangasCapitulos = mangaDB.getMangasNotificados();

        List<Manga> listaRetorno = new ArrayList<>();

        for (Manga mangaPredileto : mangasPrediletos) {
            for (Manga mangaCapitulo : mangasCapitulos) {
                if (mangaPredileto.getNome().equals(mangaCapitulo.getNome())) {
                    Manga manga = new Manga(mangaPredileto.getNome());
                    manga.setCapitulo(mangaCapitulo.getCapitulo());

                    listaRetorno.add(manga);
                }
            }
        }
        return listaRetorno;
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
