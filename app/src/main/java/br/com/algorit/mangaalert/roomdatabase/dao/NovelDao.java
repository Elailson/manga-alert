package br.com.algorit.mangaalert.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.algorit.mangaalert.model.Novel;

@Dao
public interface NovelDao {

    @Query("SELECT * FROM NOVEL ORDER BY NOME ASC")
    LiveData<List<Novel>> getAll();

    @Insert
    void insert(Novel novel);

    @Update
    void update(Novel novel);
}
