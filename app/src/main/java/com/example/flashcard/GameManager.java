package com.example.flashcard;


import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class GameManager {
    private List<Card> allCards; // all the cards in the game
    private Card correctCard;    // the correct answer card
    private List<Card> roundOptions; // cards displayed in the current round
    Context context;
    Card SpecificQuestion;

    public GameManager(List<Card> AllCards,Context context,Card specificQuestion) {
        this.allCards = AllCards;
        this.context = context;
        this.SpecificQuestion = specificQuestion;
    }

    public void startNewRound(int size,Question wrongQuestion) {
        Random random = new Random();
        Log.d("ImageMan","eeeeeeeeeeeee");

        // keep only the cards that have audio
        List<Card> cardsWithAudio = new ArrayList<>();
        for (Card card : allCards) {
            Log.d("GameManager",card.getImageResId(context)+"");
            if (card.hasAudio()) {
                cardsWithAudio.add(card);
                Log.d("GameManager",card.getAudioResId(context)+"");
            }
        }

        if(wrongQuestion!= null) {
            correctCard = wrongQuestion.CorrectAnswer;
            roundOptions = new ArrayList<>();
            roundOptions.add(wrongQuestion.CorrectAnswer);

            roundOptions.addAll(wrongQuestion.Answers);
        }
        else {
            if (SpecificQuestion != null) {
                correctCard = SpecificQuestion;
            }
            else {
                // select one card with audio as the correct answer
                correctCard = cardsWithAudio.get(random.nextInt(cardsWithAudio.size()));
            }


            // create a new list for this round + add the correct card
            roundOptions = new ArrayList<>();
            roundOptions.add(correctCard);

            Log.d("ImageMan", size + "");
            // pick 2 additional cards
            while (roundOptions.size() < size) {
                Card candidate = allCards.get(random.nextInt(allCards.size()));
                if (!roundOptions.contains(candidate) && !Objects.equals(candidate.audioResId, correctCard.audioResId)) {
                    roundOptions.add(candidate);
                }
            }
        }

        // shuffle the order of the cards
        Collections.shuffle(roundOptions);
    }
    public Card getCorrectCard() {
        return correctCard;
    }

    public List<Card> getRoundOptions() {
        return roundOptions;
    }
}
