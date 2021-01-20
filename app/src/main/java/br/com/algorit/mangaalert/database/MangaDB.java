package br.com.algorit.mangaalert.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.com.algorit.mangaalert.model.Manga;

public class MangaDB extends SQLiteOpenHelper {

    private static final String PREDILETO = "PREDILETO";
    private static final String MANGA = "MANGA";

    public MangaDB(Context context) {
        super(context, "manga.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tablePredileto = "CREATE TABLE PREDILETO (NOME TEXT, CHECKED INTEGER)";

        String tableManga = "CREATE TABLE MANGA (NOME TEXT, CAPITULO REAL)";
        String tableNovel = "CREATE TABLE NOVEL (NOME TEXT, CAPITULO REAL)";

        db.execSQL(tablePredileto);
        db.execSQL(tableManga);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertPrediletos(List<Manga> mangas) {
        recriaPredileto();
        SQLiteDatabase database = getWritableDatabase();
        for (Manga manga : mangas) {
            ContentValues values = getDataPredileto(manga.getNome());
            database.insert(PREDILETO, null, values);
        }
    }

    public void insertManga(List<Manga> mangas) {
        recriaManga();
        SQLiteDatabase database = getWritableDatabase();
        for (Manga manga : mangas) {
            ContentValues values = getDataManga(manga.getNome(), manga.getCapitulo());
            database.insert(MANGA, null, values);
        }
    }

    public void insertMangaNome(List<Manga> mangas) {
        recriaManga();
        SQLiteDatabase database = getWritableDatabase();
        for (Manga manga : mangas) {
            ContentValues values = getNomeManga(manga.getNome());
            database.insert(MANGA, null, values);
        }
    }

    public List<Manga> getPrediletos() {
        String select = "SELECT NOME, CHECKED FROM PREDILETO";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(select, null);

        List<Manga> prediletos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Manga manga = new Manga();
            manga.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            int checked = cursor.getInt(cursor.getColumnIndex("CHECKED"));
            manga.setChecked(checked == 1);

            prediletos.add(manga);
        }

        cursor.close();
        return prediletos;
    }

    public List<Manga> getMangasNotificados() {
        String select = "SELECT NOME, CAPITULO FROM MANGA";
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(select, null);

        List<Manga> mangas = new ArrayList<>();
        while (cursor.moveToNext()) {
            Manga manga = new Manga();
            manga.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
            manga.setCapitulo(cursor.getDouble(cursor.getColumnIndex("CAPITULO")));

            mangas.add(manga);
        }

        cursor.close();
        return mangas;
    }

    private void recriaPredileto() {
        SQLiteDatabase database = getWritableDatabase();
        String tableManga = "CREATE TABLE PREDILETO (NOME TEXT, CHECKED INTEGER)";
        String deleteTable = "DROP TABLE IF EXISTS PREDILETO";

        database.execSQL(deleteTable);
        database.execSQL(tableManga);
    }

    private void recriaManga() {
        SQLiteDatabase database = getWritableDatabase();
        String tableManga = "CREATE TABLE MANGA (NOME TEXT, CAPITULO REAL)";
        String deleteTable = "DROP TABLE IF EXISTS MANGA";

        database.execSQL(deleteTable);
        database.execSQL(tableManga);
    }

    private ContentValues getDataPredileto(String nome) {
        ContentValues values = new ContentValues();
        values.put("NOME", nome);
        values.put("CHECKED", 1);

        return values;
    }

    private ContentValues getDataManga(String nome, Double capitulo) {
        ContentValues values = new ContentValues();
        values.put("NOME", nome);
        values.put("CAPITULO", capitulo);

        return values;
    }

    private ContentValues getNomeManga(String nome) {
        ContentValues values = new ContentValues();
        values.put("NOME", nome);
        values.put("CAPITULO", 0);

        return values;
    }
}
