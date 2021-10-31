package br.com.algorit.manga_alert.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.repository.MangaRepository;

public class MangaViewModel extends AndroidViewModel {

    private LiveData<List<Manga>> mangas;
    private final MangaRepository repository;

    public MangaViewModel(@NonNull Application application) {
        super(application);

        this.repository = new MangaRepository(application);
        this.mangas = repository.getAllMangas();
    }

    public LiveData<List<Manga>> getMangas() {
        if (mangas == null) {
            mangas = new MutableLiveData<>();
            loadMangas();
        }
        return mangas;
    }

    public void loadMangas() {
        mangas = repository.getAllMangas();
    }

    public List<Manga> getAllChecked() {
        return repository.getAllChecked();
    }

    public void insert(Manga manga) {
        repository.insert(manga);
    }

    public void insertAll(List<Manga> mangas) {
        repository.insertAll(mangas);
    }

    public void updateCapitulo(Manga manga) {
        repository.updateCapitulo(manga);
    }

    public void updateChecked(Manga manga) {
        repository.updateChecked(manga);
    }

    public void uncheckAll() {
        repository.uncheckAll();
    }
}
