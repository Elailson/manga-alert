package br.com.algorit.mangaalert.retrofit.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.internal.EverythingIsNonNull

class CallbackWithReturn<T>(private val callback: ResponseCallback<T>) : Callback<T> {
    @EverythingIsNonNull
    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val returned = response.body()
            if (returned != null) {
                callback.success(returned)
            }
        } else {
            callback.fail("Erro ao verificar")
        }
    }

    @EverythingIsNonNull
    override fun onFailure(call: Call<T>, t: Throwable) {
        callback.fail("Falha: " + t.message)
    }

    interface ResponseCallback<T> {
        fun success(response: T)
        fun fail(fail: String)
    }
}