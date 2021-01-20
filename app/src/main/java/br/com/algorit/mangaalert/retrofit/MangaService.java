package br.com.algorit.mangaalert.retrofit;

import java.util.List;

import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.retrofit.callback.CallbackWithReturn;
import retrofit2.Call;

public class MangaService {

    private final MangaRepository mangaRepository;

    public MangaService() {
        mangaRepository = new MangaRetrofit().getMangaRepository();
    }

    public void findAll(ResponseCallback<List<Manga>> callback) {
        Call<List<Manga>> call = mangaRepository.findAll();
        call.enqueue(new CallbackWithReturn<>(new CallbackWithReturn.ResponseCallback<List<Manga>>() {
            @Override
            public void success(List<Manga> response) {
                callback.success(response);
            }

            @Override
            public void fail(String fail) {
                callback.fail(fail);
            }
        }));
    }

    public interface ResponseCallback<T> {
        void success(T response);

        void fail(String erro);
    }
}
