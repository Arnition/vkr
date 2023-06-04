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

import org.w3c.dom.Text;

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

    private TextView txt;
    private EditText kg;
    private EditText calorie;

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

        txt.setText("");
        ((EditText) findViewById(R.id.calorie)).setText("");
        ((EditText) findViewById(R.id.kg)).setText("");
    }

    private void addHooks() {
        kg = findViewById(R.id.kg);
        calorie = findViewById(R.id.calorie);
        txt = findViewById(R.id.allvivod);

        //получение животного в спинер
        animalsTypeAdapter = new ArrayAdapter<>(this, R.layout.spinner, databaseHelper.getAllAnimalsType());
        animalsTypeAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        animalTypeSpinner = findViewById(R.id.spinnerAnimalType);
        animalTypeSpinner.setAdapter(animalsTypeAdapter);


        //получение позиции из спинера
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

        //переход на экран рекомендации по нажатию кнопки
        findViewById(R.id.recommendation).setOnClickListener(view -> {
            loadSelectedAnimal();
            Intent intent = new Intent(FeedActivity.this, RecommendationActivity.class);
            intent.putExtra("AnimalType", selected_animal.getAnimalType());
            intent.putExtra("RecommendationText", selected_animal.getRecommendation());
            startActivity(intent);
        });

        //кнопка расчета
        findViewById(R.id.all).setOnClickListener(view -> {
            if(Helper.isValidDataElement(kg) || kg.getVisibility() == View.GONE) {
                if(Helper.isValidDataElement(calorie) || calorie.getVisibility() == View.GONE) {
                    loadSelectedAnimal();
                    txt.setText(selected_animal.calcResult() + "г в день \nнужно кушать вашему питомцу!");
                    return;
                }
            }

            Toast.makeText(getApplicationContext(), "Проверьте введенные данные", Toast.LENGTH_LONG).show();
        });
    }

    //получение данных из бд
    private void loadSelectedAnimal() {
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
    }

    private void updateAnimalStageAdapter(String animalType) {
        List<String> animalParams = databaseHelper.getAnimalParamsByAnimalType(animalType); //список параметров для животного

        //спиннер для стадии жизни и вида животного
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

        //логика для животного, который не нуждается в рассчетах
        Button calcButton = findViewById(R.id.all);
        if (spinnerAnimalStage.getVisibility() == View.VISIBLE || isDependWeight || isDependKg) {
            calcButton.setVisibility(View.VISIBLE);
            txt.setText("");
        }else {
            calcButton.setVisibility(View.GONE);
            txt.setText("Ваш питомец не нуждается в приеме или расчете суточной нормы потребления корма. \nПросмотрите рекомендации по питанию");
        }
    }

    private void changeVisibility(List<android.view.View> elements, int visibility) {
        for(View e : elements)
            e.setVisibility(visibility);
    }

    //для каллорийности корма
    private void kgComponentsChangeVisibility(int view) {
        List<android.view.View> weightElements = Arrays.asList(findViewById(R.id.textView9),findViewById(R.id.calorie));
        changeVisibility(weightElements, view);
    }

    //для веса животного
    private void weightComponentsChangeVisibility(int view) {
        List<android.view.View> weightElements = Arrays.asList(findViewById(R.id.textView7),findViewById(R.id.kg));
        changeVisibility(weightElements, view);
    }

    //для стадии жизни
    private void stageAnimalComponentsChangeVisibility(int view) {
        List<android.view.View> stageAnimalElements = Arrays.asList(findViewById(R.id.textView8),findViewById(R.id.spinnerAnimalStage));
        changeVisibility(stageAnimalElements, view);
    }


    //обновление полей
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