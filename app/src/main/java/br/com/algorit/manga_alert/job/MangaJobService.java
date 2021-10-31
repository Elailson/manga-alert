package br.com.algorit.manga_alert.job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.widget.Toast;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.retrofit.MangaService;

public class MangaJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        verificaMangas(params);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void verificaMangas(JobParameters params) {
        MangaService mangaService = new MangaService();
        mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
            @Override
            public void success(List<Manga> response) {
//                notifyManga(response);
            }

            @Override
            public void fail(String erro) {
                Toast.makeText(MangaJobService.this, "FAIL: " + erro, Toast.LENGTH_LONG).show();
            }
        });
        jobFinished(params, false);
    }

//    public void notifyManga(List<Manga> mangas) {
//        MangaDB mangaDB = new MangaDB(this);
//        List<Manga> mangasPrediletos = mangaDB.getPrediletos();
//        for (Manga predileto : mangasPrediletos) {
//            for (Manga manga : mangas) {
//                if (manga.getNome().equals(predileto.getNome())) {
//                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "manga")
//                            .setSmallIcon(R.drawable.ic_manga_24)
//                            .setContentTitle(manga.getNome())
//                            .setContentText("Capitulo " + getNumber(manga.getCapitulo()) + " já disponível")
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//                    notificationManager.notify(200, builder.build());
//                }
//            }
//        }
////        mangaDB.close();
//    }

    private String getNumber(Double capitulo) {
        String value = capitulo.toString();
        if (value.endsWith("0")) {
            int intValue = capitulo.intValue();
            return Integer.toString(intValue);
        }
        return capitulo.toString();
    }
}
