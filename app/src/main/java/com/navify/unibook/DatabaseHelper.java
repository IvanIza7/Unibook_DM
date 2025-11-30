package com.navify.unibook;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //BASE DE DATOS
    public static final String DATABASE_NAME = "db";

    //TABLA MATERIA
    public static final String TABLA_MATERIA = "materia";
    public static final String MATERIA_ID = "id";
    public static final String MATERIA_NOMBRE = "nombre";
    public static final String MATERIA_PROFESOR = "profesor";
    public static final String MATERIA_COLOR = "color";
    public static final String MATERIA_ICONO = "icono";

    //TABLA ACTIVIDAD
    public static final String TABLA_ACTIVIDAD = "actividad";
    public static final String ACTIVIDAD_ID = "id";
    public static final String ACTIVIDAD_TITULO = "titulo";
    public static final String ACTIVIDAD_DESCRIPCION = "descripcion";
    public static final String ACTIVIDAD_FECHA_REGISTRO = "fecha_registro";
    public static final String ACTIVIDAD_PORCENTAJE = "porcentaje";
    public static final String ACTIVIDAD_FOTO_URI = "foto_uri";
    public static final String ACTIVIDAD_FECHA_FIN = "fecha_fin";
    public static final String ACTIVIDAD_MATERIA_ID = "materia_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //HABILITAR FK
        db.execSQL("PRAGMA foreign_keys=ON;");

        //CREAR TABLA MATERIA
        db.execSQL("CREATE TABLE " + TABLA_MATERIA + " (" +
                MATERIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MATERIA_NOMBRE + " TEXT NOT NULL, " +
                MATERIA_PROFESOR + " TEXT, " +
                MATERIA_COLOR + " TEXT, " +
                MATERIA_ICONO + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLA_ACTIVIDAD + " (" +
                ACTIVIDAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACTIVIDAD_TITULO + " TEXT NOT NULL, " +
                ACTIVIDAD_DESCRIPCION + " TEXT, " +
                ACTIVIDAD_FECHA_REGISTRO + " TEXT, " +
                ACTIVIDAD_PORCENTAJE + " INTEGER, " +
                ACTIVIDAD_FOTO_URI + " TEXT, " +
                ACTIVIDAD_FECHA_FIN + " TEXT, " +
                ACTIVIDAD_MATERIA_ID + " INTEGER, " +
                "FOREIGN KEY(" + ACTIVIDAD_MATERIA_ID + ") REFERENCES " + TABLA_MATERIA + "(" + MATERIA_ID + ") ON DELETE CASCADE);");
    }

    //AGREGAR MATERIA
    public long agregarMateria(String nombre, String profesor, String color, String icono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MATERIA_NOMBRE, nombre);
        cv.put(MATERIA_PROFESOR, profesor);
        cv.put(MATERIA_COLOR, color);
        cv.put(MATERIA_ICONO, icono);

        long resultado = db.insert(TABLA_MATERIA, null, cv);
        db.close();
        return resultado;
    }

    //AGREGAR ACTIVIDAD
    public long agregarActividad(String titulo, String descripcion, String fechaReg,
                                int porcentaje, String fotoUri, String fechaFin, long idMateria) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ACTIVIDAD_TITULO, titulo);
        cv.put(ACTIVIDAD_DESCRIPCION, descripcion);
        cv.put(ACTIVIDAD_FECHA_REGISTRO, fechaReg);
        cv.put(ACTIVIDAD_PORCENTAJE, porcentaje);
        cv.put(ACTIVIDAD_FOTO_URI, fotoUri);
        cv.put(ACTIVIDAD_FECHA_FIN, fechaFin);
        cv.put(ACTIVIDAD_MATERIA_ID, idMateria);

        long resultado = db.insert(TABLA_ACTIVIDAD, null, cv);
        db.close();
        return resultado;
    }

    //ELIMINAR MATERIA
    public void eliminarMateria(int idMateria) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_MATERIA, MATERIA_ID + "=?", new String[]{String.valueOf(idMateria)});
        db.close();
    }

    //ELIMINAR ACTIVIDAD
    public void eliminarActividad(int idActividad) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLA_ACTIVIDAD, ACTIVIDAD_ID + "=?", new String[]{String.valueOf(idActividad)});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        android.util.Log.w(TABLA_MATERIA, "Actualizando la base de datos. Se destruirá la información anterior");
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_ACTIVIDAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MATERIA);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
