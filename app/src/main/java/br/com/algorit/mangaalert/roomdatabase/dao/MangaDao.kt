package br.com.algorit.mangaalert.roomdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.algorit.mangaalert.roomdatabase.model.Manga

@Dao
interface MangaDao {
    @get:Query("SELECT * FROM MANGA ORDER BY NOME ASC")
    val all: LiveData<List<Manga>>

    @get:Query("SELECT * FROM MANGA WHERE CHECKED = 1 ORDER BY NOME ASC")
    val allChecked: List<Manga>

    @Query("SELECT NOME FROM MANGA WHERE NOME = :nome")
    fun findByNome(nome: String): String

    @Insert
    fun insert(manga: Manga)

    @Query("UPDATE MANGA SET CAPITULO = :capitulo WHERE NOME = :nome")
    fun updateCapitulo(capitulo: Double, nome: String)

    @Query("UPDATE MANGA SET CHECKED = :checked WHERE NOME = :nome")
    fun updateChecked(checked: Boolean, nome: String)

    @Query("UPDATE MANGA SET CHECKED = 0 WHERE CHECKED = 1")
    fun uncheckAll()
}