package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class feed_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);


        DBAdapter db = new DBAdapter(this);
        db.open();
        db.close();

        Toast.makeText(this,"База данных подключена!", Toast.LENGTH_SHORT);
    }
}