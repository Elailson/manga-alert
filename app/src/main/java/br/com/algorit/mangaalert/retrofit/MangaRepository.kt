package br.com.algorit.mangaalert.retrofit

import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import retrofit2.Call
import retrofit2.http.GET

interface MangaRepository {
    @GET("manga/find-all")
    fun findAll(): Call<List<Manga>>

    @GET("novel/find-all")
    fun findAllNovel(): Call<List<Novel>>
}