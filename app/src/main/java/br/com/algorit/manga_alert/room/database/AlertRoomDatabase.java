package br.com.algorit.manga_alert.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.com.algorit.manga_alert.room.dao.MangaDao;
import br.com.algorit.manga_alert.room.dao.ManhuaDao;
import br.com.algorit.manga_alert.room.dao.NovelDao;
import br.com.algorit.manga_alert.room.model.Manga;
import br.com.algorit.manga_alert.room.model.Manhua;
import br.com.algorit.manga_alert.room.model.Novel;

@Database(entities = {Manga.class, Manhua.class, Novel.class}, version = 1, exportSchema = false)
public abstract class AlertRoomDatabase extends RoomDatabase {

    public abstract MangaDao mangaDao();

    public abstract ManhuaDao manhuaDao();

    public abstract NovelDao novelDao();

    private static volatile AlertRoomDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AlertRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlertRoomDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AlertRoomDatabase.class, "MANGA_ALERT_DATABASE")
                        .build();
            }
        }

        return INSTANCE;
    }

}