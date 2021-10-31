package br.com.algorit.manga_alert.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.manga_alert.room.dao.MangaDao;
import br.com.algorit.manga_alert.room.database.AlertRoomDatabase;
import br.com.algorit.manga_alert.room.model.Manga;

public class MangaRepository {

    private final MangaDao dao;
    private final LiveData<List<Manga>> allMangas;

    public MangaRepository(Application application) {
        AlertRoomDatabase database = AlertRoomDatabase.getDatabase(application);
        dao = database.mangaDao();
        allMangas = dao.getAll();
    }

    public LiveData<List<Manga>> getAllMangas() {
        return allMangas;
    }

    public List<Manga> getAllChecked() {
        return dao.getAllChecked();
    }

    public void insert(Manga manga) {
        AlertRoomDatabase.databaseWriteExecutor
                .execute(() -> dao.insert(manga));
    }

    public void insertAll(List<Manga> mangas) {
        AlertRoomDatabase.databaseWriteExecutor
                .execute(() -> dao.insertAll(mangas));
    }

    public void updateCapitulo(Manga manga) {
        AlertRoomDatabase.databaseWriteExecutor
                .execute(() -> dao.updateCapitulo(manga.getCapitulo(), manga.getNome()));
    }

    public void updateChecked(Manga manga) {
        AlertRoomDatabase.databaseWriteExecutor
                .execute(() -> dao.updateChecked(manga.isChecked(), manga.getNome()));
    }

    public void uncheckAll() {
        AlertRoomDatabase.databaseWriteExecutor
                .execute(dao::uncheckAll);
    }

}