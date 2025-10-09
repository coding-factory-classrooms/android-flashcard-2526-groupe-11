package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public class Question implements Parcelable {
    List<Integer> Answers;
    int CorrectAnswer;
    int Sound;
    Arena Difficulty;

    public Question(List<Integer> answers, int correctAnswer, int sound, Arena difficulty) {
        Answers = answers;
        this.CorrectAnswer = correctAnswer;
        Sound = sound;
        Difficulty = difficulty;
    }

    protected Question(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeList(Answers);
        dest.writeInt(CorrectAnswer);
        dest.writeInt(Sound);
        dest.writeString(Difficulty.getDifficulty());
        dest.writeInt(Difficulty.getImage());
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

}
