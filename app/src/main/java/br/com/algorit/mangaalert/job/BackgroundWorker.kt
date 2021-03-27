package br.com.algorit.mangaalert.job

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.com.algorit.mangaalert.R
import br.com.algorit.mangaalert.retrofit.MangaService
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.repository.MangaRepository
import java.lang.ref.WeakReference
import java.util.*

class BackgroundWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    private val weakContext: WeakReference<Context?> = WeakReference(context)
    private val mangaRepository: MangaRepository = MangaRepository(context as Application)

    override fun doWork(): Result {
        return try {
            Log.i(BackgroundWorker::class.java.canonicalName,
                "============================================ VERIFICA MANGAS ============================================")
            val mangaService = MangaService()
            mangaService.findAll(object : MangaService.ResponseCallback<List<Manga>> {
                override fun success(response: List<Manga>) {
                    NotifyAsyncTask(weakContext, mangaRepository).execute(response)
                }

                override fun fail(erro: String) {
                    Log.e("BACKGROUND_WORKER", erro)
                }
            })
            Result.success()
        } catch (exception: RuntimeException) {
            Result.failure()
        }
    }

    private class VerifyAsyncTask(private val weakContext: WeakReference<Context?>,
        private val mangaRepository: MangaRepository) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Log.i(BackgroundWorker::class.java.canonicalName,
                "============================================ VERIFICA MANGAS ============================================")
            val mangaService = MangaService()
            mangaService.findAll(object : MangaService.ResponseCallback<List<Manga>> {
                override fun success(response: List<Manga>) {
                    NotifyAsyncTask(weakContext, mangaRepository).execute(response)
                }

                override fun fail(erro: String) {
                    Log.e("BACKGROUND_WORKER", erro)
                }
            })
            return null
        }
    }

    private class NotifyAsyncTask(private val weakContext: WeakReference<Context?>,
        private val mangaRepository: MangaRepository) : AsyncTask<List<Manga?>?, Void?, Void?>() {
        override fun doInBackground(mangas: Array<List<Manga?>?>?): Void? {
            val mangasPrediletos = mangaRepository.allChecked
            for (predileto in mangasPrediletos!!) {
                for (manga in mangas?.get(0)!!) {
                    if (manga?.nome == predileto!!.nome && manga.capitulo!! > predileto.capitulo!!) {
                        mangaRepository.updateCapitulo(manga)
                        val builder = NotificationCompat.Builder(weakContext.get()!!, "manga")
                            .setSmallIcon(R.drawable.ic_manga_24)
                            .setContentTitle(manga.nome)
                            .setContentText("Capitulo " + getNumber(manga.capitulo) + " já disponível")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(buildPendingIntent())
                        val notificationManager =
                            NotificationManagerCompat.from(weakContext.get()!!)
                        notificationManager.notify(idNotification, builder.build())
                    }
                }
            }
            return null
        }

        private val idNotification: Int
            get() = Random().nextInt(1000)

        private fun buildPendingIntent(): PendingIntent {
            val notificationIntent = Intent(Intent.ACTION_VIEW)
            notificationIntent.data = Uri.parse("https://mangalivre.net/")
            return PendingIntent.getActivity(weakContext.get(), 0, notificationIntent, 0)
        }

        private fun getNumber(capitulo: Double?): String {
            val value = capitulo.toString()
            if (value.endsWith("0")) {
                val intValue = capitulo!!.toInt()
                return intValue.toString()
            }
            return capitulo.toString()
        }
    }
}