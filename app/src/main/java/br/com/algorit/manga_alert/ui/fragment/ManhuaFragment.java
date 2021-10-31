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
import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.ui.dialog.GenericDialog;
import br.com.algorit.manga_alert.ui.recyclerview.ManhuaRecyclerAdapter;
import br.com.algorit.manga_alert.viewmodel.ManhuaViewModel;

public class ManhuaFragment extends Fragment {

    private final Context context;
    private final List<Manhua> manhuas;
    private RecyclerView recyclerView;
    private ManhuaRecyclerAdapter manhuaRecyclerAdapter;
    private ManhuaViewModel manhuaViewModel;
    private MenuItem menuItem;

    public ManhuaFragment(Context context, List<Manhua> manhuas) {
        this.context = context;
        this.manhuas = manhuas;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        manhuaViewModel = new ViewModelProvider(this).get(ManhuaViewModel.class);
        manhuaViewModel.getAllManhuas().observe(this, returnedManhuas -> manhuaRecyclerAdapter.setManhuas(returnedManhuas));
        for (Manhua manhua : manhuas) {
            manhuaViewModel.insert(manhua);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manhua, container, false);
        setHasOptionsMenu(true);
        initAndConfigureRecycler(view);
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
                List<Manhua> manhuasFiltrados = new ArrayList<>();
                for (Manhua manhua : manhuas) {
                    if (manhua.getNome().toLowerCase().contains(input)) {
                        manhuasFiltrados.add(manhua);
                    }
                }
                manhuaRecyclerAdapter.filtrar(manhuasFiltrados);
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
        recyclerView = view.findViewById(R.id.fragment_manhua_recycler_view);
        manhuaRecyclerAdapter = new ManhuaRecyclerAdapter(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(manhuaRecyclerAdapter);
    }

    private void saveChanges() {
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQuery("", true);

        List<Manhua> listaManhuas = ((ManhuaRecyclerAdapter) recyclerView.getAdapter()).getCheckedItems();
        manhuaViewModel.uncheckAll();
        for (Manhua manhua : listaManhuas) {
            Log.i(ManhuaFragment.class.getCanonicalName(), "============== SAVE CHANGES: " + manhua.getNome());
            manhuaViewModel.updateChecked(manhua);
        }
        new GenericDialog(getContext()).show("Preferências atualizadas!");
    }
}
