package br.com.algorit.manga_alert.room.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

public abstract class Quadrinho implements IQuadrinho {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "NOME")
    protected String nome;

    @ColumnInfo(name = "CAPITULO")
    protected Double capitulo;

    @ColumnInfo(name = "CHECKED")
    protected boolean checked;

    @NotNull
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
