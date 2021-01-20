package br.com.algorit.mangaalert.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MangaRetrofit {

    private static final String URL_BASE = "http://192.168.0.107:8080/";
    private final MangaRepository mangaRepository;

    public MangaRetrofit() {
        OkHttpClient client = configClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mangaRepository = retrofit.create(MangaRepository.class);
    }

    private OkHttpClient configClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .header("Accept-Encoding", "identity")
                            .addHeader("Connection", "close")
                            .build();
                    return chain.proceed(newRequest);
                }).build();
    }

    public MangaRepository getMangaRepository() {
        return mangaRepository;
    }
}
