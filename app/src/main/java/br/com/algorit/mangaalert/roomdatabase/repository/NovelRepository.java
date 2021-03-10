package br.com.algorit.mangaalert.roomdatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.dao.NovelDao;
import br.com.algorit.mangaalert.roomdatabase.database.AlertRoomDatabase;
import br.com.algorit.mangaalert.roomdatabase.model.Novel;

public class NovelRepository {
    private final NovelDao novelDao;
    private final LiveData<List<Novel>> allNovels;

    public NovelRepository(Application application) {
        AlertRoomDatabase database = AlertRoomDatabase.getDatabase(application);
        novelDao = database.novelDao();
        allNovels = novelDao.getAll();
    }

    public LiveData<List<Novel>> getAllNovels() {
        return allNovels;
    }

    public void insert(Novel novel) {
        new InsertAsyncTask(novelDao).execute(novel);
    }

    public void updateCapitulo(Novel novel) {
        new UpdateAsyncTask(novelDao, false).execute(novel);
    }

    public void updateChecked(Novel novel) {
        new UpdateAsyncTask(novelDao, true).execute(novel);
    }

    public void uncheckAll() {
        new UncheckAllAsyncTask(novelDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Novel, Void, Void> {
        private final NovelDao asyncTaskDao;

        InsertAsyncTask(NovelDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Novel... novels) {
            Novel novel = novels[0];
            if (asyncTaskDao.findByNome(novel.getNome()) != null)
                new UpdateAsyncTask(asyncTaskDao, false).execute(novels);
            else asyncTaskDao.insert(novel);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Novel, Void, Void> {
        private final NovelDao asyncTaskDao;
        private final boolean updateChecked;

        UpdateAsyncTask(NovelDao dao, boolean updateChecked) {
            asyncTaskDao = dao;
            this.updateChecked = updateChecked;
        }

        @Override
        protected Void doInBackground(final Novel... novels) {
            Novel novel = novels[0];
            if (updateChecked) {
                asyncTaskDao.updateChecked(novel.isChecked(), novel.getNome());
            } else {
                asyncTaskDao.updateCapitulo(novel.getCapitulo(), novel.getNome());
            }
            return null;
        }
    }

    private static class UncheckAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final NovelDao asyncTaskDao;

        UncheckAllAsyncTask(NovelDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.uncheckAll();
            return null;
        }
    }
}
