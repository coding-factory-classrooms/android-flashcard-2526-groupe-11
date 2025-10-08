package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_questions);

        Button list = findViewById(R.id.HomeButton);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(v.getContext(), MainActivity.class);
                startActivity(newIntent);
            }
        });

        // Je crée en dur 3000 currencies pour avoir des trucs a afficher
        // Ca peut venir d'une API, un DB, etc
        ArrayList<Question> questions = new ArrayList<>();
        List<Integer> Answers = new ArrayList<Integer>();
        Answers.add(R.drawable.card_prince);
        Answers.add(R.drawable.card_hog_rider);
        Answers.add(R.drawable.card_valkiry);
        for (int i = 0; i < 3000; i++) {
            questions.add(new Question(Answers,R.drawable.card_hog_rider,R.raw.hog_rider_voice,new Arena(R.drawable.cr_arene_hard,"Difficile", R.drawable.backgraound_level_three)));
            questions.add(new Question(Answers,R.drawable.card_hog_rider,R.raw.gobelins_voice,new Arena(R.drawable.cr_arene_medium,"Moyen", R.drawable.backgraound_level_two)));
            questions.add(new Question(Answers,R.drawable.card_hog_rider,R.raw.mini_pekka_voice,new Arena(R.drawable.cr_arene_easy,"Facile", R.drawable.backgraound_level_one)));
        }
        // On branche tout le monde
        // les données a l'adapter
        // l'adapter au recyclerview
        RecyclerListQuestions adapter = new RecyclerListQuestions(questions);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("ListQuestionsActivity","LE PROBLEME");
    }

}
