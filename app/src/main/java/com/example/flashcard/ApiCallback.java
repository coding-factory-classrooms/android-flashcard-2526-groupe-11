package com.example.flashcard;

import java.util.List;

public interface ApiCallback {
    void onSuccess(List<Card> result);
    void onError(Exception e);
}
