package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements ArenaAdapter.OnArenaListener {

    public static final String TAG = "MainActivity";

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


        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        ImageButton mainBackgroundImagView = findViewById(R.id.mainBackgroundImagView);
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);




        Button list = findViewById(R.id.questionButton);
        list.setOnClickListener(v -> {
            Intent newIntent = new Intent(MainActivity.this, ListQuestionsActivity.class);
            startActivity(newIntent);
        });

        Button battleButton = findViewById(R.id.battleButton);
        battleButton.setOnClickListener(view -> {
            int resIDImage = getResources().getIdentifier("arenaImageButton", "id", getPackageName());
            String difficultyName = difficultyArenaTextView.getText().toString();
//            int resIDBackground = getResources().getIdentifier("mainBackgroundImagView", "id", getPackageName());

            int resIDBackground = 0;
            switch (difficultyName){
                case "Facile":
                    resIDBackground = R.drawable.backgraound_level_one;
                    break;
                case "Moyen":
                    resIDBackground = R.drawable.backgraound_level_two;
                    break;
                case "Difficle":
                    resIDBackground = R.drawable.backgraound_level_three;
                    break;
            }
            Log.d("arena", "onCreate: "+resIDBackground);

            Arena arena = new Arena(resIDImage, difficultyName, resIDBackground);

            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            intent.putExtra("arena", arena);
            startActivity(intent);
        });




        Arena DefaultArena = new Arena(R.drawable.cr_arene_easy, "Facile", R.drawable.backgraound_level_one);
        arenaImageButton.setImageResource(DefaultArena.getImage());
        mainBackgroundImagView.setImageResource(DefaultArena.getBackgroundImage());
        difficultyArenaTextView.setText(DefaultArena.getDifficulty());



        arenaImageButton.setOnClickListener(view->{

            firstMainGroup.setVisibility(View.INVISIBLE);
            RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);
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
    public void onArenaSelected(Arena arena){
        Group firstMainGroup = findViewById(R.id.firstMainGroup);
        RecyclerView recyclerView = findViewById(R.id.arenaRecyclerView);

        ImageButton arenaImageButton = findViewById(R.id.arenaImageButton);
        ImageView mainBackgroundImagView = findViewById(R.id.mainBackgroundImagView);
        TextView difficultyArenaTextView = findViewById(R.id.difficultyArenaTextView);

        arenaImageButton.setImageResource(arena.getImage());
        mainBackgroundImagView.setImageResource(arena.getBackgroundImage());
        difficultyArenaTextView.setText(arena.getDifficulty());
        Log.d("arena", "onArenaSelected: " +arena.getBackgroundImage());


        recyclerView.setVisibility(View.INVISIBLE);
        firstMainGroup.setVisibility(View.VISIBLE);
    }
}



