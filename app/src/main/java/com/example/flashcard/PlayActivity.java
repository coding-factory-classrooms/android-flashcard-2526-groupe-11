package com.example.flashcard;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;
    private ImageButton response1, response2, response3, response4, response5;
    private ImageView emoteFrame, typeResponse;
    private TextView questionIndexTextView, timerTextView;
    private LinearLayout cardLinearLayout, card2LinearLayout;
    private Button listenButton;
    private Card correctCard;
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
        response4 = findViewById(R.id.response4);
        response5 = findViewById(R.id.response5);
        questionIndexTextView = findViewById(R.id.indexQuestionTextView);
        timerTextView = findViewById(R.id.timerTextView);
        cardLinearLayout = findViewById(R.id.cardLinearLayout);
        card2LinearLayout = findViewById(R.id.card2LinearLayout);

        // Initialize EnvironmentManager (tower + decor)
        ImageView imageView9 = findViewById(R.id.imageView9);
        ImageView imageView10 = findViewById(R.id.imageView10);
        environmentManager = new EnvironmentManager(imageView9, imageView10);

        // Medium Mode layout (5 answers)
        if (Objects.equals(arena.getDifficulty(), "Moyen")) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) cardLinearLayout.getLayoutParams();
            params.topMargin = 0;
            cardLinearLayout.setLayoutParams(params);
            card2LinearLayout.setVisibility(View.VISIBLE);
        }

        // Hide reactions at the start
        emoteFrame.setVisibility(View.GONE);
        typeResponse.setVisibility(View.GONE);

        questionIndexTextView.setText(roundNumber + "/" + maxRoundNumber);

        // Initialize ReactionManager
        reactionManager = new ReactionManager(this, emoteFrame, typeResponse,
                response1, response2, response3, response4, response5);

        if (easterEgg) {
            timerTextView.setText(currentTimePerQuestion + "s");
            startTimer();
        }

        // Load cards from API
        Api api = new Api();
        api.getApi("https://students.gryt.tech/api/L2/clashroyaleblindtest/", new ApiCallback() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Card>>() {}.getType();
                List<Card> allCards = gson.fromJson(result, listType);
                Log.d("API", "Cards loaded: " + allCards.size());
                gameManager = new GameManager(allCards, getBaseContext());

                runOnUiThread(() -> {
                    environmentManager.setRandomEnvironment();
                    startNewRound();
                    startBarrelAnimation();
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("ErreurAPI", "Erreur : " + e);
            }
        });
    }

    private void startNewRound() {
        if (easterEgg) {
            stopTimer();
            currentTimePerQuestion = timePerQuestion;
            timerTextView.setText(currentTimePerQuestion + "s");
            startTimer();
        }

        releaseMediaPlayer();
        gameManager.startNewRound("Moyen".equals(arena.getDifficulty()) ? 5 : 3);

        // Re-enable buttons
        response1.setEnabled(true);
        response2.setEnabled(true);
        response3.setEnabled(true);
        response4.setEnabled(true);
        response5.setEnabled(true);

        environmentManager.setRandomEnvironment();

        correctCard = gameManager.getCorrectCard();
        List<Card> roundOptions = gameManager.getRoundOptions();
        if (roundOptions.isEmpty()) return;

        mediaPlayer = MediaPlayer.create(this, correctCard.getAudioResId(getBaseContext()));
        if (Objects.equals(arena.getDifficulty(), "Difficile")) {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(500f, 500f);
            PlaybackParams params = new PlaybackParams();
            params.setPitch(0.2f);
            mediaPlayer.setPlaybackParams(params);
            mediaPlayer.pause();
        }

        // Assign answers
        response1.setImageResource(roundOptions.get(0).getImageResId(getBaseContext()));
        response2.setImageResource(roundOptions.get(1).getImageResId(getBaseContext()));
        response3.setImageResource(roundOptions.get(2).getImageResId(getBaseContext()));
        setResponseClick(response1, roundOptions.get(0));
        setResponseClick(response2, roundOptions.get(1));
        setResponseClick(response3, roundOptions.get(2));

        if (Objects.equals(arena.getDifficulty(), "Moyen") && roundOptions.size() >= 5) {
            response4.setImageResource(roundOptions.get(3).getImageResId(getBaseContext()));
            response5.setImageResource(roundOptions.get(4).getImageResId(getBaseContext()));
            setResponseClick(response4, roundOptions.get(3));
            setResponseClick(response5, roundOptions.get(4));
        }

        listenButton.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.start();
        });
    }

    private void setResponseClick(ImageButton button, Card card) {
        button.setOnClickListener(v -> {
            if (easterEgg) stopTimer();
            handleClick(card);
        });
    }

    private void handleClick(Card card) {
        stopTimer();

        // Disable all buttons to prevent double click
        response1.setEnabled(false);
        response2.setEnabled(false);
        response3.setEnabled(false);
        response4.setEnabled(false);
        response5.setEnabled(false);

        boolean correct = card == correctCard;
        boolean isCorrect = reactionManager.showReaction(correct, null, correctCard, gameManager.getRoundOptions());
        if (isCorrect) score++;
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
        if (timerHandler != null && timerRunnable != null)
            timerHandler.removeCallbacks(timerRunnable);
    }

    private void timer() {
        if (currentTimePerQuestion <= 0) {
            timerTextView.setText("0s");
            handleClick(new Card("0", "0"));
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
        releaseMediaPlayer();
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
        stopTimer();
        releaseMediaPlayer();
    }
}
