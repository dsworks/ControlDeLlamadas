package com.dsole.controldellamadas.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dsole on 28/01/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Versión de la base de datos
    private static final int DATABASE_VERSION = 1;
    // Nombre de la base de datos
    private static final String DATABASE_NAME = "ControlLlamadas";
    private static final String TABLA_AVISOS = "aviso";
    private static final String TABLA_AVISOS_CAMPO = "preferenciasModificadas";

    private static final String[] COLUMNS = {TABLA_AVISOS_CAMPO};


    public MySQLiteHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Sentencia SQL de creación de la tabla peliculas
        String CREATE_TABLA_AVISOS = "CREATE TABLE " + TABLA_AVISOS + " ( " + TABLA_AVISOS_CAMPO + " INTEGER)";

        db.execSQL(CREATE_TABLA_AVISOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Borrar tablas antiguas si existen
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_AVISOS);

        // Crear tabla peliculas nueva
        this.onCreate(db);
    }

    public void actualizarAvisoPreferencias(int aviso) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TABLA_AVISOS_CAMPO, aviso);

        db.update(TABLA_AVISOS, cv, null, null);

        db.close();
    }

    public void insertaAviso() {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(TABLA_AVISOS_CAMPO, 0);

        db.insert(TABLA_AVISOS, null, cv);

        db.close();
    }

    public int getPreferenciasModificadas() {
        //devuelve 0 si no hay registro
        //devuelve 1 si hay registro pero es 0
        //devuelve 2 si hay registro y es 1

        int ret = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLA_AVISOS, COLUMNS, null, null, null, null, null);

        if (c != null) {
            c.moveToFirst();

            while (!c.isAfterLast()) {
                ret = 1;
                int res = c.getInt(0);

                if(res == 1) ret = 2;

                c.moveToNext();
            }
            c.close();
        }

        db.close();

        return ret;
    }
}
