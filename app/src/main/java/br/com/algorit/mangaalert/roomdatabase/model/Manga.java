package br.com.algorit.mangaalert.roomdatabase.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "MANGA")
public class Manga {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "NOME")
    private String nome;

    @ColumnInfo(name = "CAPITULO")
    private Double capitulo;

    @ColumnInfo(name = "CHECKED")
    private boolean checked;

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(Double capitulo) {
        this.capitulo = capitulo;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
