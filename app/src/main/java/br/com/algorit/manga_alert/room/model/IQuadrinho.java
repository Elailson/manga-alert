package br.com.algorit.manga_alert.room.model;

public interface IQuadrinho {

    String getNome();

    void setNome(String nome);

    boolean isChecked();

    void setChecked(boolean checked);

    Double getCapitulo();

    void setCapitulo(Double capitulo);
}
