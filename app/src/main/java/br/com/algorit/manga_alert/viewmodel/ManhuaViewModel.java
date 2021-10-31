package br.com.algorit.manga_alert.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.room.repository.ManhuaRepository;

public class ManhuaViewModel extends AndroidViewModel {
    private final ManhuaRepository manhuaRepository;
    private final LiveData<List<Manhua>> allManhuas;

    public ManhuaViewModel(@NonNull Application application) {
        super(application);
        this.manhuaRepository = new ManhuaRepository(application);
        this.allManhuas = manhuaRepository.getAllManhuas();
    }

    public LiveData<List<Manhua>> getAllManhuas() {
        return allManhuas;
    }

    public List<Manhua> getAllChecked() {
        return manhuaRepository.getAllChecked();
    }

    public void insert(Manhua manhua) {
        manhuaRepository.insert(manhua);
    }

    public void updateCapitulo(Manhua manhua) {
        manhuaRepository.updateCapitulo(manhua);
    }

    public void updateChecked(Manhua manhua) {
        manhuaRepository.updateChecked(manhua);
    }

    public void uncheckAll() {
        manhuaRepository.uncheckAll();
    }
}
