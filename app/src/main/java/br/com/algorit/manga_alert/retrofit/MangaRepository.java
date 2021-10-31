package br.com.algorit.manga_alert.retrofit;

import java.util.List;

import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.model.Novel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MangaRepository {

    @GET("manga/find-all")
    Call<List<Manga>> findAll();

    @GET("novel/find-all")
    Call<List<Novel>> findAllNovel();
}
