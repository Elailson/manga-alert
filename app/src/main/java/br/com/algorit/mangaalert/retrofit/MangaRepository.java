package br.com.algorit.mangaalert.retrofit;

import java.util.List;

import br.com.algorit.mangaalert.model.Manga;
import br.com.algorit.mangaalert.model.Novel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MangaRepository {

    @GET("manga/find-all")
    Call<List<Manga>> findAll();

    @GET("novel/find-all")
    Call<List<Novel>> findAllNovel();
}
