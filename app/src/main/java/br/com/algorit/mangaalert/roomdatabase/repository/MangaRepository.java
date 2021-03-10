package br.com.algorit.mangaalert.roomdatabase.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.dao.MangaDao;
import br.com.algorit.mangaalert.roomdatabase.database.AlertRoomDatabase;
import br.com.algorit.mangaalert.roomdatabase.model.Manga;

public class MangaRepository {
    private final MangaDao mangaDao;
    private final LiveData<List<Manga>> allMangas;

    public MangaRepository(Application application) {
        AlertRoomDatabase database = AlertRoomDatabase.getDatabase(application);
        mangaDao = database.mangaDao();
        allMangas = mangaDao.getAll();
    }

    public LiveData<List<Manga>> getAllMangas() {
        return allMangas;
    }

    public List<Manga> getAllChecked() {
        return mangaDao.getAllChecked();
    }

    public void insert(Manga manga) {
        new InsertAsyncTask(mangaDao).execute(manga);
    }

    public void updateCapitulo(Manga manga) {
        new MangaRepository.UpdateAsyncTask(mangaDao, false).execute(manga);
    }

    public void updateChecked(Manga manga) {
        new MangaRepository.UpdateAsyncTask(mangaDao, true).execute(manga);
    }

    public void uncheckAll() {
        new UncheckAllAsyncTask(mangaDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Manga, Void, Void> {
        private final MangaDao asyncMangaDao;

        InsertAsyncTask(MangaDao mangaDao) {
            this.asyncMangaDao = mangaDao;
        }

        @Override
        protected Void doInBackground(Manga... mangas) {
            Manga manga = mangas[0];
            if (asyncMangaDao.findByNome(manga.getNome()) != null)
                new UpdateAsyncTask(asyncMangaDao, false).execute(mangas);
            else asyncMangaDao.insert(manga);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Manga, Void, Void> {
        private final MangaDao asyncTaskDao;
        private final boolean updateChecked;

        UpdateAsyncTask(MangaDao dao, boolean updateChecked) {
            asyncTaskDao = dao;
            this.updateChecked = updateChecked;
        }

        @Override
        protected Void doInBackground(final Manga... mangas) {
            Manga manga = mangas[0];
            if (updateChecked) {
                asyncTaskDao.updateChecked(manga.isChecked(), manga.getNome());
            } else {
                asyncTaskDao.updateCapitulo(manga.getCapitulo(), manga.getNome());
            }
            return null;
        }
    }

    private static class UncheckAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final MangaDao asyncTaskDao;

        UncheckAllAsyncTask(MangaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.uncheckAll();
            return null;
        }
    }
}
