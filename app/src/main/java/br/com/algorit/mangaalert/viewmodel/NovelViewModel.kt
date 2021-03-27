package br.com.algorit.mangaalert.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import br.com.algorit.mangaalert.roomdatabase.repository.NovelRepository

class NovelViewModel(application: Application) : AndroidViewModel(application) {
    private val novelRepository: NovelRepository = NovelRepository(application)
    val allNovels: LiveData<List<Novel>> = novelRepository.allNovels

    fun insert(novel: Novel) {
        novelRepository.insert(novel)
    }

    fun updateCapitulo(novel: Novel) {
        novelRepository.updateCapitulo(novel)
    }

    fun updateChecked(novel: Novel) {
        novelRepository.updateChecked(novel)
    }

    fun uncheckAll() {
        novelRepository.uncheckAll()
    }

}