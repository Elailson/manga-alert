package br.com.algorit.mangaalert.retrofit

import android.os.AsyncTask
import br.com.algorit.mangaalert.retrofit.callback.CallbackWithReturn
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.model.Novel
import java.lang.ref.WeakReference

class MangaService {
    private val mangaRepository: MangaRepository = MangaRetrofit().mangaRepository

    fun findAll(callback: ResponseCallback<List<Manga>>) {
        val weakMangaRepository = WeakReference(mangaRepository)
        FindMangasAsyncTask(weakMangaRepository).execute(callback)
    }

    fun findAllNovel(callback: ResponseCallback<List<Novel>>) {
        val weakMangaRepository = WeakReference(mangaRepository)
        FindNovelsAsyncTask(weakMangaRepository).execute(callback)
    }

    interface ResponseCallback<T> {
        fun success(response: T)
        fun fail(erro: String)
    }

    private class FindMangasAsyncTask(private val weakMangaRepository: WeakReference<MangaRepository>) :
        AsyncTask<ResponseCallback<List<Manga>>, Void, Void>() {
        override fun doInBackground(vararg params: ResponseCallback<List<Manga>>): Void? {
            val call = weakMangaRepository.get()!!.findAll()
            call.enqueue(CallbackWithReturn(object :
                CallbackWithReturn.ResponseCallback<List<Manga>> {
                override fun success(response: List<Manga>) {
                    params[0].success(response)
                }

                override fun fail(fail: String) {
                    params[0].fail(fail)
                }
            }))
            return null
        }
    }

    private class FindNovelsAsyncTask(private val weakMangaRepository: WeakReference<MangaRepository>) :
        AsyncTask<ResponseCallback<List<Novel>>, Void, Void>() {
        override fun doInBackground(vararg params: ResponseCallback<List<Novel>>): Void? {
            val call = weakMangaRepository.get()!!.findAllNovel()
            call.enqueue(CallbackWithReturn(object :
                CallbackWithReturn.ResponseCallback<List<Novel>> {
                override fun success(response: List<Novel>) {
                    params[0].success(response)
                }

                override fun fail(fail: String) {
                    params[0].fail(fail)
                }
            }))
            return null
        }
    }
}