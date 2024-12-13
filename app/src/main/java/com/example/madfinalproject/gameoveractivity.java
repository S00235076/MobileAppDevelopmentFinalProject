package com.example.madfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class gameoveractivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoveractivity); // Updated to match the new layout file name


        int score = getIntent().getIntExtra("SCORE", 0);


        TextView scoreTextView = findViewById(R.id.finalScoreText);
        Button retryButton = findViewById(R.id.retryButton);


        scoreTextView.setText("Your Score: " + score);


        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
