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
import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class PlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private GameManager gameManager;

    private ImageButton response1, response2, response3;
    private ImageView emoteencadre, emotereaction, type_response;
    private TextView indexQuestionTextView, timerTextView;
    private Button listenButton;

    private Card correctCard;

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

        //Initialisiation d'un objet de la classe Api
        Api api = new Api();

        //Appel de la fonction getApi
        //En argument il y a un nouvel élément de l'interface ApiCallback afin de récupérer les données de l'api sur un thread réseau pour éviter d'être arrété par android
        api.getApi("https://students.gryt.tech/api/L2/clashroyaleblindtest/", new ApiCallback() {
            @Override
            //Réécriture de la fonction onSuccess de l'interface afin d'appeler les fonctions de PlayActivity
            public void onSuccess(String result) {
                //result renvoie le json de l'API, on récupère les données sous forme de liste avec Gson
                //On passe ensuite cette liste dans un élément GameManager
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Card>>(){}.getType();
                List<Card> allCards = gson.fromJson(result, listType);
                Log.d("AledO","Allez : "+ allCards.get(0).audioResId);
                gameManager = new GameManager(allCards,getBaseContext());

                //Utilisation de runOnUiThread pour pouvoir accéder à l'UI du thread principal (sinon erreurs)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

    // Start a new round
    private void startNewRound() {
        if (easterEgg)
        {
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
        response1.setImageResource(roundOptions.get(0).getImageResId(getBaseContext()));
        response2.setImageResource(roundOptions.get(1).getImageResId(getBaseContext()));
        response3.setImageResource(roundOptions.get(2).getImageResId(getBaseContext()));

        // Set audio for the correct card
        mediaPlayer = MediaPlayer.create(this, correctCard.getAudioResId(getBaseContext()));

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
    private void handleClick(Card card) {
        stop_timer();

        if (card == correctCard) {
            showReaction(true);
        } else {
            // ajoute la liste question raté la question

            showReaction(false);
        }

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
            handleClick(card);
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
        Card FalseCard = new Card("0", "0");

        if (currentTimePerQuestion <= 0) {
            timerTextView.setText(currentTimePerQuestion + "s");
            handleClick(FalseCard);
            currentTimePerQuestion = timePerQuestion;
        } else {
            timerTextView.setText(currentTimePerQuestion + "s");
            currentTimePerQuestion--;
        }
    }

    // Show reaction (win/lose)
    private void showReaction(boolean correct) {
        emoteencadre.setVisibility(View.VISIBLE);
        emotereaction.setVisibility(View.VISIBLE);
        type_response.setVisibility(View.VISIBLE);

        if (correct) {
            emotereaction.setImageResource(R.drawable.emote_win);
            type_response.setImageResource(R.drawable.text_true);
            score++;
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
    private void navigateToVictory()
    {
        if (easterEgg || TimerRunnable != null){
            stop_timer();
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("difficulty", arena.getDifficulty());
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
