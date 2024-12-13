package com.example.madfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class gameoveractivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameoveractivity); // Updated to match the new layout file name

        // Get score from intent
        int score = getIntent().getIntExtra("SCORE", 0);

        // Debugging: Log the received score
        Log.d("GameOverActivity", "Received score: " + score);

        TextView scoreTextView = findViewById(R.id.finalScoreText);
        Button retryButton = findViewById(R.id.retryButton);

        // Display score
        scoreTextView.setText("Your Score: " + score);

        // Retry button action
        retryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // High Score button action
        Button highScoreButton = findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HighScoreActivity.class);
            intent.putExtra("SCORE", score); // Pass score to HighScoreActivity
            startActivity(intent);
        });
    }
}
