package br.com.algorit.mangaalert.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.model.Manga;

@Dao
public interface MangaDao {

    @Query("SELECT * FROM MANGA ORDER BY NOME ASC")
    LiveData<List<Manga>> getAll();

    @Query("SELECT * FROM MANGA WHERE CHECKED = 1 ORDER BY NOME ASC")
    List<Manga> getAllChecked();

    @Query("SELECT NOME FROM MANGA WHERE NOME = :nome")
    String findByNome(String nome);

    @Insert
    void insert(Manga manga);

    @Query("UPDATE MANGA SET CAPITULO = :capitulo WHERE NOME = :nome")
    void updateCapitulo(double capitulo, String nome);

    @Query("UPDATE MANGA SET CHECKED = :checked WHERE NOME = :nome")
    void updateChecked(boolean checked, String nome);

    @Query("UPDATE MANGA SET CHECKED = 0 WHERE CHECKED = 1")
    void uncheckAll();
}
