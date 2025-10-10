package com.example.flashcard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ListQuestionsActivity extends AppCompatActivity {
    ArrayList<Card> AllCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_questions);

        Button list = findViewById(R.id.HomeButton);
        list.setOnClickListener(view-> {
                Intent newIntent = new Intent(this, MainActivity.class);
                startActivity(newIntent);
        });

        //Cartes récupérées depuis le MainActivity
        Intent srcIntent = getIntent();
        this.AllCards = srcIntent.getParcelableArrayListExtra("Questions");


        //Cards without sound are removed from the list
        ArrayList<Card> cardsWithSound = new ArrayList<>();
        for(Card card : AllCards){
            if (card.audioResId != null){
                cardsWithSound.add(card);
            }
        }

        //Passing the list inside the RecyclerView of ListQuestions
        RecyclerListQuestions adapter = new RecyclerListQuestions(cardsWithSound);

        RecyclerView recyclerView = findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));


    }

}
