package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

public class Arena implements Parcelable {
    private int image;
    private String difficulty;

    public Arena(int image, String difficulty) {
        this.image = image;
        this.difficulty = difficulty;
    }

    protected Arena(Parcel in) {
        image = in.readInt();
        difficulty = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(difficulty);
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

    //region Getter/Setter
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
    //endregion
}
