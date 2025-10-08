package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

public class Arena implements Parcelable {
    private int image;
    private String difficulty;
    private int BackgroundImage;

    public Arena(int image, String difficulty, int backgroundImage) {
        this.image = image;
        this.difficulty = difficulty;
        BackgroundImage = backgroundImage;
    }

    protected Arena(Parcel in) {
        image = in.readInt();
        difficulty = in.readString();
        BackgroundImage = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(difficulty);
        dest.writeInt(BackgroundImage);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Arena> CREATOR = new Creator<Arena>() {
        @Override
        public Arena createFromParcel(Parcel in) {
            return new Arena(in);
        }

        @Override
        public Arena[] newArray(int size) {
            return new Arena[size];
        }
    };

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getBackgroundImage() {
        return BackgroundImage;
    }

    public void setBackgroundImage(int backgroundImage) {
        BackgroundImage = backgroundImage;
    }
}
