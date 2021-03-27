package br.com.algorit.mangaalert.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MangaRetrofit {
    val mangaRepository: MangaRepository

    private fun configClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor { chain: Interceptor.Chain ->
                    val newRequest = chain.request().newBuilder()
                            .header("Accept-Encoding", "identity")
                            .addHeader("Connection", "close")
                            .build()
                    chain.proceed(newRequest)
                }.build()
    }

    companion object {
        private const val URL_BASE = "http://192.168.0.107:8080/api/"
    }

    init {
        val client = configClient()
        val retrofit = Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        mangaRepository = retrofit.create(MangaRepository::class.java)
    }
}