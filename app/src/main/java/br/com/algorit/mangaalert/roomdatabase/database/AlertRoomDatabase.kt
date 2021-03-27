package br.com.algorit.mangaalert.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.algorit.mangaalert.roomdatabase.dao.MangaDao
import br.com.algorit.mangaalert.roomdatabase.dao.NovelDao
import br.com.algorit.mangaalert.roomdatabase.model.Manga
import br.com.algorit.mangaalert.roomdatabase.model.Novel

@Database(entities = [Manga::class, Novel::class], version = 1, exportSchema = false)
abstract class AlertRoomDatabase : RoomDatabase() {
    abstract fun novelDao(): NovelDao
    abstract fun mangaDao(): MangaDao

    companion object {
        private var INSTANCE: AlertRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): AlertRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(AlertRoomDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AlertRoomDatabase::class.java, "MANGA_ALERT_DATABASE")
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}