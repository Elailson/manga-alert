package br.com.algorit.manga_alert.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.room.model.Novel;
import br.com.algorit.manga_alert.ui.activity.TabActivity;
import br.com.algorit.manga_alert.ui.fragment.MangaFragment;
import br.com.algorit.manga_alert.ui.fragment.ManhuaFragment;
import br.com.algorit.manga_alert.ui.fragment.NovelFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;
    private final TabActivity activity;
    private final int numOfTabs;
    private final List<Manga> mangas;
    private final List<Novel> novels;
    private final List<Manhua> manhuas;

    public PagerAdapter(@NonNull FragmentManager manager, int behavior, Context context,
                        TabActivity activity, List<Manga> mangas, List<Novel> novels, List<Manhua> manhuas) {
        super(manager, behavior);
        this.context = context;
        this.activity = activity;
        this.numOfTabs = behavior;
        this.mangas = mangas;
        this.novels = novels;
        this.manhuas = manhuas;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MangaFragment(this.context, this.mangas);
            case 1:
                return new ManhuaFragment(this.context, this.manhuas);
            case 2:
                return new NovelFragment(this.context, this.novels);
            default:
                throw new IllegalArgumentException("Posição não encontrada");
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
