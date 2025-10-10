package com.example.flashcard;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Card implements Parcelable {
    @SerializedName("imageResId")
    public String imageResId;   // Image of the card
    @SerializedName("audioResId")
    public String audioResId;   // Associated audio (0 if none)

    public Card(String imageResId, String audioResId) {
        this.imageResId = imageResId;
        this.audioResId = audioResId;
    }

    protected Card(Parcel in) {
        imageResId = in.readString();
        audioResId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageResId);
        dest.writeString(audioResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Card> CREATOR = new Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

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
