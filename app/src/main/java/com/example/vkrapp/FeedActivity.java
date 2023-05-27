package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Debug;
import android.view.KeyEvent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FeedActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> animalsTypeAdapter;
    private ArrayAdapter<String> animalStageAdapter;

    private Spinner animalTypeSpinner;
    private Spinner spinnerAnimalStage;
    private Animal selected_animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        this.addHooks();

    }

    @Override
    public void onPause() {
        super.onPause();

        ((TextView) findViewById(R.id.allvivod)).setText("");
        ((EditText) findViewById(R.id.calorie)).setText("");
        ((EditText) findViewById(R.id.kg)).setText("");
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


        Button recommendation = findViewById(R.id.recommendation);
        recommendation.setVisibility(View.GONE);

        recommendation.setOnClickListener(view -> {
            Intent intent = new Intent(FeedActivity.this, RecommendationActivity.class);
            intent.putExtra("AnimalType", selected_animal.getAnimalType());
            intent.putExtra("RecommendationText", selected_animal.getRecommendation());
            startActivity(intent);
//            this.cle
//            this.finish();
        });

//        button click hook
        findViewById(R.id.all).setOnClickListener(view -> {
            if(Helper.isValidDataElement(kg) || kg.getVisibility() == View.GONE) {
                if(Helper.isValidDataElement(calorie) || calorie.getVisibility() == View.GONE) {
                    String animalType = animalTypeSpinner.getSelectedItem().toString();
                    String animalStage = spinnerAnimalStage.getSelectedItem().toString();
                    String recommendationText = databaseHelper.getAnimalRecommendation(animalType);

                    double defaultAnimalCoefficient = databaseHelper.getDefaultAnimalCoefficient(animalType);
                    double coefficient = databaseHelper.getAnimalCoefficient(animalType, animalStage);
                    double grams = databaseHelper.getAnimalGrams(animalType, animalStage);
                    double weight = Helper.parseData(kg);
                    double kkl = Helper.parseData(calorie);

                    boolean hasAnimalParams = spinnerAnimalStage.getCount() > 0;

                    selected_animal = new Animal(animalType, recommendationText, defaultAnimalCoefficient, weight, grams, kkl, coefficient, hasAnimalParams);
                    txt.setText(selected_animal.calcResult() + "г в день \nнужно кушать вашему питомцу!");


                    recommendation.setVisibility(View.VISIBLE);
                    return;
                }
            }

            Toast.makeText(getApplicationContext(), "Проверьте введенные данные", Toast.LENGTH_LONG).show();
        });
    }

    private void updateAnimalStageAdapter(String animalType) {
        List<String> animalParams = databaseHelper.getAnimalParamsByAnimalType(animalType); //список параметров для животного

        if(animalParams.size() > 0) {
            animalStageAdapter = new ArrayAdapter<>(this, R.layout.spinner, animalParams);
            spinnerAnimalStage = findViewById(R.id.spinnerAnimalStage);
            spinnerAnimalStage.setAdapter(animalStageAdapter);

            stageAnimalComponentsChangeVisibility(View.VISIBLE);
        }else {
            stageAnimalComponentsChangeVisibility(View.GONE);
        }

        boolean isDependWeight = databaseHelper.isDependWeight(animalType);
        weightComponentsChangeVisibility(isDependWeight ? View.VISIBLE : View.GONE);

        boolean isDependKg = databaseHelper.isDependKg(animalType);
        kgComponentsChangeVisibility(isDependKg ? View.VISIBLE : View.GONE);
    }

    private void changeVisibility(List<android.view.View> elements, int visibility) {
        for(View e : elements)
            e.setVisibility(visibility);
    }

    //для kg
    private void kgComponentsChangeVisibility(int view) {
        List<android.view.View> weightElements = Arrays.asList(findViewById(R.id.textView9),findViewById(R.id.calorie));
        changeVisibility(weightElements, view);
    }

    //для веса
    private void weightComponentsChangeVisibility(int view) {
        List<android.view.View> weightElements = Arrays.asList(findViewById(R.id.textView7),findViewById(R.id.kg));
        changeVisibility(weightElements, view);
    }

    //для стадии жизни
    private void stageAnimalComponentsChangeVisibility(int view) {
        List<android.view.View> stageAnimalElements = Arrays.asList(findViewById(R.id.textView8),findViewById(R.id.spinnerAnimalStage));
        changeVisibility(stageAnimalElements, view);
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