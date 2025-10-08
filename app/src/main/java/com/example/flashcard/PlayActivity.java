package com.example.flashcard;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

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

        //test de intentExtra
        Intent srcIntent = getIntent();
        Arena arena = srcIntent.getParcelableExtra("arena");
        Log.d("arena", "imageID: " + arena.getImage() + "Difficulty: " + arena.getDifficulty() + " Background :" + arena.getBackgroundImage());


        // set background
        ImageView backgroundDifficultyImageView = findViewById(R.id.backgroundDifficultyImageView);
        backgroundDifficultyImageView.setImageResource(arena.getBackgroundImage());


        // Button to return to main menu
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Button to play audio
        Button lissenButton = findViewById(R.id.lissenbutton);
        mediaPlayer = MediaPlayer.create(this, R.raw.hogridervoice);

        lissenButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });

        ImageView emoteencadre = findViewById(R.id.emoteencadre);
        ImageView emotereaction = findViewById(R.id.emotereaction);
        ImageView text_true = findViewById(R.id.text_true);
        ImageButton response1 = findViewById(R.id.response1);
        ImageButton response2 = findViewById(R.id.response2);
        ImageButton response3 = findViewById(R.id.response3);

        emoteencadre.setVisibility(View.GONE);
        emotereaction.setVisibility(View.GONE);
        text_true.setVisibility(View.GONE);

        View.OnClickListener responseClickListener = v -> {
            emoteencadre.setVisibility(View.VISIBLE);
            emotereaction.setVisibility(View.VISIBLE);
            text_true.setVisibility(View.VISIBLE);
            emoteencadre.postDelayed(() -> {
                emoteencadre.setVisibility(View.GONE);
                emotereaction.setVisibility(View.GONE);
                text_true.setVisibility(View.GONE);
            }, 2000);
        };

        response1.setOnClickListener(responseClickListener);
        response2.setOnClickListener(responseClickListener);
        response3.setOnClickListener(responseClickListener);
        text_true.setOnClickListener(responseClickListener);
        startBarrelAnimation();
    }

    private void startBarrelAnimation() {
        ImageView squeleton = findViewById(R.id.squelleton);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        squeleton.setX(screenWidth);
        squeleton.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                squeleton,
                "translationX",
                screenWidth,
                -squeleton.getWidth()
        );
        animator.setDuration(2500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                squeleton.setVisibility(View.GONE);
                new Handler().postDelayed(() -> startBarrelAnimation(), 4000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
