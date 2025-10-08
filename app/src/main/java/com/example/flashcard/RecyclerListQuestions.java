package com.example.flashcard;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerListQuestions extends RecyclerView.Adapter<RecyclerListQuestions.ViewHolder> {
    ArrayList<Card> questions;

    public RecyclerListQuestions(ArrayList<Card> questions) {
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
        Card question = questions.get(position);
        MediaPlayer mediaPlayer = MediaPlayer.create(holder.SoundButton.getContext(), question.getAudioResId());
        holder.SoundButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });
        holder.answerView.setOnClickListener(v -> {
            holder.answerView.setImageResource(question.getImageResId());
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
        ImageView answerView;
        Button SoundButton;
        ImageView DifficultyImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerView = itemView.findViewById(R.id.AnswerImage);
            SoundButton = itemView.findViewById(R.id.SoundButton);
            DifficultyImage = itemView.findViewById(R.id.DifficultyImage);
        }
    }
}
