package com.example.flashcard;

public interface ApiCallback {
    void onSuccess(String result);
    void onError(Exception e);
}
