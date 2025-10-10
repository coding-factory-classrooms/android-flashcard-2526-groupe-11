package com.example.flashcard;

import android.content.Context;


interface Json {

    void jsonWrite(Context context, String fileName);

    Json jsonRead(Context context, String fileName);
    boolean jsonExist(Context context, String fileName);

}
