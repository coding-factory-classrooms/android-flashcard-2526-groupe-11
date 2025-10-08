package com.example.flashcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArenaAdapter extends RecyclerView.Adapter<ArenaAdapter.ViewHolder>{


    public interface OnArenaListener {
        void onArenaSelected(Arena arena);
    }

    ArrayList<Arena> arenas;
    OnArenaListener arenaListener;

    public ArenaAdapter(ArrayList<Arena> arenas, OnArenaListener arenaListener) {
        this.arenaListener = arenaListener;
        this.arenas = arenas;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_arena,
                parent, false);



        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Arena arena = arenas.get(position);
        holder.arenaChooseImageButton.setImageResource(arena.getImage());
        holder.difficultyTextView.setText(arena.getDifficulty());



        holder.arenaChooseImageButton.setOnClickListener(view->{
            arenaListener.onArenaSelected(arena);
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
