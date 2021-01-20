package br.com.algorit.mangaalert.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.database.MangaDB;
import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.ui.activity.TabActivity;
import br.com.algorit.mangaalert.ui.dialog.GenericDialog;
import br.com.algorit.mangaalert.ui.recyclerview.ItemClickListener;
import br.com.algorit.mangaalert.ui.recyclerview.RecyclerAdapter;

public class MangaFragment extends Fragment implements ItemClickListener {

    private final TabActivity activity;
    private final Context context;
    private final List<Manga> mangas;
    private InterstitialAd interstitialAd;
    private RecyclerView recyclerView;
    private MangaDB mangaDB;

    public MangaFragment(TabActivity activity, Context context, List<Manga> mangas) {
        this.activity = activity;
        this.context = context;
        this.mangas = mangas;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        initAdMob();
        mangaDB = new MangaDB(context);
        initAndConfigureRecycler(mangas, view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        showAd();
        return super.onOptionsItemSelected(item);
    }

    private void initAndConfigureRecycler(List<Manga> listaManga, View view) {
        recyclerView = view.findViewById(R.id.fragment_manga_recycler_view);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, context, listaManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    private void initAdMob() {
        String idTeste = "ca-app-pub-3940256099942544/1033173712";
        String idProd = "ca-app-pub-6750275666832506/4671759050";

        MobileAds.initialize(context, initializationStatus -> {
        });
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId(idTeste);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    private void showAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            RecyclerAdapter adapter = ((RecyclerAdapter) recyclerView.getAdapter());
            if (adapter != null) {
                List<Manga> listaManga = adapter.getCheckedItems();
                mangaDB.insertPrediletos(listaManga);
                mangaDB.insertMangaNome(listaManga);
                new GenericDialog(this.activity).show("Preferências atualizadas!");
            } else {
                new GenericDialog(this.activity).show("Tente novamente!");
            }
        } else {
            try {
                Thread.sleep(500);
            } catch (Exception ex) {
                Log.e(MangaFragment.class.getCanonicalName(), "Erro ao tentar exibir AD: " + ex.getMessage());
            }
            showAd();
        }
    }
}