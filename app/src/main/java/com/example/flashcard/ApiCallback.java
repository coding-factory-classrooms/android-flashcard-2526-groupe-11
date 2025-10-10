package com.example.flashcard;

import java.util.ArrayList;
import java.util.List;

public interface ApiCallback {
    void onSuccess(ArrayList<Card> result);
    void onError(Exception e);
}
