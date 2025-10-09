package com.example.flashcard;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
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
import java.util.Objects;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    private ImageButton response1, response2, response3;
    private ImageView emoteFrame, typeResponse;
    private TextView questionIndexTextView, timerTextView;
    private Button listenButton;

    private Card correctCard;

    // Liste des questions raté par le joueur
    private ArrayList<Question> wrongQuestions = new ArrayList<>();

    public int score = 0;
    public int roundNumber = 0;
    public int maxRoundNumber = 5;
    public Arena arena;
    public int currentTimePerQuestion = 5;
    public int timePerQuestion = 5;

    private Handler timerHandler;
    private Runnable timerRunnable;
    private boolean easterEgg;

    private ReactionManager reactionManager;

    private EnvironmentManager environmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);

        // Adjust for system bars
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
            stopTimer();
            startActivity(new Intent(PlayActivity.this, MainActivity.class));
            finish();
        });

        // Initialize UI
        listenButton = findViewById(R.id.lissenbutton);
        emoteFrame = findViewById(R.id.emoteencadre);
        typeResponse = findViewById(R.id.type_response);
        response1 = findViewById(R.id.response1);
        response2 = findViewById(R.id.response2);
        response3 = findViewById(R.id.response3);
        questionIndexTextView = findViewById(R.id.indexQuestionTextView);
        timerTextView = findViewById(R.id.timerTextView);

        // Initialize EnvironmentManager (tower + decor)
        ImageView imageView9 = findViewById(R.id.imageView9);
        ImageView imageView10 = findViewById(R.id.imageView10);
        environmentManager = new EnvironmentManager(imageView9, imageView10);

        // Hide reactions at the start
        emoteFrame.setVisibility(View.GONE);
        typeResponse.setVisibility(View.GONE);

        questionIndexTextView.setText(roundNumber + "/" + maxRoundNumber);

        // Initialize ReactionManager
        reactionManager = new ReactionManager(this, emoteFrame, typeResponse, response1, response2, response3);

        if (easterEgg) {
            timerTextView.setText(currentTimePerQuestion + "s");
            startTimer();
        }

        // Liste des questions a rejouer
        ArrayList<Question> retryQuestions = getIntent().getParcelableArrayListExtra("retryQuestions");

        // --- Convertir les questions ratées en Card ---
        List<Card> retryCards = new ArrayList<>();
        if (retryQuestions != null && !retryQuestions.isEmpty()) {
            for (Question q : retryQuestions) {
                retryCards.add(new Card(q.CorrectAnswer, q.Sound));
            }
        }

        // --- Créer le GameManager avec les questions ratées si elles existent ---
        if (!retryCards.isEmpty()) {
            gameManager = new GameManager(retryCards);
            maxRoundNumber = retryCards.size();
        } else {
            gameManager = new GameManager();
            maxRoundNumber = 5;
        }

        // Random tower and decor at start
        environmentManager.setRandomEnvironment();

        startNewRound();
        startBarrelAnimation();
    }

    private void startNewRound() {
        if (easterEgg) {
            stopTimer();
            currentTimePerQuestion = timePerQuestion;
            timerTextView.setText(currentTimePerQuestion + "s");
            startTimer();
        }

        releaseMediaPlayer();
        gameManager.startNewRound();

        // Change tower and decor each round
        environmentManager.setRandomEnvironment();

        correctCard = gameManager.getCorrectCard();
        List<Card> roundOptions = gameManager.getRoundOptions();
        if (roundOptions.size() < 3) return;

        response1.setImageResource(roundOptions.get(0).getImageResId());
        response2.setImageResource(roundOptions.get(1).getImageResId());
        response3.setImageResource(roundOptions.get(2).getImageResId());

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

        setResponseClick(response1, roundOptions.get(0));
        setResponseClick(response2, roundOptions.get(1));
        setResponseClick(response3, roundOptions.get(2));
    }

    private void setResponseClick(ImageButton button, Card card) {
        button.setOnClickListener(v -> {
            if (easterEgg) stopTimer();
            handleClick(card, button);
        });
    }

    private void handleClick(Card card, ImageButton clickedButton) {
        stopTimer();
        boolean correct = card == correctCard;

        boolean isCorrect = reactionManager.showReaction(correct, clickedButton, correctCard, gameManager.getRoundOptions());
        if (isCorrect) {score++;}
        else{

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
        reactionManager.hideReactionAfterDelay();

        roundNumber++;
        questionIndexTextView.setText(roundNumber + "/" + maxRoundNumber);

        if (roundNumber < maxRoundNumber) {
            new Handler().postDelayed(this::startNewRound, 2000);
        } else {
            new Handler().postDelayed(this::navigateToVictory, 2000);
        }
    }

    private void startTimer() {
        timerHandler = new Handler();
        timerHandler.postDelayed(timerRunnable = new Runnable() {
            @Override
            public void run() {
                timer();
                timerHandler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void stopTimer() {
        if (timerHandler != null && timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);
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
        ImageView skeleton = findViewById(R.id.squelleton);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        skeleton.setX(screenWidth);
        skeleton.setVisibility(View.VISIBLE);
        skeleton.animate()
                .translationX(-skeleton.getWidth())
                .setDuration(2500)
                .withEndAction(() -> {
                    skeleton.setVisibility(View.GONE);
                    new Handler().postDelayed(this::startBarrelAnimation, 4000);
                })
                .start();
    }

    private void navigateToVictory() {
        stopTimer();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", arena.getDifficulty());
        intent.putExtra("maxRound", maxRoundNumber);
        Log.d("test", "navigateToVictory: " + wrongQuestions);
        intent.putParcelableArrayListExtra("wrongQuestions", wrongQuestions);
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
        stopTimer();
        releaseMediaPlayer();
    }
}