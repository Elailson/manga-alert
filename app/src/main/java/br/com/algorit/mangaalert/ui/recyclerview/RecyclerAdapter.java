package br.com.algorit.mangaalert.ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.database.MangaDB;
import br.com.algorit.mangaalert.model.Manga;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final List<Manga> listaManga;
    private final Context context;
    private final ItemClickListener clickListener;

    public RecyclerAdapter(ItemClickListener clickListener, Context context, List<Manga> listaManga) {
        this.listaManga = listaManga;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(view, clickListener, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String titulo = listaManga.get(position).getNome();
        holder.titulo.setText(titulo);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                listaManga.get(position).setChecked(isChecked));

        markChecked();
        holder.checkBox.setChecked(listaManga.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return listaManga.size();
    }

    public List<Manga> getCheckedItems() {
        List<Manga> checkedItems = new ArrayList<>();
        for (Manga manga : listaManga) {
            if (manga.isChecked())
                checkedItems.add(manga);
        }
        return checkedItems;
    }

    private void markChecked() {
        MangaDB mangaDB = new MangaDB(context);
        List<Manga> mangas = mangaDB.getPrediletos();
        for (Manga manga : mangas) {
            for (int i = 0; i < getItemCount(); i++) {
                if (manga.getNome().equals(listaManga.get(i).getNome())) {
                    listaManga.get(i).setChecked(true);
                }
            }
        }
        mangaDB.close();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView titulo;
        private ItemClickListener clickListener;

        MyViewHolder(View itemView, ItemClickListener clickListener, Context context) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_lista_check);
            titulo = itemView.findViewById(R.id.item_lista_titulo);

            itemView.setOnClickListener(view -> clickListener.onItemClick(view, getAdapterPosition()));
        }
    }
}
