package com.example.flashcard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArenaAdapter extends RecyclerView.Adapter<ArenaAdapter.ViewHolder>{

    ArrayList<Arena> arenas;
    Arena chooseArena;


    public ArenaAdapter(ArrayList<Arena> arenas) {
        this.arenas = arenas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Pour récupérer notre item_currency.xml
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_arena,
                parent, false);
        View viewMain = LayoutInflater.from(context).inflate(R.layout.activity_main,
                parent, false);


        // On crée notre instance de ViewHolder qui sera recyclée a chaque scroll de l'utilisateur
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Arena arena = arenas.get(position);
        holder.arenaChooseImageButton.setImageResource(arena.getImage());
        holder.difficultyTextView.setText(arena.getDifficulty());

        holder.arenaChooseImageButton.setOnClickListener(view->{

            Log.d("Difficulty", "Click:" + arena.getDifficulty() + arena.getImage());
            chooseArena = new Arena(arena.getImage(), arena.getDifficulty());
            Log.d("Difficulty", "onBindViewHolder: " + chooseArena);
        });

    }

    @Override
    public int getItemCount() {
        return arenas.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        Group FirstMainGroup;
        ImageButton arenaChooseImageButton;
        TextView difficultyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            arenaChooseImageButton = itemView.findViewById(R.id.arenaChooseImageButton);
            difficultyTextView = itemView.findViewById(R.id.difficultyTextView);

        }
    }
}
