package br.com.algorit.mangaalert.ui.activity;

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
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.model.Novel;
import br.com.algorit.mangaalert.retrofit.MangaService;
import br.com.algorit.mangaalert.ui.PagerAdapter;
import br.com.algorit.mangaalert.util.BlockUI;
import br.com.algorit.mangaalert.util.Notification;
import br.com.algorit.mangaalert.util.Worker;

public class TabActivity extends AppCompatActivity {

    private BlockUI blockUI;

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

        configAdBanner();
        findMangas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void configTabLayout(List<Manga> mangas, List<Novel> novels) {
        TabLayout tabLayout = findViewById(R.id.activity_tab_tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Mangá"));
        tabLayout.addTab(tabLayout.newTab().setText("Manhua"));
        tabLayout.addTab(tabLayout.newTab().setText("Novel"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        configViewPager(tabLayout, mangas, novels);
    }

    private void configViewPager(TabLayout tabLayout, List<Manga> mangas, List<Novel> novels) {
        final ViewPager viewPager = findViewById(R.id.activity_tab_pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),
                tabLayout.getTabCount(), getApplicationContext(), TabActivity.this,
                mangas, novels);

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

    private void configAdBanner() {
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void findMangas() {
        MangaService mangaService = new MangaService();
        mangaService.findAll(new MangaService.ResponseCallback<List<Manga>>() {
            @Override
            public void success(List<Manga> response) {
                findNovels(response);
                blockUI.stop();
            }

            @Override
            public void fail(String erro) {
                Log.e(TabActivity.class.getCanonicalName(), erro);
            }
        });
    }

    private void findNovels(List<Manga> mangas) {
        MangaService mangaService = new MangaService();
        mangaService.findAllNovel(new MangaService.ResponseCallback<List<Novel>>() {
            @Override
            public void success(List<Novel> response) {
                configTabLayout(mangas, response);
            }

            @Override
            public void fail(String erro) {
                Log.e(TabActivity.class.getCanonicalName(), erro);
            }
        });
    }
}