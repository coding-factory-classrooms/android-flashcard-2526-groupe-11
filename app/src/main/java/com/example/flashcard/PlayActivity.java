package com.example.flashcard;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Objects;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    private ImageButton response1, response2, response3;
    private ImageView emoteEncadre, typeResponse;
    private TextView indexQuestionTextView, timerTextView;
    private Button listenButton;

    private Card correctCard;

    public int score = 0;
    public int roundNumber = 0;
    public int maxRoundNumber = 5;
    public Arena arena;
    public int currentTimePerQuestion = 5;
    public int timePerQuestion = 5;

    private Handler TimerHandler;
    private Runnable TimerRunnable;
    private boolean easterEgg;

    private ReactionManager reactionManager;

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

        // Get arena and extras
        Intent srcIntent = getIntent();
        this.arena = srcIntent.getParcelableExtra("arena");
        this.easterEgg = srcIntent.getBooleanExtra("easterEgg", false);

        // Background
        ImageView backgroundDifficultyImageView = findViewById(R.id.backgroundDifficultyImageView);
        backgroundDifficultyImageView.setImageResource(arena.getBackgroundImage());

        // Home button
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            releaseMediaPlayer();
            stop_timer();
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
            finish();
        });

        // Initialize UI
        listenButton = findViewById(R.id.lissenbutton);
        emoteEncadre = findViewById(R.id.emoteencadre);
        typeResponse = findViewById(R.id.type_response);
        response1 = findViewById(R.id.response1);
        response2 = findViewById(R.id.response2);
        response3 = findViewById(R.id.response3);
        indexQuestionTextView = findViewById(R.id.indexQuestionTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // Hide reactions at start
        emoteEncadre.setVisibility(View.GONE);
        typeResponse.setVisibility(View.GONE);

        indexQuestionTextView.setText(roundNumber + "/" + maxRoundNumber);

        // Initialize ReactionManager
        reactionManager = new ReactionManager(this, emoteEncadre, typeResponse, response1, response2, response3);

        if (easterEgg) {
            timerTextView.setText(currentTimePerQuestion + "s");
            start_timer();
        }

        gameManager = new GameManager();
        startNewRound();
        startBarrelAnimation();
    }

    // Start a new round
    private void startNewRound() {
        if (easterEgg) {
            stop_timer();
            currentTimePerQuestion = timePerQuestion;
            timerTextView.setText(currentTimePerQuestion + "s");
            start_timer();
        }

        releaseMediaPlayer();
        gameManager.startNewRound();

        correctCard = gameManager.getCorrectCard();
        List<Card> roundOptions = gameManager.getRoundOptions();
        if (roundOptions.size() < 3) return;

        // Set images for options
        response1.setImageResource(roundOptions.get(0).getImageResId());
        response2.setImageResource(roundOptions.get(1).getImageResId());
        response3.setImageResource(roundOptions.get(2).getImageResId());

        // Audio
        mediaPlayer = MediaPlayer.create(this, correctCard.getAudioResId());
        if (Objects.equals(arena.getDifficulty(), "Difficile")){
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(500, 500);
            PlaybackParams params = new PlaybackParams();
            params.setPitch(0.2f);
            mediaPlayer.setPlaybackParams(params);
        }



        listenButton.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.start();
        });

        // Listeners
        setResponseClick(response1, roundOptions.get(0));
        setResponseClick(response2, roundOptions.get(1));
        setResponseClick(response3, roundOptions.get(2));
    }

    private void setResponseClick(ImageButton button, Card card) {
        button.setOnClickListener(v -> {
            if (easterEgg) stop_timer();
            handleClick(card, button);
        });
    }

    private void handleClick(Card card, ImageButton clickedButton) {
        stop_timer();
        boolean correct = card == correctCard;

        // Utilisation de ReactionManager
        boolean isCorrect = reactionManager.showReaction(correct, clickedButton, correctCard, gameManager.getRoundOptions());
        if (isCorrect) score++;
        reactionManager.hideReactionAfterDelay();

        roundNumber++;
        indexQuestionTextView.setText(roundNumber + "/" + maxRoundNumber);

        if (roundNumber < maxRoundNumber) {
            new Handler().postDelayed(this::startNewRound, 2000);
        } else {
            new Handler().postDelayed(this::navigateToVictory, 2000);
        }
    }

    // Timer
    private void start_timer() {
        TimerHandler = new Handler();
        TimerHandler.postDelayed(TimerRunnable = new Runnable() {
            @Override
            public void run() {
                timer();
                TimerHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void stop_timer() {
        if (TimerHandler != null && TimerRunnable != null) TimerHandler.removeCallbacks(TimerRunnable);
    }

    private void timer() {
        if (currentTimePerQuestion <= 0) {
            timerTextView.setText("0s");
            handleClick(new Card(0, 0), null);
            currentTimePerQuestion = timePerQuestion;
        } else {
            timerTextView.setText(currentTimePerQuestion + "s");
            currentTimePerQuestion--;
        }
    }

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

    private void navigateToVictory() {
        stop_timer();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", arena.getDifficulty());
        intent.putExtra("maxRound", maxRoundNumber);
        startActivity(intent);
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop_timer();
        releaseMediaPlayer();
    }
}
