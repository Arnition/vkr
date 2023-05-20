package com.example.vkrapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "animal_new.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных

    private SQLiteDatabase db;
    private DatabaseHelper DBHelper;

    // название таблицы в бд
    static final String TABLE_ANIMAL = "animals_table";
    static final String TABLE_PARAMS = "params_table";
    static final String TABLE_DATA = "data_table";
    // названия столбцов для таблицы animals_table
    public static final String COLUMN_ID_ANIMAL = "animals_id";
    public static final String COLUMN_NAME_ANIMAL = "name_animals";
    // названия столбцов для таблицы params_table
    public static final String COLUMN_ID_PARAMS = "params_id";
    public static final String COLUMN_NAME_PARAMS = "name_params";

    // названия столбцов для таблицы data_table
    public static final String COLUMN_ID_DATA = "data_id";
    public static final String COLUMN_NAME_ANIMALID = "id_animals";
    public static final String COLUMN_NAME_PARAMSID = "id_params";
    public static final String COLUMN_NAME_KOF = "kof";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL("CREATE TABLE animals_table (" + COLUMN_ID_ANIMAL
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_ANIMAL
                    + " VARCHAR );");

            db.execSQL("CREATE TABLE params_table (" + COLUMN_ID_PARAMS
                    + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_PARAMS
                    + " VARCHAR );");

            String sql_create_table = "CREATE TABLE data_table (" + COLUMN_ID_DATA + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
            sql_create_table += COLUMN_NAME_ANIMALID + " INTEGER, ";

            sql_create_table += "FOREIGN KEY (" + COLUMN_NAME_ANIMALID + ") REFERENCES " + TABLE_ANIMAL + " ";
            sql_create_table += "(" + COLUMN_ID_ANIMAL + "), " + COLUMN_NAME_PARAMSID + " INTEGER, ";

            sql_create_table += "FOREIGN KEY (" + COLUMN_NAME_PARAMSID + ") REFERENCES " + TABLE_PARAMS + " ";
            sql_create_table += "(" + COLUMN_ID_PARAMS + "), " + COLUMN_NAME_PARAMS + " REAL);";

            db.execSQL(sql_create_table);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// добавление начальных данных
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

//    public void onInsert(SQLiteDatabase db, String values){
//        db.execSQL("INSERT INTO "+ TABLE_ANIMAL +" " +
//                "(" + COLUMN_NAME_ANIMAL + ") VALUES (" + values + ")");
//    }


    public DatabaseHelper open_db() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close_db(){
        DBHelper.close();
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ANIMAL);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PARAMS);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_DATA);
        onCreate(db);
    }
}

