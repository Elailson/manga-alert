package br.com.algorit.mangaalert.ui.fragment;

import android.content.Context;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.roomdatabase.model.Novel;
import br.com.algorit.mangaalert.ui.dialog.GenericDialog;
import br.com.algorit.mangaalert.ui.recyclerview.ItemClickListener;
import br.com.algorit.mangaalert.ui.recyclerview.NovelRecyclerAdapter;
import br.com.algorit.mangaalert.viewmodel.NovelViewModel;

public class NovelFragment extends Fragment implements ItemClickListener {

    private final Context context;
    private final List<Novel> novels;
    private RecyclerView recyclerView;
    private NovelRecyclerAdapter novelRecyclerAdapter;
    private NovelViewModel novelViewModel;

    public NovelFragment(Context context, List<Novel> novels) {
        this.context = context;
        this.novels = novels;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        novelViewModel = ViewModelProviders.of(this).get(NovelViewModel.class);
        novelViewModel.getAllNovels().observe(this, returnedNovels -> novelRecyclerAdapter.setNovels(returnedNovels));
        for (Novel novel : novels) {
            novelViewModel.insert(novel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novel, container, false);
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
        recyclerView = view.findViewById(R.id.fragment_novel_recycler_view);
        novelRecyclerAdapter = new NovelRecyclerAdapter(this, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(novelRecyclerAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }

    private void saveChanges() {
        List<Novel> listaNovel = ((NovelRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
        novelViewModel.uncheckAll();
        for (Novel novel : listaNovel) {
            novelViewModel.updateChecked(novel);
        }
        new GenericDialog(getContext()).show("Preferências atualizadas!");
    }
}