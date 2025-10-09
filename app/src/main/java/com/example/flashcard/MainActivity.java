package com.example.flashcard;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements ArenaAdapter.OnArenaListener, ShakeDetector.Listener {

    public static final String TAG = "MainActivity";
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    public Arena selectedArena =  new Arena(R.drawable.cr_arene_easy, "Facile", R.drawable.backgraound_level_one);
    private boolean easterEgg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager == null) {
            Log.e(TAG, "SensorManager est null — l’appareil ne supporte pas les capteurs ?");
        }

        // Initialise le ShakeDetector (avec this comme Listener)
        mShakeDetector = new ShakeDetector(this);

        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);





        // Navigate to ListQuestionsActivity
        Button list = findViewById(R.id.questionButton);
        list.setOnClickListener(v -> {
            Intent newIntent = new Intent(MainActivity.this, ListQuestionsActivity.class);
            startActivity(newIntent);
        });

        // Navigate to PlayActivity
        Button battleButton = findViewById(R.id.battleButton);
        battleButton.setOnClickListener(view -> {
            Arena arena = new Arena(selectedArena.getImage(), selectedArena.getDifficulty(), selectedArena.getBackgroundImage());

            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra("arena", arena);
            if (easterEgg){
                intent.putExtra("easterEgg", true);
            }
            startActivity(intent);
        });


        arenaImageButton.setImageResource(selectedArena.getImage());
        difficultyArenaTextView.setText(selectedArena.getDifficulty());



        // Display recyclerView
        arenaImageButton.setOnClickListener(view->{
            RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);

            firstMainGroup.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            ArrayList<Arena> arenas = new ArrayList<>();
            ArenaAdapter adapter = new ArenaAdapter(arenas, this);

            arenas.clear();
            arenas.add(new Arena(R.drawable.cr_arene_easy,"Facile", R.drawable.backgraound_level_one));
            arenas.add(new Arena(R.drawable.cr_arene_medium,"Moyen", R.drawable.backgraound_level_two));
            arenas.add(new Arena(R.drawable.cr_arene_hard,"Difficile", R.drawable.backgraound_level_three));


            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShakeDetector != null && mSensorManager != null) {
            // Démarrer la détection de shake ici, avec un delay raisonnable
            mShakeDetector.start(mSensorManager, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Log.e(TAG, "Impossible de démarrer ShakeDetector — instance ou sensorManager est nul");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mShakeDetector != null) {
            mShakeDetector.stop();
        }
    }
    @Override
    public void onArenaSelected(Arena arena){
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);
        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);

        this.selectedArena = arena;

        arenaImageButton.setImageResource(arena.getImage());
        difficultyArenaTextView.setText(arena.getDifficulty());

        recyclerView.setVisibility(View.INVISIBLE);
        firstMainGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public synchronized void hearShake() {
        boolean actualBoolean = easterEgg;
        easterEgg = !actualBoolean;
        String message = "";
        if (easterEgg){
            message = "Activé";
        }
        else{
            message = "Désactivé";
        }

        Toast.makeText(this, "Easter Egg "+message, Toast.LENGTH_SHORT).show();


    }
}



