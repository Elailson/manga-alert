package br.com.algorit.mangaalert.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.database.MangaDB;
import br.com.algorit.mangaalert.job.BackgroundWorker;
import br.com.algorit.mangaalert.job.MangaJobService;
import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.retrofit.MangaService;
import br.com.algorit.mangaalert.ui.dialog.GenericDialog;
import br.com.algorit.mangaalert.ui.recyclerview.ItemClickListener;
import br.com.algorit.mangaalert.ui.recyclerview.MangaRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    private InterstitialAd interstitialAd;
    private FloatingActionButton floatingButton;
    private RecyclerView recyclerView;
    private MangaDB mangaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
//        job();
        init();
        worker();
    }

    private void init() {
        initAdMob();
        floatingButton = findViewById(R.id.activity_main_floating_button);
        mangaDB = new MangaDB(getApplicationContext());
        MangaService mangaService = new MangaService();
        mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
            @Override
            public void success(List<Manga> response) {
                initAndConfigureRecycler(response);
            }

            @Override
            public void fail(String erro) {
                new GenericDialog(MainActivity.this).show("Ops! tente novamente");
            }
        });
        eventosClick();
    }

    private void eventosClick() {
        floatingButton.setOnClickListener(view -> showAd());
    }

    private void initAndConfigureRecycler(List<Manga> listaManga) {
        recyclerView = findViewById(R.id.activity_main_recycler_view);
        MangaRecyclerAdapter mangaRecyclerAdapter = new MangaRecyclerAdapter(this, getApplicationContext(), listaManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mangaRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    private void job() {
        ComponentName componentName = new ComponentName(this, MangaJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(1000L * 10L)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(info);
    }

    private void worker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(BackgroundWorker.class, 20, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(periodicWorkRequest);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MangaChannel";
            String description = "Channel for Manga Reminder";
            NotificationChannel channel = new NotificationChannel("manga", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void initAdMob() {
        String idTeste = "ca-app-pub-3940256099942544/1033173712";
        String idProd = "ca-app-pub-6750275666832506/4671759050";

        MobileAds.initialize(this, initializationStatus -> {
        });
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(idTeste);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        configAdBanner();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void configAdBanner() {
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void showAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            List<Manga> listaManga = ((MangaRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
            mangaDB.insertPrediletos(listaManga);
            mangaDB.insertMangaNome(listaManga);
            new GenericDialog(this).show("Preferências atualizadas!");
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Log.e(MainActivity.class.getCanonicalName(), "Erro ao tentar exibir AD: " + ex.getMessage());
            }
            showAd();
        }
    }
}