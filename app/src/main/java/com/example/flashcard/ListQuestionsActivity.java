package com.example.flashcard;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
        for (int i = 0; i < 1; i++) {
            questions.add("LE ROI");
        }

        // On branche tout le monde
        // les données a l'adapter
        // l'adapter au recyclerview
        RecyclerListQuestions adapter = new RecyclerListQuestions(questions);
        RecyclerView recyclerView = findViewById(R.id.QuestionsListRecycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
