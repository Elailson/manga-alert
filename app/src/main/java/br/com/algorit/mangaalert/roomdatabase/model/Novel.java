package br.com.algorit.mangaalert.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "NOVEL")
public class Novel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "NOME")
    private String nome;

    @ColumnInfo(name = "CAPITULO")
    private Double capitulo;

    @ColumnInfo(name = "CHECKED")
    private boolean checked;

    @Ignore
    public Novel(@NonNull String nome) {
        this.setNome(nome);
    }

    public Novel(@NonNull String nome, Double capitulo, boolean checked) {
        this.nome = nome;
        this.capitulo = capitulo;
        this.checked = checked;
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
