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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    private ImageButton response1, response2, response3;
    private ImageView emoteencadre, emotereaction, type_response;
    private TextView indexQuestionTextView, timerTextView;
    private Button listenButton;

    private Card correctCard;

    // Liste des questions raté par le joueur
    private ArrayList<Question> wrongQuestions = new ArrayList<>();


    public int score = 0;
    public int roundNumber = 0;
    public int maxRoundNumber = 2;
    public Arena arena;
    public int currentTimePerQuestion = 5;
    public int timePerQuestion = 5;
    Handler TimerHandler;
    Runnable TimerRunnable;
    boolean easterEgg;

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
        this.arena = srcIntent.getParcelableExtra("arena");
        this.easterEgg = srcIntent.getBooleanExtra("easterEgg", false);

        // set background
        ImageView backgroundDifficultyImageView = findViewById(R.id.backgroundDifficultyImageView);
        backgroundDifficultyImageView.setImageResource(arena.getBackgroundImage());

        // Home button
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            stop_timer();
            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Initialize UI
        listenButton = findViewById(R.id.lissenbutton);
        emoteencadre = findViewById(R.id.emoteencadre);
        emotereaction = findViewById(R.id.emotereaction);
        type_response = findViewById(R.id.type_response);
        response1 = findViewById(R.id.response1);
        response2 = findViewById(R.id.response2);
        response3 = findViewById(R.id.response3);
        indexQuestionTextView = findViewById(R.id.indexQuestionTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // Hide reactions at start
        emoteencadre.setVisibility(View.GONE);
        emotereaction.setVisibility(View.GONE);
        type_response.setVisibility(View.GONE);

        indexQuestionTextView.setText(roundNumber + "/" + maxRoundNumber);

        if (easterEgg){
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

    // Handle click
    private void handleClick(Card card, ImageButton clickedButton) {
        stop_timer();

        boolean correct = card == correctCard;
        showReaction(correct, clickedButton);

        roundNumber++;
        indexQuestionTextView.setText(roundNumber + "/" + maxRoundNumber);

        // Check if all question are answered
        if (roundNumber < maxRoundNumber) {
            new Handler().postDelayed(this::startNewRound, 2000);
        } else {
            new Handler().postDelayed(this::navigateToVictory, 2000);
        }
    }

    private void setResponseClick(ImageButton button, Card card) {
        button.setOnClickListener(v -> {
            if (easterEgg){
                stop_timer();
            }
            handleClick(card, button);
        });
    }

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
        if (TimerHandler != null && TimerRunnable != null) {
            TimerHandler.removeCallbacks(TimerRunnable);
        }
    }

    // Timer Logic
    private void timer() {
        Card FalseCard = new Card(0, 0);

        if (currentTimePerQuestion <= 0) {
            timerTextView.setText(currentTimePerQuestion + "s");
            handleClick(FalseCard, null);
            currentTimePerQuestion = timePerQuestion;
        } else {
            timerTextView.setText(currentTimePerQuestion + "s");
            currentTimePerQuestion--;
        }
    }

    // Show reaction (win/lose) and highlight correct/wrong
    // Show reaction (win/lose) and highlight answers
    private void showReaction(boolean correct, ImageButton clickedButton) {

        // Show UI
        emoteencadre.setVisibility(View.VISIBLE);
        emotereaction.setVisibility(View.VISIBLE);
        type_response.setVisibility(View.VISIBLE);

        if (correct) {
            // Correct answer
            emotereaction.setImageResource(R.drawable.emote_win);
            type_response.setImageResource(R.drawable.text_true);
            score++;
        } else {
            // Mauvaise réponse
            emotereaction.setImageResource(R.drawable.emote_lose);
            type_response.setImageResource(R.drawable.text_false);

            // Récupère la liste des cartes du round depuis le GameManager
            List<Card> roundOptions = gameManager.getRoundOptions();

            // Crée un nouvel objet "Question" correspondant au round raté
            Question wrongQuestion = new Question(
                    Arrays.asList(
                            roundOptions.get(0).getImageResId(),
                            roundOptions.get(1).getImageResId(),
                            roundOptions.get(2).getImageResId()
                    ),
                    gameManager.getCorrectCard().getImageResId(),
                    gameManager.getCorrectCard().getAudioResId(),
                    arena
            );

            // Ajoute cette question à la liste des questions ratées
            wrongQuestions.add(wrongQuestion);
        }

        // Highlight correct answer
        if (correctCard == gameManager.getRoundOptions().get(0)) {
            response1.setColorFilter(getResources().getColor(R.color.correct_answer, null));
        } else if (correctCard == gameManager.getRoundOptions().get(1)) {
            response2.setColorFilter(getResources().getColor(R.color.correct_answer, null));
        } else if (correctCard == gameManager.getRoundOptions().get(2)) {
            response3.setColorFilter(getResources().getColor(R.color.correct_answer, null));
        }

        // Highlight wrong choice
        if (clickedButton != null) {
            clickedButton.setColorFilter(getResources().getColor(R.color.wrong_answer, null));
        }
    }

    // Hide UI and reset colors after 2s
        emoteencadre.postDelayed(()->{
        emoteencadre.setVisibility(View.GONE);
        emotereaction.setVisibility(View.GONE);
        type_response.setVisibility(View.GONE);
        response1.clearColorFilter();
        response2.clearColorFilter();
        response3.clearColorFilter();
    }, 2000);

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

    private void navigateToVictory() {
        if (easterEgg || TimerRunnable != null){
            stop_timer();
        }
        //Log.d("TAG", "navigateToVictory: "+ wrongCard.wrongCards);
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", arena.getDifficulty());
        // intent.putExtra("wrongCards", wrongCard);
        intent.putExtra("maxRound", maxRoundNumber);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (easterEgg){
            stop_timer();
        }
        // Release MediaPlayer
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}



