package br.com.algorit.manga_alert.room.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.manga_alert.room.dao.ManhuaDao;
import br.com.algorit.manga_alert.room.database.AlertRoomDatabase;
import br.com.algorit.manga_alert.room.model.Manhua;

public class ManhuaRepository {
    private final ManhuaDao manhuaDao;
    private final LiveData<List<Manhua>> allManhuas;

    public ManhuaRepository(Application application) {
        AlertRoomDatabase database = AlertRoomDatabase.getDatabase(application);
        manhuaDao = database.manhuaDao();
        allManhuas = manhuaDao.getAll();
    }

    public LiveData<List<Manhua>> getAllManhuas() {
        return allManhuas;
    }

    public List<Manhua> getAllChecked() {
        return manhuaDao.getAllChecked();
    }

    public void insert(Manhua manhua) {
        new InsertAsyncTask(manhuaDao).execute(manhua);
    }

    public void updateCapitulo(Manhua manhua) {
        new ManhuaRepository.UpdateAsyncTask(manhuaDao, false).execute(manhua);
    }

    public void updateChecked(Manhua manhua) {
        new ManhuaRepository.UpdateAsyncTask(manhuaDao, true).execute(manhua);
    }

    public void uncheckAll() {
        new UncheckAllAsyncTask(manhuaDao).execute();
    }

    private static class InsertAsyncTask extends AsyncTask<Manhua, Void, Void> {
        private final ManhuaDao asyncManhuaDao;

        InsertAsyncTask(ManhuaDao manhuaDao) {
            this.asyncManhuaDao = manhuaDao;
        }

        @Override
        protected Void doInBackground(Manhua... manhuas) {
            Manhua manhua = manhuas[0];
            if (asyncManhuaDao.findByNome(manhua.getNome()) != null)
                new UpdateAsyncTask(asyncManhuaDao, false).execute(manhua);
            else asyncManhuaDao.insert(manhua);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Manhua, Void, Void> {
        private final ManhuaDao asyncTaskDao;
        private final boolean updateChecked;

        UpdateAsyncTask(ManhuaDao dao, boolean updateChecked) {
            asyncTaskDao = dao;
            this.updateChecked = updateChecked;
        }

        @Override
        protected Void doInBackground(final Manhua... manhuas) {
            Manhua manhua = manhuas[0];
            if (manhua.getCapitulo() != null) {
                if (updateChecked) {
                    asyncTaskDao.updateChecked(manhua.isChecked(), manhua.getNome());
                } else {
                    asyncTaskDao.updateCapitulo(manhua.getCapitulo(), manhua.getNome());
                }
            }
            return null;
        }
    }

    private static class UncheckAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final ManhuaDao asyncTaskDao;

        UncheckAllAsyncTask(ManhuaDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.uncheckAll();
            return null;
        }
    }
}
