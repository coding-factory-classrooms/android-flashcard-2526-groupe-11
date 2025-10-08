package com.example.flashcard;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
    Context context;

    ArrayList<Arena> arenas;
    Arena chooseArena = new Arena(R.drawable.cr_arene_easy, "Facile");


    public ArenaAdapter(ArrayList<Arena> arenas, Context context) {
        this.context = context;
        this.arenas = arenas;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Pour récupérer notre item_currency.xml
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_arena,
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

            Log.d("Difficulty", "Click:" + arena.getDifficulty()+ " " + arena.getImage());
            chooseArena.setDifficulty(arena.getDifficulty());
            chooseArena.setImage(arena.getImage());
            Log.d("Difficulty", "onBindViewHolder: " + chooseArena);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("arena", chooseArena);
            startActivity(context, intent, null);
        });

    }

    @Override
    public int getItemCount() {
        return arenas.size();
    }




    class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton arenaChooseImageButton;
        TextView difficultyTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);




            arenaChooseImageButton = itemView.findViewById(R.id.arenaChooseImageButton);
            difficultyTextView = itemView.findViewById(R.id.difficultyTextView);

        }
    }
}
