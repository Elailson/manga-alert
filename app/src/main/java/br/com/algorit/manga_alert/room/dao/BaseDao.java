package br.com.algorit.manga_alert.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T quadrinho);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<T> quadrinhos);

}