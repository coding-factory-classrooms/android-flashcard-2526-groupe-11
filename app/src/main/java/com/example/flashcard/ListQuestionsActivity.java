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


        //Api class object initialized
        Api api = new Api();


        //Call of getApi function
        //Our second argument is a new ApiCallback element (Interface) to get data from the API using a background thread to avoid android stopping us
        api.getApi("https://students.gryt.tech/api/L2/clashroyaleblindtest/", new ApiCallback() {
            @Override
            //onSuccess function of ApiCallback Interface modified to edit listQuestions with API data
            public void onSuccess(List<Card> result) {

                //Cards without sound are removed from the list
                ArrayList<Card> cardsWithSound = new ArrayList<>();
                for(Card card : result){
                    if (card.audioResId != null){
                        cardsWithSound.add(card);
                    }
                }

                //Passing the list inside the RecyclerView of ListQuestions
                RecyclerListQuestions adapter = new RecyclerListQuestions(cardsWithSound);
                //runOnUiThread is used to access and modify the UI of the main thread (error if on current thread)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = findViewById(R.id.RecyclerView);

                        recyclerView.setAdapter(adapter);

                        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("ErreurAPI", "Erreur : " + e);
            }
        });
    }

}
