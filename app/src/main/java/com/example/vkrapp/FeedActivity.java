package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vkrapp.utils.Animal;
import com.example.vkrapp.utils.DatabaseHelper;
import com.example.vkrapp.utils.Helper;

import java.util.List;


public class FeedActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> animalsTypeAdapter;
    private ArrayAdapter<String> animalStageAdapter;

    private Spinner animalTypeSpinner;
    private Spinner spinnerAnimalStage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        this.addHooks();
    }

    private void addHooks() {
        animalsTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner, databaseHelper.getAllAnimalsType());

        animalTypeSpinner = findViewById(R.id.spinnerAnimalType);
        animalTypeSpinner.setAdapter(animalsTypeAdapter);

        animalTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                updateAnimalStageAdapter(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case when nothing is selected
            }
        });


        EditText kg = findViewById(R.id.kg);
        EditText calorie = findViewById(R.id.calorie);
        TextView txt = findViewById(R.id.allvivod);

//        button click hook
        findViewById(R.id.all).setOnClickListener(view -> {
            if(Helper.isValidDataElement(kg) && Helper.isValidDataElement(calorie)) {
                String animalType = animalTypeSpinner.getSelectedItem().toString();
                String animalStage = spinnerAnimalStage.getSelectedItem().toString();

                double coefficient = databaseHelper.getAnimalCoefficient(animalType, animalStage);
                double weight = Helper.parseData(kg);
                double kkl = Helper.parseData(calorie);

                Toast.makeText(getApplicationContext(), " " + coefficient, Toast.LENGTH_SHORT).show();

                Animal animal = new Animal(animalType, weight, kkl, coefficient);
                txt.setText(animal.calcResult() + "г в день \nнужно кушать вашему питомцу!");
            }else {
                Toast.makeText(getApplicationContext(), "Проверьте введенные данные", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateAnimalStageAdapter(String animalType) {
        animalStageAdapter = new ArrayAdapter<>(this, R.layout.spinner, databaseHelper.getAnimalParamsByAnimalType(animalType));
        spinnerAnimalStage = findViewById(R.id.spinnerAnimalStage);
        spinnerAnimalStage.setAdapter(animalStageAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}