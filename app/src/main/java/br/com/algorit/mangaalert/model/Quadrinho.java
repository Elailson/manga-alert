package br.com.algorit.mangaalert.model;

public abstract class Quadrinho {

    private String nome;

    private Double capitulo;

    private boolean checked;

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
