package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

public class Question implements Parcelable {
    List<Card> Answers;
    Card CorrectAnswer;
    Arena Difficulty;

    public Question(List<Card> answers, Card correctAnswer, Arena difficulty) {
        Answers = answers;
        this.CorrectAnswer = correctAnswer;
        Difficulty = difficulty;
    }


    protected Question(Parcel in) {
        Difficulty = in.readParcelable(Arena.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(Difficulty, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
