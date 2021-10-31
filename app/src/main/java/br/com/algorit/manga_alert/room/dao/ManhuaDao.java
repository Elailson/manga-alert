package br.com.algorit.manga_alert.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manhua;

@Dao
public interface ManhuaDao extends BaseDao<Manhua> {

    @Query("SELECT * FROM MANHUA ORDER BY NOME ASC")
    LiveData<List<Manhua>> getAll();

    @Query("SELECT * FROM MANHUA WHERE CHECKED = 1 ORDER BY NOME ASC")
    List<Manhua> getAllChecked();

    @Query("SELECT NOME FROM MANHUA WHERE NOME = :nome")
    String findByNome(String nome);

    @Query("UPDATE MANHUA SET CAPITULO = :capitulo WHERE NOME = :nome")
    void updateCapitulo(double capitulo, String nome);

    @Query("UPDATE MANHUA SET CHECKED = :checked WHERE NOME = :nome")
    void updateChecked(boolean checked, String nome);

    @Query("UPDATE MANHUA SET CHECKED = 0 WHERE CHECKED = 1")
    void uncheckAll();
}
