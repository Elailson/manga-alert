package br.com.algorit.mangaalert.roomdatabase.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.model.Novel;

@Dao
public interface NovelDao {

    @Query("SELECT * FROM NOVEL ORDER BY NOME ASC")
    LiveData<List<Novel>> getAll();

    @Query("SELECT NOME FROM NOVEL WHERE NOME = :nome")
    String findByNome(String nome);

    @Insert
    void insert(Novel novel);

    @Query("UPDATE NOVEL SET CAPITULO = :capitulo WHERE NOME = :nome")
    void updateCapitulo(double capitulo, String nome);

    @Query("UPDATE NOVEL SET CHECKED = :checked WHERE NOME = :nome")
    void updateChecked(boolean checked, String nome);

    @Query("UPDATE NOVEL SET CHECKED = 0 WHERE CHECKED = 1")
    void uncheckAll();
}
