package com.example.flashcard;

public class Card {
    private int imageResId;   // Image of the card
    private int audioResId;   // Associated audio (0 if none)

    public Card(int imageResId, int audioResId) {
        this.imageResId = imageResId;
        this.audioResId = audioResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getAudioResId() {
        return audioResId;
    }

    public boolean hasAudio() {
        return audioResId != 0;
    }
}
