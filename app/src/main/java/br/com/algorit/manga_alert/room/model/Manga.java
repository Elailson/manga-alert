package br.com.algorit.manga_alert.room.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "MANGA")
public class Manga extends Quadrinho {

    public Manga() {
        this.setNome("");
    }

    @Ignore
    public Manga(@NonNull String nome) {
        this.setNome(nome);
    }

    public Manga(@NonNull String nome, Double capitulo, boolean checked) {
        this.nome = nome;
        this.capitulo = capitulo;
        this.checked = checked;
    }

    public int compareTo(Manga manga) {
        if (this.isChecked() && !manga.isChecked()) {
            return -1;
        } else if (!this.isChecked() && manga.isChecked()) {
            return 1;
        } else {
            return 0;
        }
    }

}