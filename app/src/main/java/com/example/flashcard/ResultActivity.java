package com.example.flashcard;

import android.os.Bundle;

// Import des différentes fonctionnalité utilisé
import android.widget.Button;
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
            String message = "J'ai terminé le quiz sur FlashCard, essaie toi aussi ! ";

            // Partage via l' Intent
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, message);

            // lanceur des différentes appli dispo
            startActivity(Intent.createChooser(shareIntent, "Partagez via"));
        });
    }
}