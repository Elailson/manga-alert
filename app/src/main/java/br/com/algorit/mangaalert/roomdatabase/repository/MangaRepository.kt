package br.com.algorit.mangaalert.roomdatabase.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import br.com.algorit.mangaalert.roomdatabase.dao.MangaDao
import br.com.algorit.mangaalert.roomdatabase.database.AlertRoomDatabase.Companion.getDatabase
import br.com.algorit.mangaalert.roomdatabase.model.Manga

class MangaRepository(application: Application?) {

    private val mangaDao: MangaDao
    val allMangas: LiveData<List<Manga>>

    init {
        val database = getDatabase(application!!)
        mangaDao = database!!.mangaDao()
        allMangas = mangaDao.all
    }

    val allChecked: List<Manga>
        get() = mangaDao.allChecked

    fun insert(manga: Manga) {
        if (mangaDao.findByNome(manga.nome) != null)
            UpdateAsyncTask(mangaDao, false).execute(manga)
        else
            InsertAsyncTask(mangaDao).execute(manga)
    }

    fun updateCapitulo(manga: Manga) {
        UpdateAsyncTask(mangaDao, false).execute(manga)
    }

    fun updateChecked(manga: Manga) {
        UpdateAsyncTask(mangaDao, true).execute(manga)
    }

    fun uncheckAll() {
        UncheckAllAsyncTask(mangaDao).execute()
    }

    private class InsertAsyncTask(private val asyncMangaDao: MangaDao) :
        AsyncTask<Manga, Void, Void>() {
        override fun doInBackground(vararg params: Manga): Void? {
            val manga = params[0]
            asyncMangaDao.insert(manga)
            return null
        }
    }

    private class UpdateAsyncTask(private val asyncTaskDao: MangaDao,
        private val updateChecked: Boolean) : AsyncTask<Manga, Void, Void>() {
        override fun doInBackground(vararg params: Manga?): Void? {
            val manga = params[0]
            if (updateChecked) {
                asyncTaskDao.updateChecked(manga!!.isChecked, manga.nome)
            } else {
                asyncTaskDao.updateCapitulo(manga?.capitulo!!, manga.nome)
            }
            return null
        }
    }

    private class UncheckAllAsyncTask(private val asyncTaskDao: MangaDao?) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            asyncTaskDao!!.uncheckAll()
            return null
        }
    }
}