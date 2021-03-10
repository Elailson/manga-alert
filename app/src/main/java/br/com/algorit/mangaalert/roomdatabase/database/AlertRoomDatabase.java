package br.com.algorit.mangaalert.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.algorit.mangaalert.dao.NovelDao;
import br.com.algorit.mangaalert.model.Novel;

@Database(entities = {Novel.class}, version = 1, exportSchema = false)
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
}
