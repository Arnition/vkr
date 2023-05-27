package com.example.vkrapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class RecommendationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        String animalType = getIntent().getStringExtra("AnimalType");
        String recommendationText = getIntent().getStringExtra("RecommendationText");

        ((TextView) findViewById(R.id.animalTypeTextView)).setText(animalType);
        ((TextView) findViewById(R.id.recommendationTextView)).setText(recommendationText);
    }
}

