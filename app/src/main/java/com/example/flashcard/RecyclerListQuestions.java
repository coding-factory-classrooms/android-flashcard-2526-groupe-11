package com.example.flashcard;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;


public class RecyclerListQuestions extends RecyclerView.Adapter<RecyclerListQuestions.ViewHolder> {
    ArrayList<Question> questions;

    public RecyclerListQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Code relou pour récupérer notre item_currency.xml
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,
                parent,
                false);

        // On crée notre instance de ViewHolder qui sera recyclée a chaque scroll de l'utilisateur
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // A chaque qu'on veut afficher une ligne, cette fonction est appelée
    // on va modifier les données pour une row de notre RecyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = questions.get(position);
        Collections.shuffle(question.Answers);
        holder.answerView1.setImageResource(question.Answers.get(0));
        holder.answerView2.setImageResource(question.Answers.get(1));
        holder.answerView3.setImageResource(question.Answers.get(2));
        holder.DifficultyImage.setImageResource(question.Difficulty.getImage());
        MediaPlayer mediaPlayer = MediaPlayer.create(holder.SoundButton.getContext(), question.Sound);
        holder.SoundButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });
    }


    @Override
    public int getItemCount() {
        return questions.size();
    }

    // Ce sont des instances de ViewHolder qui vont être recyclés lorsqu'on scroll
    // Un ViewHolder fait le lien avec un item_currency.xml
    // Si j'ai 10 items visibles a l'écran, j'aurais 10 instances de ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView answerView1;
        ImageView answerView2;
        ImageView answerView3;
        Button SoundButton;
        ImageView DifficultyImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerView1 = itemView.findViewById(R.id.imageAnswer1);
            answerView2 = itemView.findViewById(R.id.imageAnswer2);
            answerView3 = itemView.findViewById(R.id.imageAnswer3);
            SoundButton = itemView.findViewById(R.id.SoundButton);
            DifficultyImage = itemView.findViewById(R.id.DifficultyImage);
        }
    }
}
