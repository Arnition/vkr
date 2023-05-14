package com.example.vkrapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startFeedActivity(View view){
        Intent intent = new Intent(this, feed_activity.class);
        startActivity(intent);
    }

    public void startWalkActivity(View view){
        Intent intent = new Intent(this, walk_activity.class);
        startActivity(intent);
    }
}