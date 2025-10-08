package com.example.flashcard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    private ImageButton response1, response2, response3;
    private ImageView emoteencadre, emotereaction, type_response;
    private Button listenButton;

    private Card correctCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get arena from intent
        Intent srcIntent = getIntent();
        Arena arena = srcIntent.getParcelableExtra("arena");
        Log.d("arena", "imageID: " + arena.getImage() + "Difficulty: " + arena.getDifficulty() + " Background :" + arena.getBackgroundImage());


        // set background
        ImageView backgroundDifficultyImageView = findViewById(R.id.backgroundDifficultyImageView);
        backgroundDifficultyImageView.setImageResource(arena.getBackgroundImage());

        // Home button
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Initialize UI
        listenButton = findViewById(R.id.lissenbutton);
        emoteencadre = findViewById(R.id.emoteencadre);
        emotereaction = findViewById(R.id.emotereaction);
        type_response = findViewById(R.id.type_response);
        response1 = findViewById(R.id.response1);
        response2 = findViewById(R.id.response2);
        response3 = findViewById(R.id.response3);

        // Hide reactions at start
        emoteencadre.setVisibility(View.GONE);
        emotereaction.setVisibility(View.GONE);
        type_response.setVisibility(View.GONE);

        gameManager = new GameManager();
        startNewRound();
        startBarrelAnimation();
    }

    // Start a new round
    private void startNewRound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        gameManager.startNewRound();
        correctCard = gameManager.getCorrectCard();
        List<Card> roundOptions = gameManager.getRoundOptions();

        // Set images for options
        response1.setImageResource(roundOptions.get(0).getImageResId());
        response2.setImageResource(roundOptions.get(1).getImageResId());
        response3.setImageResource(roundOptions.get(2).getImageResId());

        // Set audio for the correct card
        mediaPlayer = MediaPlayer.create(this, correctCard.getAudioResId());

        listenButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });

        // Set click listeners
        setResponseClick(response1, roundOptions.get(0));
        setResponseClick(response2, roundOptions.get(1));
        setResponseClick(response3, roundOptions.get(2));
    }

    // Check if the answer is correct
    private void setResponseClick(ImageButton button, Card card) {
        button.setOnClickListener(v -> {
            if (card == correctCard) {
                showReaction(true);
            } else {
                showReaction(false);
            }
            // Start new round after 2s
            new Handler().postDelayed(this::startNewRound, 2000);
        });
    }

    // Show reaction (win/lose)
    private void showReaction(boolean correct) {
        emoteencadre.setVisibility(View.VISIBLE);
        emotereaction.setVisibility(View.VISIBLE);
        type_response.setVisibility(View.VISIBLE);

        if (correct) {
            emotereaction.setImageResource(R.drawable.emote_win);
            type_response.setImageResource(R.drawable.text_true);
        } else {
            emotereaction.setImageResource(R.drawable.emote_lose);
            type_response.setImageResource(R.drawable.text_false);
        }

        // Hide reactions after 2s
        emoteencadre.postDelayed(() -> {
            emoteencadre.setVisibility(View.GONE);
            emotereaction.setVisibility(View.GONE);
            type_response.setVisibility(View.GONE);
        }, 2000);
    }

    // Skeleton animation across the screen
    private void startBarrelAnimation() {
        ImageView squeleton = findViewById(R.id.squelleton);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        squeleton.setX(screenWidth);
        squeleton.setVisibility(View.VISIBLE);
        squeleton.animate()
                .translationX(-squeleton.getWidth())
                .setDuration(2500)
                .withEndAction(() -> {
                    squeleton.setVisibility(View.GONE);
                    new Handler().postDelayed(this::startBarrelAnimation, 4000);
                })
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
