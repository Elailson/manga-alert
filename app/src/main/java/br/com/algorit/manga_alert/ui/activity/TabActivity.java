package br.com.algorit.manga_alert.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.room.model.Novel;
import br.com.algorit.manga_alert.ui.adapter.PagerAdapter;
import br.com.algorit.manga_alert.util.BlockUI;
import br.com.algorit.manga_alert.util.Notification;
import br.com.algorit.manga_alert.util.Worker;

public class TabActivity extends AppCompatActivity {

    private BlockUI blockUI;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        Toolbar toolbar = findViewById(R.id.activity_tab_toolbar);
        setSupportActionBar(toolbar);

        blockUI = new BlockUI(this);
        blockUI.start();

        new Notification(getApplicationContext());
        new Worker(getApplicationContext());

        MobileAds.initialize(this, initializationStatus -> {
            createAdBanner();
            createPersonalizedAd();
        });

        getFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showAd();
        return false;
    }

    private void getFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Mangas");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Manga>> type = new GenericTypeIndicator<HashMap<String, Manga>>() {
                };
                HashMap<String, Manga> lista = snapshot.getValue(type);
                if (lista != null) {
                    List<Manga> mangas = new ArrayList<>(lista.values());
                    getNovelsFromFirebase(mangas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TabActivity.class.getCanonicalName(),
                        "Failed to read value.", error.toException());
            }
        });
    }

    private void getNovelsFromFirebase(List<Manga> mangas) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Novels");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Novel>> type = new GenericTypeIndicator<HashMap<String, Novel>>() {
                };
                HashMap<String, Novel> lista = snapshot.getValue(type);
                if (lista != null) {
                    List<Novel> novels = new ArrayList<>(lista.values());
                    getManhuasFromFirebase(mangas, novels);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TabActivity.class.getCanonicalName(),
                        "Failed to read value.", error.toException());
            }
        });
    }

    private void getManhuasFromFirebase(List<Manga> mangas, List<Novel> novels) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Manhuas");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Manhua>> type = new GenericTypeIndicator<HashMap<String, Manhua>>() {
                };
                HashMap<String, Manhua> lista = snapshot.getValue(type);
                if (lista != null) {
                    List<Manhua> manhuas = new ArrayList<>(lista.values());
                    configTabLayout(mangas, novels, manhuas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TabActivity.class.getCanonicalName(),
                        "Failed to read value.", error.toException());
            }
        });
    }

    private void configTabLayout(List<Manga> mangas, List<Novel> novels, List<Manhua> manhuas) {
        TabLayout tabLayout = findViewById(R.id.activity_tab_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Mangá"));
        tabLayout.addTab(tabLayout.newTab().setText("Manhua"));
        tabLayout.addTab(tabLayout.newTab().setText("Novel"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        configViewPager(tabLayout, mangas, novels, manhuas);
    }

    private void configViewPager(TabLayout tabLayout, List<Manga> mangas, List<Novel> novels, List<Manhua> manhuas) {
        final ViewPager viewPager = findViewById(R.id.activity_tab_pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), getApplicationContext(), TabActivity.this,
                mangas, novels, manhuas);

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        blockUI.stop();
    }

    private void createAdBanner() {
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void createPersonalizedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        createInterstitialAd(adRequest);
    }

    private void createInterstitialAd(AdRequest adRequest) {
        String idTeste = "ca-app-pub-3940256099942544/1033173712";
        String idProd = "ca-app-pub-7971169796973042/6863692258";
        InterstitialAd.load(this, idTeste, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        createPersonalizedAd();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        mInterstitialAd = null;
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    private void showAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(TabActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TabActivity.class.getCanonicalName(), "ONRESUME");
    }
}