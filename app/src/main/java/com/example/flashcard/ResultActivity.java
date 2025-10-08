package com.example.flashcard;

import android.os.Bundle;

// Import des différentes fonctionnalité utilisé
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

    // Déclaration du bouton
    private Button restartButton;
    private Button shareButton;

    private TextView choiceDifficult;
    private TextView messageResult;
    private TextView scoreResult;
    private ImageView resultImage;
    private int bonneReponses;
    private int totalQuestions;

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

        // Recupere info extra depuis PlayActivity
        String difficulty = getIntent().getStringExtra("difficult");
        totalQuestions = getIntent().getIntExtra("totalquestionns", 0);
        bonneReponses = getIntent().getIntExtra("bonneRéponse", 0);

        // code debug
        difficulty = "facile";
        totalQuestions = 10;
        bonneReponses = 9;

        // Récupère le niveau choisi via l'id

        choiceDifficult = findViewById(R.id.difficultyTextView);

        // Affichage de la difficulté en fonction du choix du joueur
        choiceDifficult.setText(difficulty);

        // Affichage du score du joueur

        scoreResult = findViewById(R.id.scoreTextView);

        scoreResult.setText("Score : " + bonneReponses + "/" + totalQuestions);

        // Afficher une image et un petit message différents en fonction du resultat en utilisant if/else if

        resultImage = findViewById(R.id.resultImageView);
        messageResult = findViewById(R.id.messageTextView);


        // Récupere le pourcentage du score : en utilisant float afin de recuper le score en décimal et le *100

        float pourcentage = ((float) bonneReponses / totalQuestions * 100);

        // On applique les conditions

        if (pourcentage < 50) {
            resultImage.setImageResource(R.drawable.loss);
            messageResult.setText("Entraine toi !");
        } else if (pourcentage < 80) {
            resultImage.setImageResource(R.drawable.card_hog_rider);
            messageResult.setText("Bien joué");
        }else {
            resultImage.setImageResource(R.drawable.emote_win);
            messageResult.setText("Excellent !");
        }


        // Recupere le bouton rejouer via l'id
        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Boutton rejouer cliqué");

            // Renvoie vers la page d'acceuil lors du clique sur le boutton rejouer
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Récupere le bouton partage via l'id
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Boutton partagez cliqué");

            // Message a partagez
            String message = "J'ai fait : " + bonneReponses + " / " + totalQuestions + "au Flashcard, essaie toi aussi !";

            // Partage via l' Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            // lanceur des différentes appli dispo
            startActivity(Intent.createChooser(shareIntent, "Partagez via"));
        });
    }
}