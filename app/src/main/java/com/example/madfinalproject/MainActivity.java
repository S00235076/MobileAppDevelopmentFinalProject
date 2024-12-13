package com.example.madfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button blueButton, greenButton, redButton, yellowButton, playButton;
    private TextView scoreBoard;
    private List<Integer> sequence = new ArrayList<>();
    private List<Integer> userSequence = new ArrayList<>();
    private int currentStep = 0;
    private int score = 0;
    private int round = 1;
    private boolean isUserTurn = false;

    private final Handler handler = new Handler();
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        blueButton = findViewById(R.id.blueButton);
        greenButton = findViewById(R.id.greenButton);
        redButton = findViewById(R.id.redButton);
        yellowButton = findViewById(R.id.yellowButton);
        playButton = findViewById(R.id.playButton);

        scoreBoard = findViewById(R.id.scoreBoard);

        setButtonListeners();


        playButton.setOnClickListener(v -> startNewGame());
    }

    private void setButtonListeners() {
        View.OnClickListener listener = v -> {
            if (!isUserTurn) return;

            int colorId = getColorIdFromButton(v.getId());
            userSequence.add(colorId);

            if (colorId == sequence.get(userSequence.size() - 1)) {
                if (userSequence.size() == sequence.size()) {
                    score += sequence.size();
                    updateScoreBoard();
                    Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
                    handler.postDelayed(this::nextRound, 1000);
                }
            } else {
                Toast.makeText(this, "Wrong! Game Over.", Toast.LENGTH_SHORT).show();
                navigateToGameOver();
            }
        };

        blueButton.setOnClickListener(listener);
        greenButton.setOnClickListener(listener);
        redButton.setOnClickListener(listener);
        yellowButton.setOnClickListener(listener);
    }

    private void startNewGame() {
        sequence.clear();
        userSequence.clear();
        currentStep = 0;
        score = 0;
        round = 1;
        updateScoreBoard();
        isUserTurn = false;
        nextRound();
    }

    private void nextRound() {
        userSequence.clear();


        int nextSequenceLength = 4 + (round - 1);


        for (int i = 0; i < nextSequenceLength; i++) {
            sequence.add(random.nextInt(4));
        }

        round++;
        playSequence();
    }

    private void playSequence() {
        isUserTurn = false;
        handler.postDelayed(() -> {
            for (int i = 0; i < sequence.size(); i++) {
                int delay = i * 1000;
                handler.postDelayed(() -> flashButton(sequence.get(currentStep++)), delay);
            }
            handler.postDelayed(() -> {
                currentStep = 0;
                isUserTurn = true;
            }, sequence.size() * 1000);
        }, 500);
    }

    private void flashButton(int colorId) {
        Button button = getButtonFromColorId(colorId);
        if (button == null) return;

        button.setAlpha(0.5f);
        handler.postDelayed(() -> button.setAlpha(1f), 500);
    }

    private Button getButtonFromColorId(int colorId) {
        switch (colorId) {
            case 0: return blueButton;
            case 1: return greenButton;
            case 2: return redButton;
            case 3: return yellowButton;
            default: return null;
        }
    }

    private int getColorIdFromButton(int buttonId) {
        if (buttonId == R.id.blueButton) return 0;
        if (buttonId == R.id.greenButton) return 1;
        if (buttonId == R.id.redButton) return 2;
        if (buttonId == R.id.yellowButton) return 3;
        return -1;
    }

    private void updateScoreBoard() {
        scoreBoard.setText("Score: " + score);
    }

    private void navigateToGameOver() {
        Intent intent = new Intent(this, gameoveractivity.class);
        intent.putExtra("SCORE", score);
        startActivity(intent);
        finish();
    }
}
