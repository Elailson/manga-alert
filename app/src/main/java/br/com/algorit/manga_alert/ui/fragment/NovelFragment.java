package br.com.algorit.manga_alert.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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
import br.com.algorit.manga_alert.room.model.Novel;
import br.com.algorit.manga_alert.ui.dialog.GenericDialog;
import br.com.algorit.manga_alert.ui.recyclerview.ItemClickListener;
import br.com.algorit.manga_alert.ui.recyclerview.NovelRecyclerAdapter;
import br.com.algorit.manga_alert.viewmodel.NovelViewModel;

public class NovelFragment extends Fragment implements ItemClickListener {

    private final Context context;
    private final List<Novel> novels;
    private RecyclerView recyclerView;
    private NovelRecyclerAdapter novelRecyclerAdapter;
    private NovelViewModel novelViewModel;
    private MenuItem menuItem;

    public NovelFragment(Context context, List<Novel> novels) {
        this.context = context;
        this.novels = novels;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        novelViewModel = new ViewModelProvider(this).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(this, returnedNovels -> novelRecyclerAdapter.setNovels(returnedNovels));
        for (Novel novel : novels) {
            novelViewModel.insert(novel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
        setHasOptionsMenu(true);
        init(view);
        return view;
    }

    private void init(View view) {
        initAndConfigureRecycler(view);
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
                List<Novel> novelsFiltradas = new ArrayList<>();
                for (Novel novel : novels) {
                    if (novel.getNome().toLowerCase().contains(input)) {
                        novelsFiltradas.add(novel);
                    }
                }
                novelRecyclerAdapter.filtrar(novelsFiltradas);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        saveChanges();
        return super.onOptionsItemSelected(item);
    }

    private void initAndConfigureRecycler(View view) {
        recyclerView = view.findViewById(R.id.fragment_novel_recycler_view);
        novelRecyclerAdapter = new NovelRecyclerAdapter(this, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(novelRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, String position) {
    }

    private void saveChanges() {
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQuery("", true);

        List<Novel> listaNovel = ((NovelRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
        novelViewModel.uncheckAll();
        for (Novel novel : listaNovel) {
            novelViewModel.updateChecked(novel);
        }
        new GenericDialog(getContext()).show("Preferências atualizadas!");
    }
}