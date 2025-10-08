package com.example.flashcard;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    // Buttons
    private Button restartButton;
    private Button shareButton;

    // Variables
    private TextView chosenDifficulty;
    private TextView resultMessage;
    private TextView scoreResult;
    private ImageView resultImage;
    private int correctAnswers;
    private int totalQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        // Adjust padding to account for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get extras from PlayActivity
        String difficulty = getIntent().getStringExtra("difficulty");
        totalQuestions = getIntent().getIntExtra("maxRound", 0);
        correctAnswers = getIntent().getIntExtra("score", 0);

        // Debug code (to remove once everything is linked)
//        difficulty = "facile";
//        totalQuestions = 10;
//        correctAnswers = 9;

        // Display the chosen difficulty
        chosenDifficulty = findViewById(R.id.difficultyTextView);
        chosenDifficulty.setText(difficulty);

        // Display the score
        scoreResult = findViewById(R.id.scoreTextView);
        scoreResult.setText("Score : " + correctAnswers + "/" + totalQuestions);

        // Display an image and a message depending on the result
        resultImage = findViewById(R.id.resultImageView);
        resultMessage = findViewById(R.id.messageTextView);

        // Calculate success percentage
        float percentage = ((float) correctAnswers / totalQuestions * 100);

        // Apply conditions and display different messages
        if (percentage < 50) {
            resultImage.setImageResource(R.drawable.loss);
            resultMessage.setText("Entraîne-toi !");
        } else if (percentage < 80) {
            resultImage.setImageResource(R.drawable.card_hog_rider);
            resultMessage.setText("Bien joué !");
        } else {
            resultImage.setImageResource(R.drawable.emote_win);
            resultMessage.setText("Excellent !");
        }

        // "Replay" button
        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Replay button clicked");

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // "Share" button
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Share button clicked");

            // Message to share
            String message = "J'ai fait : " + correctAnswers + " / " + totalQuestions + " au Flashcard, essaie toi aussi !";

            // Send via intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(shareIntent, "Partagez via"));
        });
    }
}
