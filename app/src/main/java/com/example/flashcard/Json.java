package com.example.flashcard;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

interface Json {

    void jsonWrite(Context context, String fileName);

    Json jsonRead(Context context, String fileName);
    boolean jsonExist(Context context, String fileName);

    //maybe method to convert to an Object



}
