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

        // Button to return to the home page
        ImageButton homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        TextView appVersion = findViewById(R.id.appVersion);

        // Retrieve and display the app version automatically from the Manifest
        try {
            // Get the system Package Manager
            PackageManager manager = getPackageManager();
            // Get the app info
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            // Get and display the app version
            String versionName = info.versionName;
            appVersion.setText("Version : " + versionName);

        } // If the info is not found
        catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appVersion.setText("Unknown version");
        }
    }
}
