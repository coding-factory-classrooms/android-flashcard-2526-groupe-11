package com.example.flashcard;

import android.os.Bundle;

// Import des différentes fonctionnalités utilisées
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

    // Déclaration des boutons
    private Button restartButton;
    private Button shareButton;
    // Déclaration des variables utilisé
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

        // Récupère les infos "extra" depuis PlayActivity
        String difficulty = getIntent().getStringExtra("difficult");
        totalQuestions = getIntent().getIntExtra("totalquestionns", 0);
        bonneReponses = getIntent().getIntExtra("bonneRéponse", 0);

        // Code de debug
        difficulty = "facile";
        totalQuestions = 10;
        bonneReponses = 9;

        // Récupère et affiche la difficulté choisi via l'id
        choiceDifficult = findViewById(R.id.difficultyTextView);
        choiceDifficult.setText(difficulty);

        // Affichage du score du joueur
        scoreResult = findViewById(R.id.scoreTextView);
        scoreResult.setText("Score : " + bonneReponses + "/" + totalQuestions);

        // Affiche une image et un petit message différent en fonction du résultat en utilisant if/else if
        resultImage = findViewById(R.id.resultImageView);
        messageResult = findViewById(R.id.messageTextView);

        // Récupère le pourcentage du score : en utilisant float afin de récupérer le score en décimal et le *100
        float pourcentage = ((float) bonneReponses / totalQuestions * 100);

        // On applique les conditions et affiche les différents messages
        if (pourcentage < 50) {
            resultImage.setImageResource(R.drawable.loss);
            messageResult.setText("Entraîne-toi !");
        } else if (pourcentage < 80) {
            resultImage.setImageResource(R.drawable.card_hog_rider);
            messageResult.setText("Bien joué !");
        } else {
            resultImage.setImageResource(R.drawable.emote_win);
            messageResult.setText("Excellent !");
        }

        // Récupère le bouton "Rejouer" via l'id
        restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Bouton rejouer cliqué");

            // Renvoie vers la page d’accueil lors du clic sur le bouton "Rejouer"
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        // Récupère le bouton "Partager" via l'id
        shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> {
            Log.d("ResultActivity", "Bouton partager cliqué");

            // Message à partager
            String message = "J'ai fait : " + bonneReponses + " / " + totalQuestions + " au Flashcard, essaie toi aussi !";

            // Partage via l'Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            // Lanceur des différentes applis disponibles
            startActivity(Intent.createChooser(shareIntent, "Partagez via"));
        });
    }
}
