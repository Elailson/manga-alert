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
import br.com.algorit.mangaalert.roomdatabase.model.Novel;

public class NovelRecyclerAdapter extends RecyclerView.Adapter<NovelRecyclerAdapter.MyViewHolder> {

    private final ItemClickListener clickListener;
    private final LayoutInflater layoutInflater;
    private List<Novel> novels;

    public NovelRecyclerAdapter(ItemClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (novels != null) {
            Novel novel = novels.get(position);
            holder.titulo.setText(novel.getNome());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    novel.setChecked(isChecked));

            holder.checkBox.setChecked(novel.isChecked());
        }
    }

    public void setNovels(List<Novel> novels) {
        this.novels = novels;
        if (novels != null) {
            Collections.sort(this.novels, Novel::compareTo);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.novels != null)
            return novels.size();
        else return 0;
    }

    public List<Novel> getCheckedItems() {
        List<Novel> checkedItems = new ArrayList<>();
        for (Novel novel : novels) {
            if (novel.isChecked())
                checkedItems.add(novel);
        }
        return checkedItems;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView titulo;
        private ItemClickListener clickListener;

        MyViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_lista_check);
            titulo = itemView.findViewById(R.id.item_lista_titulo);

//            itemView.setOnClickListener(view -> clickListener.onItemClick(view, getAdapterPosition()));
        }
    }
}
