package br.com.algorit.manga_alert.ui.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.job.BackgroundWorker;
import br.com.algorit.manga_alert.retrofit.MangaService;
import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.ui.dialog.GenericDialog;
import br.com.algorit.manga_alert.ui.recyclerview.MangaRecyclerAdapter;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd interstitialAd;
    private FloatingActionButton floatingButton;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(MainActivity.class.getCanonicalName(), "============================================ ON CREATE ============================================");
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        init();
        worker();
    }

    private void init() {
        initAdMob();
        floatingButton = findViewById(R.id.activity_main_floating_button);
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
        MangaRecyclerAdapter mangaRecyclerAdapter = new MangaRecyclerAdapter(listaManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mangaRecyclerAdapter);
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
        String idProd = "ca-app-pub-7971169796973042/6863692258";

        MobileAds.initialize(this, initializationStatus -> {
        });

        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                initAdMob();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                saveChanges();
            }
        };

        InterstitialAd.load(this, idTeste,
                new AdRequest.Builder().build(),
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd ad) {
                        interstitialAd = ad;
                        interstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        initAdMob();
                    }
                });

        configAdBanner();
    }

    private void configAdBanner() {
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void showAd() {
        if (interstitialAd != null) {
            interstitialAd.show(MainActivity.this);
        } else {
            saveChanges();
        }
    }

    private void saveChanges() {
        List<Manga> listaManga = ((MangaRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
//        mangaDB.insertPrediletos(listaManga);
//        mangaDB.insertMangaNome(listaManga);
        new GenericDialog(getApplicationContext()).show("Preferências atualizadas!");
    }
}