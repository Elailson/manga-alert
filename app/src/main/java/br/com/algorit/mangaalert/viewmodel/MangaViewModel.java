package br.com.algorit.mangaalert.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.model.Manga;
import br.com.algorit.mangaalert.roomdatabase.repository.MangaRepository;

public class MangaViewModel extends AndroidViewModel {
    private final MangaRepository mangaRepository;
    private final LiveData<List<Manga>> allMangas;

    public MangaViewModel(@NonNull Application application) {
        super(application);
        this.mangaRepository = new MangaRepository(application);
        this.allMangas = mangaRepository.getAllMangas();
    }

    public LiveData<List<Manga>> getAllMangas() {
        return allMangas;
    }

    public List<Manga> getAllChecked() {
        return mangaRepository.getAllChecked();
    }

    public void insert(Manga manga) {
        mangaRepository.insert(manga);
    }

    public void updateCapitulo(Manga manga) {
        mangaRepository.updateCapitulo(manga);
    }

    public void updateChecked(Manga manga) {
        mangaRepository.updateChecked(manga);
    }

    public void uncheckAll() {
        mangaRepository.uncheckAll();
    }
}
