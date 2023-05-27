package com.example.vkrapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startFeedActivity(View view){
        Intent intent = new Intent(this, FeedActivity.class);
        startActivity(intent);
    }

    public void startWalkActivity(View view){
        Intent intent = new Intent(this, WalkActivity.class);
        startActivity(intent);
    }
}