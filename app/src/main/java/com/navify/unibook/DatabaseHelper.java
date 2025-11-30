package com.navify.unibook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //BASE DE DATOS
    public static final String DATABASE_NAME = "db";
    private static final int DATABASE_VERSION = 1;

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
    public static final String ACTIVIDAD_FECHA_FIN = "fecha_fin"; // Formato "YYYY-MM-DD HH:MM"
    public static final String ACTIVIDAD_MATERIA_ID = "materia_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        String crearTablaMateria = "CREATE TABLE " + TABLA_MATERIA + " (" +
                MATERIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MATERIA_NOMBRE + " TEXT NOT NULL, " +
                MATERIA_PROFESOR + " TEXT, " +
                MATERIA_COLOR + " TEXT, " +
                MATERIA_ICONO + " TEXT);";
        db.execSQL(crearTablaMateria);

        String crearTablaActividad = "CREATE TABLE " + TABLA_ACTIVIDAD + " (" +
                ACTIVIDAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ACTIVIDAD_TITULO + " TEXT NOT NULL, " +
                ACTIVIDAD_DESCRIPCION + " TEXT, " +
                ACTIVIDAD_FECHA_REGISTRO + " TEXT, " +
                ACTIVIDAD_PORCENTAJE + " INTEGER, " +
                ACTIVIDAD_FOTO_URI + " TEXT, " +
                ACTIVIDAD_FECHA_FIN + " TEXT, " +
                ACTIVIDAD_MATERIA_ID + " INTEGER, " +
                "FOREIGN KEY(" + ACTIVIDAD_MATERIA_ID + ") REFERENCES " + TABLA_MATERIA + "(" + MATERIA_ID + ") ON DELETE CASCADE);";
        db.execSQL(crearTablaActividad);
    }

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

    public List<String> getNombresDeTodasLasMaterias() {
        List<String> materias = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLA_MATERIA, new String[]{MATERIA_NOMBRE}, null, null, null, null, MATERIA_NOMBRE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                materias.add(cursor.getString(cursor.getColumnIndexOrThrow(MATERIA_NOMBRE)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return materias;
    }

    public List<Actividad> getTodasLasActividades() {
        List<Actividad> actividades = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT a." + ACTIVIDAD_ID + ", a." + ACTIVIDAD_TITULO + ", a." + ACTIVIDAD_FECHA_FIN + ", " +
                "m." + MATERIA_NOMBRE + ", m." + MATERIA_COLOR + ", m." + MATERIA_ICONO + " " +
                "FROM " + TABLA_ACTIVIDAD + " a " +
                "JOIN " + TABLA_MATERIA + " m ON a." + ACTIVIDAD_MATERIA_ID + " = m." + MATERIA_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(ACTIVIDAD_ID));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow(ACTIVIDAD_TITULO));
                String materiaNombre = cursor.getString(cursor.getColumnIndexOrThrow(MATERIA_NOMBRE));
                String materiaColor = cursor.getString(cursor.getColumnIndexOrThrow(MATERIA_COLOR));
                String materiaIcono = cursor.getString(cursor.getColumnIndexOrThrow(MATERIA_ICONO));
                String fechaFinCompleta = cursor.getString(cursor.getColumnIndexOrThrow(ACTIVIDAD_FECHA_FIN));

                String fecha = "";
                String hora = "";

                if (fechaFinCompleta != null && !fechaFinCompleta.isEmpty()) {
                    String[] parts = fechaFinCompleta.split(" ", 2);
                    fecha = parts[0];
                    if (parts.length > 1) {
                        hora = parts[1];
                    }
                }

                actividades.add(new Actividad(id, nombre, materiaNombre, materiaColor, materiaIcono, fecha, hora));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return actividades;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
