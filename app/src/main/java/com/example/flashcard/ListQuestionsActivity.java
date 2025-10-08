package com.example.flashcard;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListQuestionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_questions);
        // Je crée en dur 15000 currencies pour avoir des trucs a afficher
        // Ca peut venir d'une API, un DB, etc
        ArrayList<String> questions = new ArrayList<>();
        Log.d("ListQuestionsActivity","LE ROI?");
        for (int i = 0; i < 3000; i++) {
            questions.add("LE ROI");
        }
        Log.d("ListQuestionsActivity","LE ROI!");
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
