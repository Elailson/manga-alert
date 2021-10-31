package br.com.algorit.manga_alert.ui.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.Manga;

public class MangaRecyclerAdapter extends RecyclerView.Adapter<MangaRecyclerAdapter.ViewHolder> {

    private final List<Manga> listaManga;
    private final Set<Manga> modifiedMangas;

    public MangaRecyclerAdapter(List<Manga> listaManga) {
        this.listaManga = listaManga;
        Collections.sort(this.listaManga, Manga::compareTo);
        modifiedMangas = new HashSet<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (listaManga != null) {
            holder.titulo.setText(listaManga.get(position).getNome());

            holder.checkBox.setOnCheckedChangeListener((button, isChecked) -> {
                listaManga.get(position).setChecked(isChecked);
                modifiedMangas.add(listaManga.get(position));
            });

            holder.checkBox.setChecked(listaManga.get(position).isChecked());
        }
    }

    public void filtrar(List<Manga> mangasFiltrados) {
        listaManga.clear();
        listaManga.addAll(mangasFiltrados);
        notifyDataSetChanged();
    }

    public void sort() {
        Collections.sort(this.listaManga, Manga::compareTo);
        notifyDataSetChanged();
    }

    public void addModifiedToList() {
        if (modifiedMangas != null && !modifiedMangas.isEmpty()) {
            for (Manga manga : modifiedMangas) {
                listaManga.remove(manga);
                listaManga.add(manga);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaManga != null ? listaManga.size() : 0;
    }

    public List<Manga> getCheckedItems() {
        Log.i(this.getClass().getCanonicalName(), "TAMANHO DA LISTA DE MANGÁS CLICADOS: " + modifiedMangas.size());
        return new ArrayList<>(modifiedMangas);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView titulo;

        ViewHolder(View view) {
            super(view);

            checkBox = view.findViewById(R.id.item_lista_check);
            titulo = view.findViewById(R.id.item_lista_titulo);
        }
    }
}
