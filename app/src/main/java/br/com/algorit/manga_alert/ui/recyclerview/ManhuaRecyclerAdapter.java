package br.com.algorit.manga_alert.ui.recyclerview;

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

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.Manhua;

public class ManhuaRecyclerAdapter extends RecyclerView.Adapter<ManhuaRecyclerAdapter.MyViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<Manhua> listaManhuas;

    public ManhuaRecyclerAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_lista, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (listaManhuas != null) {
            Manhua manhua = listaManhuas.get(position);
            holder.titulo.setText(manhua.getNome());
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    manhua.setChecked(isChecked));

            holder.checkBox.setChecked(manhua.isChecked());
        }
    }

    public void filtrar(List<Manhua> manhuasFiltrados) {
        listaManhuas = new ArrayList<>();
        listaManhuas.addAll(manhuasFiltrados);
        notifyDataSetChanged();
    }

    public void setManhuas(List<Manhua> manhuas) {
        this.listaManhuas = manhuas;
        if (listaManhuas != null) {
            Collections.sort(this.listaManhuas, Manhua::compareTo);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (this.listaManhuas != null)
            return listaManhuas.size();
        else return 0;
    }

    public List<Manhua> getCheckedItems() {
        List<Manhua> checkedItems = new ArrayList<>();
        for (Manhua manhua : listaManhuas) {
            if (manhua.isChecked())
                checkedItems.add(manhua);
        }
        return checkedItems;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;
        private final TextView titulo;

        MyViewHolder(View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_lista_check);
            titulo = itemView.findViewById(R.id.item_lista_titulo);
        }
    }
}
