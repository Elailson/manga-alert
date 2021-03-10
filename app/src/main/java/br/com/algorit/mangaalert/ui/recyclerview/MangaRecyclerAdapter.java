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
import java.util.Collections;
import java.util.List;

import br.com.algorit.mangaalert.R;
import br.com.algorit.mangaalert.roomdatabase.model.Manga;

public class MangaRecyclerAdapter extends RecyclerView.Adapter<MangaRecyclerAdapter.MyViewHolder> {

    private final ItemClickListener clickListener;
    private final LayoutInflater layoutInflater;
    private List<Manga> listaManga;

    public MangaRecyclerAdapter(ItemClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (listaManga != null) {
            Manga manga = listaManga.get(position);
            holder.titulo.setText(manga.getNome());
            holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) ->
                    manga.setChecked(isChecked)));
            holder.checkBox.setChecked(manga.isChecked());
        }
    }

    public void setMangas(List<Manga> mangas) {
        this.listaManga = mangas;
        if (listaManga != null) {
            Collections.sort(this.listaManga, Manga::compareTo);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.listaManga != null)
            return listaManga.size();
        else return 0;
    }

    public List<Manga> getCheckedItems() {
        List<Manga> checkedItems = new ArrayList<>();
        for (Manga manga : listaManga) {
            if (manga.isChecked())
                checkedItems.add(manga);
        }
        return checkedItems;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkBox;
        private TextView titulo;
        private ItemClickListener clickListener;

        MyViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_lista_check);
            titulo = itemView.findViewById(R.id.item_lista_titulo);

//            itemView.setOnClickListener(view -> clickListener.onItemClick(view, getAdapterPosition()));
        }
    }
}
