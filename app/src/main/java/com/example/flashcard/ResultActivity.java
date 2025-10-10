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
import android.view.View;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    // Buttons
    private Button restartButton;
    private Button shareButton;
    private Button retryButton;

    // Variables
    private TextView chosenDifficulty;
    private TextView resultMessage;
    private TextView scoreResult;
    private TextView percentageResult;
    private ImageView resultImage;
    private int correctAnswers;
    private int totalQuestions;
    private int totalTimePlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get extras from PlayActivity
        String difficulty = getIntent().getStringExtra("difficulty");
        totalQuestions = getIntent().getIntExtra("maxRound", 0);
        correctAnswers = getIntent().getIntExtra("score", 0);
        ArrayList<Question> wrongQuestions = getIntent().getParcelableArrayListExtra("wrongQuestions");
        totalTimePlay = getIntent().getIntExtra("totalTimePlay", 0);

        // Create/Update GlobalStats json
        GlobalStats globalStats = new GlobalStats(1,correctAnswers,totalQuestions-correctAnswers,totalTimePlay,0);
        if (!globalStats.jsonExist(this, "globalStats.json"))
        {
            globalStats.jsonWrite(this, "globalStats.json");
        }
        else{
            GlobalStats  globalStatsJson = globalStats.jsonRead(this, "globalStats.json");
            int totalQuiz = globalStatsJson.getTotalQuiz();
            int goodAnswer = globalStatsJson.getGoodAnswer();
            int badAnswer = globalStatsJson.getBadAnswer();
            long totalTimePlays = globalStatsJson.getTotalTimePlay();
            int meanQuizTime = globalStatsJson.getMeanQuizTime();
            globalStats.addMeanQuizTime(meanQuizTime);
            globalStats.addTotalQuiz(totalQuiz);
            globalStats.addGoodAnswer(goodAnswer);
            globalStats.addBadAnswer(badAnswer);
            globalStats.addTotalTimePlay(totalTimePlays);
            globalStats.jsonWrite(this, "globalStats.json");

        }








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
        int percentage = (int)(((float)correctAnswers / totalQuestions) * 100);

        // Display the percentage
        percentageResult = findViewById(R.id.percentagetextView);
        percentageResult.setText(percentage + " %");

        // Apply conditions and display different messages depending on the score and the chosen difficulty
        if (percentage < 50) {
            if (difficulty.equals("Facile")) {
                resultImage.setImageResource(R.drawable.defeat_easy);

            } else if (difficulty.equals("Moyen")) {
                resultImage.setImageResource(R.drawable.defeat_medium);

            } else {
                resultImage.setImageResource(R.drawable.defeat_hard);
            }
            resultMessage.setText("Entraîne-toi !");
        } else if (percentage < 80) {

            if (difficulty.equals("Facile")) {
                resultImage.setImageResource(R.drawable.correct_easy);

            } else if (difficulty.equals("Moyen")) {
                resultImage.setImageResource(R.drawable.correct_medium);

            } else {
                resultImage.setImageResource(R.drawable.correct_hard);
            }
            resultMessage.setText("Bien joué !");
        } else {
            if (difficulty.equals("Facile")) {
                resultImage.setImageResource(R.drawable.victory_easy);

            } else if (difficulty.equals("Moyen")) {
                resultImage.setImageResource(R.drawable.victory_medium);

            } else {
                resultImage.setImageResource(R.drawable.victory_hard);
            }
            resultMessage.setText("Excellent !");
        }

        // "Replay" button
        restartButton = findViewById(R.id.restartButton);

        restartButton.setOnClickListener(v -> {
            // Navigate to the main page
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // "Share" button
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            // Message to share
            String message = "J'ai fait : " + correctAnswers + " / " + totalQuestions + " au Flashcard, essaie toi aussi !";

            // Send via intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        // "Retry" button
        retryButton = findViewById(R.id.retryButton);
        if (percentage == 100) {
            retryButton.setVisibility(View.GONE);
        }
        retryButton.setOnClickListener(v -> {
            // Check that the question list is present and not empty
            if (wrongQuestions != null && !wrongQuestions.isEmpty()) {

                // Relaunch the PlayActivity via Intent
                Intent intent = new Intent(this, PlayActivity.class);

                // Send the list of questions to retry
                intent.putParcelableArrayListExtra("retryQuestions", wrongQuestions);
                startActivity(intent);
            }
        });
    }
}
