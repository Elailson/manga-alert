package br.com.algorit.manga_alert.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Novel;

@Dao
public interface NovelDao extends BaseDao<Novel> {

    @Query("SELECT * FROM NOVEL ORDER BY NOME ASC")
    LiveData<List<Novel>> getAll();

    @Query("SELECT * FROM NOVEL WHERE CHECKED = 1 ORDER BY NOME ASC")
    List<Novel> getAllChecked();

    @Query("SELECT NOME FROM NOVEL WHERE NOME = :nome")
    String findByNome(String nome);

    @Query("UPDATE NOVEL SET CAPITULO = :capitulo WHERE NOME = :nome")
    void updateCapitulo(double capitulo, String nome);

    @Query("UPDATE NOVEL SET CHECKED = :checked WHERE NOME = :nome")
    void updateChecked(boolean checked, String nome);

    @Query("UPDATE NOVEL SET CHECKED = 0 WHERE CHECKED = 1")
    void uncheckAll();
}
