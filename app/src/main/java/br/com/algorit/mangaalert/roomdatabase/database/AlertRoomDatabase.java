package br.com.algorit.mangaalert.roomdatabase.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.algorit.mangaalert.roomdatabase.dao.MangaDao;
import br.com.algorit.mangaalert.roomdatabase.dao.NovelDao;
import br.com.algorit.mangaalert.roomdatabase.model.Manga;
import br.com.algorit.mangaalert.roomdatabase.model.Novel;

@Database(entities = {Manga.class, Novel.class}, version = 1, exportSchema = false)
public abstract class AlertRoomDatabase extends RoomDatabase {

    private static AlertRoomDatabase INSTANCE;

    public static AlertRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AlertRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AlertRoomDatabase.class, "MANGA_ALERT_DATABASE")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract NovelDao novelDao();

    public abstract MangaDao mangaDao();
}
