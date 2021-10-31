package br.com.algorit.manga_alert.room.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "MANHUA")
public class Manhua extends Quadrinho {

    public Manhua() {
        this.setNome("");
    }

    @Ignore
    public Manhua(@NonNull String nome) {
        this.setNome(nome);
    }

    public Manhua(@NonNull String nome, Double capitulo, boolean checked) {
        this.nome = nome;
        this.capitulo = capitulo;
        this.checked = checked;
    }

    public int compareTo(Manhua manhua) {
        if (this.isChecked() && !manhua.isChecked()) {
            return -1;
        } else if (!this.isChecked() && manhua.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }
}
