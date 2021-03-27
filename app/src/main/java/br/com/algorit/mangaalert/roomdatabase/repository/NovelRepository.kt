package br.com.algorit.mangaalert.roomdatabase.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import br.com.algorit.mangaalert.roomdatabase.dao.NovelDao
import br.com.algorit.mangaalert.roomdatabase.database.AlertRoomDatabase.Companion.getDatabase
import br.com.algorit.mangaalert.roomdatabase.model.Novel

class NovelRepository(application: Application?) {
    private val novelDao: NovelDao
    val allNovels: LiveData<List<Novel>>

    init {
        val database = getDatabase(application!!)
        novelDao = database!!.novelDao()
        allNovels = novelDao.all
    }

    fun insert(novel: Novel) {
        if (novelDao.findByNome(novel.nome) != null)
            UpdateAsyncTask(novelDao, false).execute(novel)
        else
            InsertAsyncTask(novelDao).execute(novel)
    }

    fun updateCapitulo(novel: Novel) {
        UpdateAsyncTask(novelDao, false).execute(novel)
    }

    fun updateChecked(novel: Novel) {
        UpdateAsyncTask(novelDao, true).execute(novel)
    }

    fun uncheckAll() {
        UncheckAllAsyncTask(novelDao).execute()
    }

    private class InsertAsyncTask(private val asyncTaskDao: NovelDao) :
        AsyncTask<Novel, Void, Void>() {
        override fun doInBackground(vararg novels: Novel): Void? {
            val novel = novels[0]
            asyncTaskDao.insert(novel)
            return null
        }
    }

    private class UpdateAsyncTask(
        private val asyncTaskDao: NovelDao,
        private val updateChecked: Boolean
    ) : AsyncTask<Novel, Void, Void>() {
        override fun doInBackground(vararg novels: Novel): Void? {
            val novel = novels[0]
            if (updateChecked) {
                asyncTaskDao.updateChecked(novel.isChecked, novel.nome)
            } else {
                asyncTaskDao.updateCapitulo(novel.capitulo!!, novel.nome)
            }
            return null
        }
    }

    private class UncheckAllAsyncTask(private val asyncTaskDao: NovelDao) :
        AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg voids: Void): Void? {
            asyncTaskDao.uncheckAll()
            return null
        }
    }
}