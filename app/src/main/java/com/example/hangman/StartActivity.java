package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onClickPlay(View view) {
        Intent myIntent = new Intent(getBaseContext(), ListSelectActivity.class);
        startActivity(myIntent);
    }

    public void onClickInstructions(View view) {
        //todo add popup with instructions
    }
}
