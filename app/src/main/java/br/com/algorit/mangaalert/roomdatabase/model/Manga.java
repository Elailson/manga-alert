package br.com.algorit.mangaalert.model;

public class Manga extends Quadrinho {

    public Manga() {
    }

    public Manga(String nome) {
        setNome(nome);
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
