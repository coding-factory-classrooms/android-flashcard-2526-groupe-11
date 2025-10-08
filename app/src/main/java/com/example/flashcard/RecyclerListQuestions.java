package com.example.flashcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RecyclerListQuestions extends RecyclerView.Adapter<RecyclerListQuestions.ViewHolder> {
    ArrayList<String> questions;

    public RecyclerListQuestions(ArrayList<String> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Code relou pour récupérer notre item_currency.xml
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.activity_list_questions,
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
        String question = questions.get(position);
        holder.questionTextView.setText(question);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    // Ce sont des instances de ViewHolder qui vont être recyclés lorsqu'on scroll
    // Un ViewHolder fait le lien avec un item_currency.xml
    // Si j'ai 10 items visibles a l'écran, j'aurais 10 instances de ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.textView);
        }
    }
}
