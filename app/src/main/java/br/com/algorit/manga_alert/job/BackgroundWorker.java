package br.com.algorit.manga_alert.job;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.room.model.Novel;
import br.com.algorit.manga_alert.room.repository.MangaRepository;
import br.com.algorit.manga_alert.room.repository.ManhuaRepository;
import br.com.algorit.manga_alert.room.repository.NovelRepository;
import br.com.algorit.manga_alert.ui.activity.TabActivity;

public class BackgroundWorker extends Worker {

    private final WeakReference<Context> weakContext;
    private final MangaRepository mangaRepository;
    private final ManhuaRepository manhuaRepository;
    private final NovelRepository novelRepository;

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.weakContext = new WeakReference<>(context);
        mangaRepository = new MangaRepository((Application) context);
        manhuaRepository = new ManhuaRepository((Application) context);
        novelRepository = new NovelRepository((Application) context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.w(BackgroundWorker.class.getCanonicalName(), "EXECUTOU DO WORK");
        try {
            new VerifyAsyncTask(weakContext, mangaRepository, manhuaRepository, novelRepository).execute();
            return Result.success();
        } catch (RuntimeException exception) {
            return Result.failure();
        }
    }

    private static class VerifyAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> weakContext;
        private final MangaRepository mangaRepository;
        private final ManhuaRepository manhuaRepository;
        private final NovelRepository novelRepository;

        VerifyAsyncTask(WeakReference<Context> weakContext, MangaRepository mangaRepository,
                        ManhuaRepository manhuaRepository, NovelRepository novelRepository) {
            this.weakContext = weakContext;
            this.mangaRepository = mangaRepository;
            this.manhuaRepository = manhuaRepository;
            this.novelRepository = novelRepository;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mangasReference = database.getReference("Mangas");

            mangasReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<HashMap<String, Manga>> type = new
                            GenericTypeIndicator<HashMap<String, Manga>>() {
                            };
                    HashMap<String, Manga> lista = snapshot.getValue(type);

                    if (lista != null) {
                        List<Manga> mangas = new ArrayList<>(lista.values());
                        new NotifyAsyncTask(weakContext, mangaRepository, manhuaRepository,
                                novelRepository, mangas, null, null).execute();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TabActivity.class.getCanonicalName(),
                            "Failed to read value.", error.toException());
                }
            });

            DatabaseReference manhuasReference = database.getReference("Manhuas");

            manhuasReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<HashMap<String, Manhua>> type = new
                            GenericTypeIndicator<HashMap<String, Manhua>>() {
                            };
                    HashMap<String, Manhua> lista = snapshot.getValue(type);

                    if (lista != null) {
                        List<Manhua> manhuas = new ArrayList<>(lista.values());
                        new NotifyAsyncTask(weakContext, mangaRepository, manhuaRepository,
                                novelRepository, null, manhuas, null).execute();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TabActivity.class.getCanonicalName(),
                            "Failed to read value.", error.toException());
                }
            });

            DatabaseReference novelsReference = database.getReference("Novels");

            novelsReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    GenericTypeIndicator<HashMap<String, Novel>> type = new
                            GenericTypeIndicator<HashMap<String, Novel>>() {
                            };
                    HashMap<String, Novel> lista = snapshot.getValue(type);

                    if (lista != null) {
                        List<Novel> novels = new ArrayList<>(lista.values());
                        new NotifyAsyncTask(weakContext, mangaRepository, manhuaRepository,
                                novelRepository, null, null, novels).execute();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TabActivity.class.getCanonicalName(),
                            "Failed to read value.", error.toException());
                }
            });

            return null;
        }
    }

    private static class NotifyAsyncTask extends AsyncTask<Void, Void, Void> {

        private final WeakReference<Context> weakContext;
        private final MangaRepository mangaRepository;
        private final ManhuaRepository manhuaRepository;
        private final NovelRepository novelRepository;
        private final List<Manga> mangas;
        private final List<Manhua> manhuas;
        private final List<Novel> novels;

        NotifyAsyncTask(WeakReference<Context> weakContext, MangaRepository mangaRepository,
                        ManhuaRepository manhuaRepository, NovelRepository novelRepository,
                        List<Manga> mangas, List<Manhua> manhuas, List<Novel> novels) {
            this.weakContext = weakContext;
            this.mangaRepository = mangaRepository;
            this.manhuaRepository = manhuaRepository;
            this.novelRepository = novelRepository;
            this.mangas = mangas;
            this.manhuas = manhuas;
            this.novels = novels;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (mangas != null) {
                List<Manga> mangasPrediletos = mangaRepository.getAllChecked();
                for (Manga manga : mangas) {
                    for (Manga predileto : mangasPrediletos) {
                        if (manga.getNome().equals(predileto.getNome()) && manga.getCapitulo() > predileto.getCapitulo()) {
                            mangaRepository.updateCapitulo(manga);
                            notifyNewChapter(manga.getNome(), manga.getCapitulo());
                        }
                    }
                }
            } else if (manhuas != null) {
                List<Manhua> manhuasPrediletos = manhuaRepository.getAllChecked();
                for (Manhua manhua : manhuas) {
                    for (Manhua predileto : manhuasPrediletos) {
                        if (manhua.getNome().equals(predileto.getNome()) && manhua.getCapitulo() > predileto.getCapitulo()) {
                            manhuaRepository.updateCapitulo(manhua);
                            notifyNewChapter(manhua.getNome(), manhua.getCapitulo());
                        }
                    }
                }
            } else {
                List<Novel> novelsPrediletas = novelRepository.getAllChecked();
                for (Novel novel : novels) {
                    for (Novel predileto : novelsPrediletas) {
                        if (novel.getNome().equals(predileto.getNome()) && novel.getCapitulo() > predileto.getCapitulo()) {
                            novelRepository.updateCapitulo(novel);
                            notifyNewChapter(novel.getNome(), novel.getCapitulo());
                        }
                    }
                }
            }
            return null;
        }

        private void notifyNewChapter(String titulo, Double capitulo) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                Log.e(BackgroundWorker.class.getCanonicalName(), "Erro ao tentar notificar");
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(weakContext.get(), "manga")
                    .setSmallIcon(R.drawable.ic_manga_24)
                    .setContentTitle(titulo)
                    .setContentText("Capitulo " + getNumber(capitulo) + " já disponível")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(buildPendingIntent());

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(weakContext.get());

            notificationManager.notify(getIdNotification(), builder.build());
        }

        private int getIdNotification() {
            return new Random()
                    .nextInt(9999);
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
