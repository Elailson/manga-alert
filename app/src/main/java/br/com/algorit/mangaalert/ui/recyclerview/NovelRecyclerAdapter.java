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
import br.com.algorit.mangaalert.model.Novel;

public class NovelRecyclerAdapter extends RecyclerView.Adapter<NovelRecyclerAdapter.MyViewHolder> {

    private final List<Novel> novels;
    private final Context context;
    private final ItemClickListener clickListener;

    public NovelRecyclerAdapter(ItemClickListener clickListener, Context context, List<Novel> novels) {
        this.novels = novels;
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
        String titulo = novels.get(position).getNome();
        holder.titulo.setText(titulo);

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                novels.get(position).setChecked(isChecked));

        markChecked();
        holder.checkBox.setChecked(novels.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return novels.size();
    }

    public List<Novel> getCheckedItems() {
        List<Novel> checkedItems = new ArrayList<>();
        for (Novel novel : novels) {
            if (novel.isChecked())
                checkedItems.add(novel);
        }
        return checkedItems;
    }

    private void markChecked() {
        MangaDB mangaDB = new MangaDB(context);
//        List<Novel> novels = mangaDB.getPrediletos();
        for (Novel novel : novels) {
            for (int i = 0; i < getItemCount(); i++) {
                if (novel.getNome().equals(novels.get(i).getNome())) {
                    novels.get(i).setChecked(true);
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
