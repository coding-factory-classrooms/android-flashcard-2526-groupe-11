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
        ArrayList<Card> questions = new ArrayList<>();
        List<Integer> Answers = new ArrayList<Integer>();
        Answers.add(R.drawable.card_prince);
        Answers.add(R.drawable.card_hog_rider);
        Answers.add(R.drawable.card_valkiry);
        for (int i = 0; i < 3000; i++) {
            questions.add(new Card(R.drawable.card_hog_rider,R.raw.hog_rider_voice));
            questions.add(new Card(R.drawable.card_goblins,R.raw.gobelins_voice));
            questions.add(new Card(R.drawable.card_mini_pekka,R.raw.mini_pekka_voice));
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
