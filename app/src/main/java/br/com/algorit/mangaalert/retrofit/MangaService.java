package br.com.algorit.mangaalert.retrofit;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import br.com.algorit.mangaalert.roomdatabase.model.Manga;
import br.com.algorit.mangaalert.roomdatabase.model.Novel;
import br.com.algorit.mangaalert.retrofit.callback.CallbackWithReturn;
import retrofit2.Call;

public class MangaService {

    private final MangaRepository mangaRepository;

    public MangaService() {
        mangaRepository = new MangaRetrofit().getMangaRepository();
    }

    public void findAll(ResponseCallback<List<Manga>> callback) {
        WeakReference<MangaRepository> weakMangaRepository = new WeakReference<>(mangaRepository);
        new FindMangasAsyncTask(weakMangaRepository).execute(callback);
    }

    public void findAllNovel(ResponseCallback<List<Novel>> callback) {
        WeakReference<MangaRepository> weakMangaRepository = new WeakReference<>(mangaRepository);
        new FindNovelsAsyncTask(weakMangaRepository).execute(callback);
    }

    public interface ResponseCallback<T> {
        void success(T response);

        void fail(String erro);
    }

    private static class FindMangasAsyncTask extends AsyncTask<ResponseCallback<List<Manga>>, Void, Void> {

        private final WeakReference<MangaRepository> weakMangaRepository;

        FindMangasAsyncTask(WeakReference<MangaRepository> weakMangaRepository) {
            this.weakMangaRepository = weakMangaRepository;
        }

        @Override
        protected Void doInBackground(ResponseCallback<List<Manga>>... callbacks) {
            Call<List<Manga>> call = weakMangaRepository.get().findAll();
            call.enqueue(new CallbackWithReturn<>(new CallbackWithReturn.ResponseCallback<List<Manga>>() {
                @Override
                public void success(List<Manga> response) {
                    callbacks[0].success(response);
                }

                @Override
                public void fail(String fail) {
                    callbacks[0].fail(fail);
                }
            }));
            return null;
        }
    }

    private static class FindNovelsAsyncTask extends AsyncTask<ResponseCallback<List<Novel>>, Void, Void> {

        private final WeakReference<MangaRepository> weakMangaRepository;

        FindNovelsAsyncTask(WeakReference<MangaRepository> weakMangaRepository) {
            this.weakMangaRepository = weakMangaRepository;
        }

        @Override
        protected Void doInBackground(ResponseCallback<List<Novel>>... callbacks) {
            Call<List<Novel>> call = weakMangaRepository.get().findAllNovel();
            call.enqueue(new CallbackWithReturn<>(new CallbackWithReturn.ResponseCallback<List<Novel>>() {
                @Override
                public void success(List<Novel> response) {
                    callbacks[0].success(response);
                }

                @Override
                public void fail(String fail) {
                    callbacks[0].fail(fail);
                }
            }));
            return null;
        }
    }

}
