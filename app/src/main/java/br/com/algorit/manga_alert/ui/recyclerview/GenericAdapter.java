package br.com.algorit.manga_alert.ui.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import br.com.algorit.manga_alert.R;
import br.com.algorit.manga_alert.room.model.IQuadrinho;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

    private final List<IQuadrinho> items;

    public GenericAdapter(List<IQuadrinho> items) {
        this.items = items;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_lista, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if (items != null) {
            IQuadrinho quadrinho = items.get(position);
            holder.titulo.setText(quadrinho.getNome());
            holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) ->
                    quadrinho.setChecked(isChecked)));
            holder.checkBox.setChecked(quadrinho.isChecked());
        }
    }

    @Override
    public int getItemCount() {
        if (this.items != null)
            return items.size();
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final CheckBox checkBox;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);

            titulo = view.findViewById(R.id.item_lista_titulo);
            checkBox = view.findViewById(R.id.item_lista_check);
        }
    }
}
