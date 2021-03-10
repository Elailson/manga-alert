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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.roomdatabase.model.Manga;
import br.com.algorit.mangaalert.ui.dialog.GenericDialog;
import br.com.algorit.mangaalert.ui.recyclerview.ItemClickListener;
import br.com.algorit.mangaalert.ui.recyclerview.MangaRecyclerAdapter;
import br.com.algorit.mangaalert.viewmodel.MangaViewModel;

public class MangaFragment extends Fragment implements ItemClickListener {

    private final Context context;
    private final List<Manga> mangas;
    private RecyclerView recyclerView;
    private MangaRecyclerAdapter mangaRecyclerAdapter;
    private MangaViewModel mangaViewModel;

    public MangaFragment(Context context, List<Manga> mangas) {
        this.context = context;
        this.mangas = mangas;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel.class);
        mangaViewModel.getAllMangas().observe(this, returnedMangas -> mangaRecyclerAdapter.setMangas(returnedMangas));
        for (Manga manga : mangas) {
            mangaViewModel.insert(manga);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manga, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        initAndConfigureRecycler(view);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        saveChanges();
        return super.onOptionsItemSelected(item);
    }

    private void initAndConfigureRecycler(View view) {
        recyclerView = view.findViewById(R.id.fragment_manga_recycler_view);
        mangaRecyclerAdapter = new MangaRecyclerAdapter(this, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
//        new ItemTouchHelper(itemTouchCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(mangaRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mangas.remove(viewHolder.getAdapterPosition());
            mangaRecyclerAdapter.notifyDataSetChanged();
        }
    };

    private void saveChanges() {
        List<Manga> listaManga = ((MangaRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
        mangaViewModel.uncheckAll();
        for (Manga manga : listaManga) {
            Log.i(MangaFragment.class.getCanonicalName(), "============== SAVE CHANGES: " + manga.getNome());
            mangaViewModel.updateChecked(manga);
        }
        new GenericDialog(getContext()).show("Preferências atualizadas!");
    }
}