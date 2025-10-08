package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ArenaAdapter.OnArenaListener {

    public static final String TAG = "Difficulty";

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




        // Changer tout






        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);



        ArrayList<Arena> arenas = new ArrayList<>();
        ArenaAdapter adapter = new ArenaAdapter(arenas, this);

//        Intent srcIntent = getIntent();
//        Arena arenaGet = srcIntent.getParcelableExtra("arena");

        int arenaImage;
        String arenaDifficulty;

         arenaImage = adapter.chooseArena.getImage();
         arenaDifficulty = adapter.chooseArena.getDifficulty();



        arenaImageButton.setImageResource(arenaImage);
        difficultyArenaTextView.setText(arenaDifficulty);





        arenaImageButton.setOnClickListener(view->{
            Log.d(TAG, "Click");
            firstMainGroup.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);
            recyclerView.setVisibility(View.VISIBLE);



            arenas.clear();
            arenas.add(new Arena(R.drawable.cr_arene_easy,"Facile"));
            arenas.add(new Arena(R.drawable.cr_arene_medium,"Moyen"));
            arenas.add(new Arena(R.drawable.cr_arene_hard,"Difficile"));


            // On branche tout le monde
            //  les données à l'adapter
            // l'adapter au recyclerView
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        });
    }
    @Override
    public void onArenaSelected(Arena arena){
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);

        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);
        arenaImageButton.setImageResource(arena.getImage());
        difficultyArenaTextView.setText(arena.getDifficulty());


        recyclerView.setVisibility(View.INVISIBLE);
        firstMainGroup.setVisibility(View.VISIBLE);
        Log.d(TAG, "onArenaSelected: "+arena);

    }
}


