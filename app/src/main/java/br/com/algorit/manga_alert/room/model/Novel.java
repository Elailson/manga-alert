package br.com.algorit.manga_alert.room.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "NOVEL")
public class Novel extends Quadrinho {

    public Novel() {
        this.setNome("");
    }

    @Ignore
    public Novel(@NonNull String nome) {
        this.setNome(nome);
    }

    public Novel(@NonNull String nome, Double capitulo, boolean checked) {
        this.nome = nome;
        this.capitulo = capitulo;
        this.checked = checked;
    }

    public int compareTo(Novel novel) {
        if (this.isChecked() && !novel.isChecked()) {
            return -1;
        } else if (!this.isChecked() && novel.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }
}
