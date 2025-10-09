package com.example.flashcard;

import com.google.gson.Gson;

import java.io.FileWriter;

public class Json {

    //method to create a jsonFile
    public void jsonWrite(){
        Gson gson = new Gson();
        gson.toJson(123.45, new FileWriter(filePath));


    }

    // method read jsonfile

    //maybe method to convert to an Object



}
