package com.example.flashcard;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Bouton pour retourner a la page d'accueil
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        });

        TextView appVersion = findViewById(R.id.appVersion);

        // Récupère et affiche la version de l'app automatiquement depuis le Manifest

        try {
            // Get the package Manager of the system
            PackageManager manager = getPackageManager();
            // Get the infos of my app
            PackageInfo info = manager.getPackageInfo(getPackageName(),0);
            // Get and display the version of my app
            String versionName = info.versionName;
            appVersion.setText("Version : " + versionName);

        } // If the info its not find
        catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
            appVersion.setText("Version inconnue");
        }

    }
}