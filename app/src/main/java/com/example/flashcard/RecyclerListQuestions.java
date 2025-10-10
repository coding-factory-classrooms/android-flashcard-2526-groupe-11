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
        // Get item_question.xml View in variable
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_question,
                parent,
                false);

        // ViewHolder created using layout of item_question.xml
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Edit each item_question.xml element to set buttons functions
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card question = questions.get(position);
        MediaPlayer mediaPlayer = MediaPlayer.create(holder.SoundButton.getContext(), question.getAudioResId(holder.SoundButton.getContext()));
        holder.SoundButton.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        });
        holder.answerView.setOnClickListener(v -> {
            holder.answerView.setImageResource(question.getImageResId(v.getContext()));
        });
    }


    @Override
    public int getItemCount() {
        return questions.size();
    }

    // ViewHolder class with variables to store a questions' answer and sound
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
