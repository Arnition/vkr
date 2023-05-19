package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class feed_activity extends AppCompatActivity {

    Spinner myspinner;
    ArrayAdapter<CharSequence> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        myspinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.activity_arr, R.layout.spinner);
        myspinner.setAdapter(adapter);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        DBAdapter db = new DBAdapter(this);
        db.open();
        db.close();

        Toast.makeText(this,"База данных подключена!", Toast.LENGTH_SHORT);
    }

    public void onRadioButtonClicked(View view) {
    }
}