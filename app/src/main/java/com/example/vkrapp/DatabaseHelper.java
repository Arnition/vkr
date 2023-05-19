package com.example.vkrapp;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "testanimal.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "animal_test"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE animal_test (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
//                + " TEXT );");
//        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" " +
//                "(" + COLUMN_NAME+ ") VALUES ('Кошка');");
//        db.execSQL("INSERT INTO "+ TABLE +" " +
//                "(" + COLUMN_NAME+ ") VALUES ('Собака');");
//        db.execSQL("INSERT INTO "+ TABLE +" " +
//                "(" + COLUMN_NAME+ ") VALUES ('Попугай');");
//        db.execSQL("INSERT INTO "+ TABLE +" " +
//                "(" + COLUMN_NAME+ ") VALUES ('Хомяк');");
//        db.execSQL("INSERT INTO "+ TABLE +" " +
//                "(" + COLUMN_NAME+ ") VALUES ('Крыса');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
