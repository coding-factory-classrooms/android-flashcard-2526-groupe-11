package com.example.flashcard;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

public class Card {
    @SerializedName("imageResId")
    public String imageResId;   // Image of the card
    @SerializedName("audioResId")
    public String audioResId;   // Associated audio (0 if none)

    public Card(String imageResId, String audioResId) {
        this.imageResId = imageResId;
        this.audioResId = audioResId;
    }

    public int getImageResId(Context context) {
        return context.getResources().getIdentifier(imageResId, "drawable", context.getPackageName());

//        return imageResId;
    }

    public int getAudioResId(Context context) {
        return context.getResources().getIdentifier(audioResId, "raw", context.getPackageName());

//        return audioResId;
    }

    public boolean hasAudio() {
        return audioResId != null;
    }
}
