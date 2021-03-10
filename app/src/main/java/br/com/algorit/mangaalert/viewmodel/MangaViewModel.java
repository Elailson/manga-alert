package br.com.algorit.mangaalert.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.model.Novel;
import br.com.algorit.mangaalert.roomdatabase.repository.NovelRepository;

public class NovelViewModel extends AndroidViewModel {
    private final NovelRepository novelRepository;
    private final LiveData<List<Novel>> allNovels;

    public NovelViewModel(@NonNull Application application) {
        super(application);
        this.novelRepository = new NovelRepository(application);
        this.allNovels = novelRepository.getAllNovels();
    }

    public LiveData<List<Novel>> getAllNovels() {
        return allNovels;
    }

    public void insert(Novel novel) {
        novelRepository.insert(novel);
    }

    public void updateCapitulo(Novel novel) {
        novelRepository.updateCapitulo(novel);
    }

    public void updateChecked(Novel novel) {
        novelRepository.updateChecked(novel);
    }
}
