package br.com.algorit.manga_alert.retrofit.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CallbackWithReturn<T> implements Callback<T> {

    private final ResponseCallback<T> callback;

    public CallbackWithReturn(ResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    @EverythingIsNonNull
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T returned = response.body();
            if (returned != null) {
                callback.success(returned);
            }
        } else {
            callback.fail("Erro ao verificar");
        }
    }

    @Override
    @EverythingIsNonNull
    public void onFailure(Call<T> call, Throwable t) {
        callback.fail("Falha: " + t.getMessage());
    }

    public interface ResponseCallback<T> {
        void success(T response);

        void fail(String fail);
    }
}
