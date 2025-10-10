package com.example.flashcard;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GlobalStats implements Json {
    /**
     * Nombre total de quiz joués
     * Nombre total de bonnes réponses / mauvaises réponses
     * Temps total de jeu
     * Temps moyen d'un quiz
     */

    @SerializedName("totalQuiz")
    private int totalQuiz;
    @SerializedName("goodAnswer")
    private int goodAnswer;
    @SerializedName("badAnswer")
    private int badAnswer;
    @SerializedName("totalTimePlay")
    private long totalTimePlay;
    @SerializedName("meanQuizTime")
    private int meanQuizTime;


    public GlobalStats(int totalQuiz, int goodAnswer, int badAnswer, long totalTimePlay, int meanQuizTime) {
        this.totalQuiz = totalQuiz;
        this.goodAnswer = goodAnswer;
        this.badAnswer = badAnswer;
        this.totalTimePlay = totalTimePlay;
        this.meanQuizTime = meanQuizTime;
    }

    // region Getter/Setter
    public int getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(int totalQuiz) {
        this.totalQuiz = totalQuiz;
    }

    public int getGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(int goodAnswer) {
        this.goodAnswer = goodAnswer;
    }

    public int getBadAnswer() {
        return badAnswer;
    }

    public void setBadAnswer(int badAnswer) {
        this.badAnswer = badAnswer;
    }

    public long getTotalTimePlay() {
        return totalTimePlay;
    }

    public void setTotalTimePlay(long totalTimePlay) {
        this.totalTimePlay = totalTimePlay;
    }

    public int getMeanQuizTime() {
        return meanQuizTime;
    }

    public void setMeanQuizTime(int meanQuizTime) {
        this.meanQuizTime = meanQuizTime;
    }
    //endregion

    //region Adder
    public void addTotalQuiz(int value) {
        this.totalQuiz += value;
    }

    public void addGoodAnswer(int value) {
        this.goodAnswer += value;
    }

    public void addBadAnswer(int value) {
        this.badAnswer += value;
    }

    public void addTotalTimePlay(long value) {
        this.totalTimePlay += value;
    }

    public void addMeanQuizTime(int value) {
        this.meanQuizTime += value;
    }
    //endregion

    @Override
    public void jsonWrite(Context context, String fileName) {

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

         GlobalStats globalStats = new GlobalStats(this.totalQuiz, this.goodAnswer,this.badAnswer,this.totalTimePlay,this.meanQuizTime);

        File filePath = new File(context.getFilesDir(), fileName);

        try(FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(globalStats, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public GlobalStats jsonRead(Context context, String fileName) {
        File filePath = new File(context.getFilesDir(), fileName);


        try (FileReader reader = new FileReader(filePath)) {
            return new Gson().fromJson(reader, GlobalStats.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean jsonExist(Context context, String fileName) {
        File filePath = new File(context.getFilesDir(), fileName);
        return filePath.exists();
    }
}
