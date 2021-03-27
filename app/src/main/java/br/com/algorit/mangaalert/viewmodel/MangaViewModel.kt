package br.com.algorit.mangaalert.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.repository.MangaRepository

class MangaViewModel(application: Application) : AndroidViewModel(application) {
    private val mangaRepository: MangaRepository = MangaRepository(application)
    val allMangas: LiveData<List<Manga>> = mangaRepository.allMangas

    val allChecked: List<Manga>
        get() = mangaRepository.allChecked

    fun insert(manga: Manga) {
        mangaRepository.insert(manga)
    }

    fun updateCapitulo(manga: Manga) {
        mangaRepository.updateCapitulo(manga)
    }

    fun updateChecked(manga: Manga) {
        mangaRepository.updateChecked(manga)
    }

    fun uncheckAll() {
        mangaRepository.uncheckAll()
    }

}