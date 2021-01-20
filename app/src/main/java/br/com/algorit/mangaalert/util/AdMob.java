package br.com.algorit.mangaalert.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import br.com.algorit.mangaalert.database.MangaDB;
import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.ui.dialog.GenericDialog;
import br.com.algorit.mangaalert.ui.fragment.MangaFragment;
import br.com.algorit.mangaalert.ui.recyclerview.MangaRecyclerAdapter;

public class AdMob {

    private final Activity activity;
    private final Context context;
    private final InterstitialAd interstitialAd;
    private final RecyclerView recyclerView;

    public AdMob(Activity activity, Context context, InterstitialAd interstitialAd, RecyclerView recyclerView) {
        this.activity = activity;
        this.context = context;
        this.interstitialAd = interstitialAd;
        this.recyclerView = recyclerView;
    }

    public void showAd() {
        if (this.interstitialAd.isLoaded()) {
            this.interstitialAd.show();
            MangaDB mangaDB = new MangaDB(this.context);
            MangaRecyclerAdapter adapter = ((MangaRecyclerAdapter) this.recyclerView.getAdapter());
            if (adapter != null) {
                List<Manga> listaManga = adapter.getCheckedItems();
                mangaDB.insertPrediletos(listaManga);
                mangaDB.insertMangaNome(listaManga);
                new GenericDialog(this.activity).show("Preferências atualizadas!");
            } else {
                new GenericDialog(this.activity).show("Tente novamente!");
            }
            mangaDB.close();
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
