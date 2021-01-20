package br.com.algorit.mangaalert.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.model.Novel;
import br.com.algorit.mangaalert.ui.activity.TabActivity;
import br.com.algorit.mangaalert.ui.fragment.MangaFragment;
import br.com.algorit.mangaalert.ui.fragment.ManhuaFragment;
import br.com.algorit.mangaalert.ui.fragment.NovelFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final Context context;
    private final TabActivity activity;
    private final int numOfTabs;
    private final List<Manga> mangas;
    private final List<Novel> novels;

    public PagerAdapter(@NonNull FragmentManager manager, int behavior, Context context,
                        TabActivity activity, List<Manga> mangas, List<Novel> novels) {
        super(manager, behavior);
        this.context = context;
        this.activity = activity;
        this.numOfTabs = behavior;
        this.mangas = mangas;
        this.novels = novels;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MangaFragment(this.activity, this.context, this.mangas);
            case 1:
                return new ManhuaFragment();
            case 2:
                return new NovelFragment(this.activity, this.context, this.novels);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
