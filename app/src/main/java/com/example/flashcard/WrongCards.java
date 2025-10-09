package com.example.flashcard;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WrongCards implements Parcelable {
    List<List<Card>> wrongCards = new ArrayList<>();


    protected WrongCards(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WrongCards> CREATOR = new Creator<WrongCards>() {
        @Override
        public WrongCards createFromParcel(Parcel in) {
            return new WrongCards(in);
        }

        @Override
        public WrongCards[] newArray(int size) {
            return new WrongCards[size];
        }
    };
}
