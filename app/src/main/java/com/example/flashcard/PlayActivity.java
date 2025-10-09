package com.example.flashcard;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

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

        // Medium Mode layout
        if (Objects.equals(arena.getDifficulty(), "Moyen"))
        {
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
        reactionManager = new ReactionManager(this, emoteFrame, typeResponse, response1, response2, response3, response4, response5);

        if (easterEgg) {
            timerTextView.setText(currentTimePerQuestion + "s");
            startTimer();
        }

        //Initialisiation d'un objet de la classe Api
        Api api = new Api();

        //Appel de la fonction getApi
        //En argument il y a un nouvel élément de l'interface ApiCallback afin de récupérer les données de l'api sur un thread réseau pour éviter d'être arrété par android
        api.getApi("https://students.gryt.tech/api/L2/clashroyaleblindtest/", new ApiCallback() {
            @Override
            //Réécriture de la fonction onSuccess de l'interface afin d'appeler les fonctions de PlayActivity
            public void onSuccess(List<Card> result) {
                //result renvoie le json de l'API, on récupère les données sous forme de liste avec Gson
                //On passe ensuite cette liste dans un élément GameManager

                gameManager = new GameManager(result,getBaseContext());

                //Utilisation de runOnUiThread pour pouvoir accéder à l'UI du thread principal (sinon erreurs)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Random tower and decor at start
                        environmentManager.setRandomEnvironment();
                        startNewRound();
                        startBarrelAnimation();
                    }
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

        // Change tower and decor each round
        environmentManager.setRandomEnvironment();

        correctCard = gameManager.getCorrectCard();
        List<Card> roundOptions = gameManager.getRoundOptions();
        if (roundOptions.size() < 3) return;



        mediaPlayer = MediaPlayer.create(this, correctCard.getAudioResId(getBaseContext()));
        if (Objects.equals(arena.getDifficulty(), "Difficile")){
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setVolume(500f, 500f);
            PlaybackParams params = new PlaybackParams();
            params.setPitch(0.2f);
            mediaPlayer.setPlaybackParams(params);
            mediaPlayer.pause();
        }

        response1.setImageResource(roundOptions.get(0).getImageResId(getBaseContext()));
        response2.setImageResource(roundOptions.get(1).getImageResId(getBaseContext()));
        response3.setImageResource(roundOptions.get(2).getImageResId(getBaseContext()));
        listenButton.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.start();
        });

        setResponseClick(response1, roundOptions.get(0));
        setResponseClick(response2, roundOptions.get(1));
        setResponseClick(response3, roundOptions.get(2));

        if (Objects.equals(arena.getDifficulty(), "Moyen") && roundOptions.size() >=5)
        {
            response4.setImageResource(roundOptions.get(3).getImageResId(getBaseContext()));
            response5.setImageResource(roundOptions.get(4).getImageResId(getBaseContext()));
            setResponseClick(response4, roundOptions.get(3));
            setResponseClick(response5, roundOptions.get(4));
        }





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
        if (timerHandler != null && timerRunnable != null) timerHandler.removeCallbacks(timerRunnable);
    }

    private void timer() {
        if (currentTimePerQuestion <= 0) {
            timerTextView.setText("0s");
            handleClick(new Card("0", "0"), null);
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
