package com.example.vkrapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yandex.mapkit.geometry.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 4;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.copyDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Не нужно выполнять никаких операций здесь, так как база данных уже скопирована из assets
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.close();
        context.deleteDatabase(DATABASE_NAME);
        copyDatabase();
    }

    private Cursor getCursorFromSQL(String sql) {
        return this.getReadableDatabase().rawQuery(sql, null);
    }

    public List<String> getAnimalParamsByAnimalType(String animalType) {
        List<String> animalParams = new ArrayList<>();

        String sql = "SELECT animals_params.name FROM data ";
        sql += "INNER JOIN animals ON animals.id = data.id_animal ";
        sql += "INNER JOIN animals_params ON animals_params.id = data.id_param ";
        sql += "WHERE animals.name = \"" + animalType + "\";";

        Cursor cursor = getCursorFromSQL(sql);

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("name");
                animalParams.add(cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return animalParams;
    }

    public double getAnimalCoefficient(String animalType, String animalParams) {
        double coefficient = 0;

        String sql = "SELECT data.coefficient FROM data ";
        sql += "INNER JOIN animals ON animals.id = data.id_animal ";
        sql += "INNER JOIN animals_params ON animals_params.id = data.id_param ";
        sql += "WHERE animals.name = \"" + animalType + "\" and ";
        sql += "animals_params.name = \"" + animalParams + "\";";

        Cursor cursor = getCursorFromSQL(sql);
        if (cursor.moveToFirst()) {
            coefficient = cursor.getDouble(0);
        }

        cursor.close();
        this.close();

        return coefficient;
    }

    public List<String> getAllAnimalsType() {
        List<String> animalsType = new ArrayList<>();
        Cursor cursor = getCursorFromSQL("SELECT * FROM animals");

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("name");
                animalsType.add(cursor.getString(columnIndex));
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return animalsType;
    }

    public List<Point> getAllWalkPoints() {
        List<Point> walkPoints = new ArrayList<>();
        Cursor cursor = getCursorFromSQL("SELECT * FROM walk_points");

        if (cursor.moveToFirst()) {
            do {
                int latIndex = cursor.getColumnIndex("lat");
                int longIndex = cursor.getColumnIndex("long");

                Point walkPoint = new Point(cursor.getDouble(latIndex), cursor.getDouble(longIndex));
                walkPoints.add(walkPoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        this.close();

        return walkPoints;
    }

    private void copyDatabase() {
        // Путь к базе данных во внутреннем хранилище
        try {
            String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();

            // Проверяем, существует ли уже база данных во внутреннем хранилище
            if (!isDatabaseExists(dbPath)) {
                // Открываем поток для копирования базы данных из assets во внутреннее хранилище
                InputStream inputStream = context.getAssets().open("databases/" + DATABASE_NAME);
                OutputStream outputStream = new FileOutputStream(dbPath);

                // Копируем базу данных
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Закрываем потоки
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDatabaseExists(String dbPath) {
        return new File(dbPath).exists();
    }
}