package br.com.algorit.manga_alert.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.ui.dialog.GenericDialog;
import br.com.algorit.manga_alert.ui.recyclerview.MangaRecyclerAdapter;
import br.com.algorit.manga_alert.viewmodel.MangaViewModel;

public class MangaFragment extends Fragment {

    private final Context context;
    private final List<Manga> mangas;
    private MangaRecyclerAdapter mangaRecyclerAdapter;
    private RecyclerView recyclerView;
    private MangaViewModel mangaViewModel;
    private MenuItem menuItem;

    public MangaFragment(Context context, List<Manga> mangas) {
        this.context = context;
        this.mangas = mangas;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mangaViewModel = new ViewModelProvider(this).get(MangaViewModel.class);

        compareAndSave(mangas);

        mangaViewModel.getMangas().observe(this, returnedMangas -> {
            configureRecyclerView(returnedMangas);
            Log.i(MangaFragment.class.getCanonicalName(), "CHAMOU O OBSERVE E SETOU MANGÁS");
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.fragment_manga_recycler_view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(context);
        menuItem.setActionView(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                String input = query.toLowerCase();
                List<Manga> mangasFiltrados = new ArrayList<>();

                for (Manga manga : mangas) {
                    if (manga.getNome().toLowerCase().contains(input)) {
                        mangasFiltrados.add(manga);
                    }
                }

                mangaRecyclerAdapter.filtrar(mangasFiltrados);

                if (input.isEmpty()) {
                    mangaRecyclerAdapter.addModifiedToList();
                    mangaRecyclerAdapter.sort();
                }

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        saveChanges();
        return super.onOptionsItemSelected(item);
    }

    private void compareAndSave(List<Manga> downloadedMangas) {
        List<Manga> databaseMangas = mangaViewModel.getMangas().getValue();
        if (databaseMangas == null) {
            mangaViewModel.insertAll(downloadedMangas);
            return;
        }
        for (Manga downManga : downloadedMangas) {
            for (Manga baseManga : databaseMangas) {
                if (downManga.getNome().equals(baseManga.getNome())
                        && downManga.getCapitulo() > baseManga.getCapitulo()) {
                    downManga.setChecked(baseManga.isChecked());
                    mangaViewModel.insert(downManga);
                }
            }
        }
    }

    private void configureRecyclerView(List<Manga> mangas) {
        mangaRecyclerAdapter = new MangaRecyclerAdapter(mangas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mangaRecyclerAdapter);
    }

    private void saveChanges() {
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQuery("", false);
        searchView.setIconified(true);

        List<Manga> listaManga = mangaRecyclerAdapter.getCheckedItems();

        for (Manga manga : listaManga) {
            Log.i(MangaFragment.class.getCanonicalName(), "============== SAVE CHANGES: " + manga.getNome());
            mangaViewModel.updateChecked(manga);
        }

        mangaRecyclerAdapter.sort();
        new GenericDialog(getContext()).show("Preferências atualizadas!");
    }
}