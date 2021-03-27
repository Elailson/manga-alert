package br.com.algorit.mangaalert.roomdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "MANGA")
class Manga {
    @PrimaryKey
    @ColumnInfo(name = "NOME")
    var nome: String

    @ColumnInfo(name = "CAPITULO")
    var capitulo: Double? = null

    @ColumnInfo(name = "CHECKED")
    var isChecked = false

    @Ignore
    constructor(nome: String) {
        this.nome = nome
    }

    constructor(nome: String, capitulo: Double?, checked: Boolean) {
        this.nome = nome
        this.capitulo = capitulo
        isChecked = checked
    }

    operator fun compareTo(manga: Manga): Int {
        return if (isChecked && !manga.isChecked) {
            -1
        } else if (!isChecked && manga.isChecked) {
            1
        } else {
            0
        }
    }
}