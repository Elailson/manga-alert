package br.com.algorit.mangaalert.roomdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.algorit.mangaalert.roomdatabase.model.Novel

@Dao
interface NovelDao {
    @get:Query("SELECT * FROM NOVEL ORDER BY NOME ASC")
    val all: LiveData<List<Novel>>

    @Query("SELECT NOME FROM NOVEL WHERE NOME = :nome")
    fun findByNome(nome: String): String

    @Insert
    fun insert(novel: Novel)

    @Query("UPDATE NOVEL SET CAPITULO = :capitulo WHERE NOME = :nome")
    fun updateCapitulo(capitulo: Double, nome: String)

    @Query("UPDATE NOVEL SET CHECKED = :checked WHERE NOME = :nome")
    fun updateChecked(checked: Boolean, nome: String)

    @Query("UPDATE NOVEL SET CHECKED = 0 WHERE CHECKED = 1")
    fun uncheckAll()
}