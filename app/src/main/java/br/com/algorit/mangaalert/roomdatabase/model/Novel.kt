package br.com.algorit.mangaalert.roomdatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "NOVEL")
class Novel {
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

    operator fun compareTo(novel: Novel): Int {
        return if (isChecked && !novel.isChecked) {
            -1
        } else if (!isChecked && novel.isChecked) {
            1
        } else {
            0
        }
    }
}