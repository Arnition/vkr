package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class feed_activity extends AppCompatActivity {
    ListView userList;
    TextView header;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    Spinner myspinner;
    Spinner myspinnerTest;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        myspinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.activity_arr, R.layout.spinner);
        myspinner.setAdapter(adapter);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        myspinnerTest = (Spinner) findViewById(R.id.spnTest);

//        final int RB1_ID = 1;//first radio button id
//        final int RB2_ID = 2;//second radio button id
//
//        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton);
//        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
//        rb1.setId(RB1_ID);
//        rb2.setId(RB2_ID);

//
//
//        RadioGroup myRadioGroup = findViewById(R.id.rad);
//        int radId = myRadioGroup.getCheckedRadioButtonId();
//
//        int state = (int) myspinner.getSelectedItemId();
//        int state_osn = (int) myspinnerTest.getSelectedItemId();

        EditText kg = findViewById(R.id.kg);
        EditText calorie = findViewById(R.id.calorie);

        Button btn = findViewById(R.id.all);
        TextView txt = findViewById(R.id.allvivod);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String animal_type = null;

                Cursor cursor = userAdapter.getCursor();
                int columnIndex = cursor.getColumnIndex("name");

                if (columnIndex != -1) {
                    animal_type = cursor.getString(columnIndex);
                }

                double weight = Double.parseDouble(kg.getText().toString().trim());
                String life_stage = myspinner.getSelectedItem().toString();
                double calorie_value = Double.parseDouble(calorie.getText().toString().trim());

                Animal animal = new Animal(animal_type, life_stage, weight, calorie_value);
                int result = CalcLogic.getCoefficient(animal);

                txt.setText(result + "г в день \nнужно кушать вашему питомцу!");

            }
        });
    }

    //                if (state == 0 && state_osn == 0 && radId == R.id.radioButton .equals("Малоподвижный образ жизни")) {
//                    energy = 30 * str + 70;
//                    kof = (int) (energy * 1.2);
//                    result = str2 / kof;
//                    txt.setText(String.valueOf(result));
//                } else if (state == 0 && state_osn == 0 && radId == R.id.radioButton2) {
//                    energy = 30 * str + 70;
//                    kof = (int) (energy * 1.4);
//                    result = str2 / kof;
//                    txt.setText(String.valueOf(result));
//                }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_ANIMAL, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME_ANIMAL};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                userCursor, headers, new int[]{android.R.id.text1}, 0);
        myspinnerTest.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }
}