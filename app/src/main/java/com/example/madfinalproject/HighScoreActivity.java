package com.example.madfinalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HighScoreActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "HighScores";
    private static final String SCORES_KEY = "Scores";

    private List<ScoreEntry> leaderboard = new ArrayList<>();
    private TextView highScoreList;
    private EditText playerNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);

        // Initialize views
        highScoreList = findViewById(R.id.highScoreList);
        playerNameInput = findViewById(R.id.playerNameInput);
        Button saveScoreButton = findViewById(R.id.saveScoreButton);
        Button backButton = findViewById(R.id.backButton);

        // Load leaderboard from SharedPreferences
        loadLeaderboard();

        // Get the score from the intent
        int score = getIntent().getIntExtra("SCORE", 0);

        // Debugging: Log the received score
        Log.d("HighScoreActivity", "Received score: " + score);

        // Save score button logic
        saveScoreButton.setOnClickListener(v -> {
            String playerName = playerNameInput.getText().toString().trim();
            if (playerName.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            } else {
                saveScore(playerName, score);
                updateHighScoreList();
                playerNameInput.setText(""); // Clear the input field
                Toast.makeText(this, "Score saved!", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button logic
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Update the leaderboard display
        updateHighScoreList();
    }

    private void saveScore(String name, int score) {
        Log.d("HighScoreActivity", "Saving score: " + score); // Debugging: Log the score being saved
        leaderboard.add(new ScoreEntry(name, score));
        Collections.sort(leaderboard, (a, b) -> b.score - a.score); // Sort by score descending
        if (leaderboard.size() > 10) {
            leaderboard.remove(leaderboard.size() - 1); // Keep only top 10
        }
        saveLeaderboard();
    }

    private void loadLeaderboard() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String serializedScores = prefs.getString(SCORES_KEY, "");

        // Debugging: Log the serialized scores loaded from SharedPreferences
        Log.d("HighScoreActivity", "Loaded serialized scores: " + serializedScores);

        if (!serializedScores.isEmpty()) {
            String[] entries = serializedScores.split(";");
            for (String entry : entries) {
                String[] parts = entry.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    leaderboard.add(new ScoreEntry(name, score));
                }
            }
        }
    }

    private void saveLeaderboard() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        StringBuilder serializedScores = new StringBuilder();
        for (ScoreEntry entry : leaderboard) {
            serializedScores.append(entry.name).append(",").append(entry.score).append(";");
        }
        editor.putString(SCORES_KEY, serializedScores.toString());
        editor.apply();

        // Debugging: Log the serialized scores being saved
        Log.d("HighScoreActivity", "Saving serialized scores: " + serializedScores.toString());
    }

    private void updateHighScoreList() {
        StringBuilder displayText = new StringBuilder();
        for (int i = 0; i < leaderboard.size(); i++) {
            ScoreEntry entry = leaderboard.get(i);
            displayText.append(i + 1).append(". ").append(entry.name).append(": ").append(entry.score).append("\n");
        }
        highScoreList.setText(displayText.toString());
    }

    private static class ScoreEntry {
        String name;
        int score;

        ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
