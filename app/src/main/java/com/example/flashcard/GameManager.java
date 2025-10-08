package com.example.flashcard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameManager {
    private List<Card> allCards; // all the cards in the game
    private Card correctCard;    // the correct answer card
    private List<Card> roundOptions; // cards displayed in the current round

    public GameManager() {
        allCards = new ArrayList<>();

        allCards.add(new Card(R.drawable.card_goblins, R.raw.gobelins_voice));
        allCards.add(new Card(R.drawable.card_hog_rider, R.raw.hog_rider_voice));
        allCards.add(new Card(R.drawable.card_mini_pekka, R.raw.mini_pekka_voice));
        allCards.add(new Card(R.drawable.card_prince, 0));
        allCards.add(new Card(R.drawable.card_valkiry, 0));
    }

    public void startNewRound() {
        Random random = new Random();

        // keep only the cards that have audio
        List<Card> cardsWithAudio = new ArrayList<>();
        for (Card card : allCards) {
            if (card.hasAudio()) {
                cardsWithAudio.add(card);
            }
        }

        // select one card with audio as the correct answer
        correctCard = cardsWithAudio.get(random.nextInt(cardsWithAudio.size()));

        // create a new list for this round + add the correct card
        roundOptions = new ArrayList<>();
        roundOptions.add(correctCard);

        // pick 2 additional cards
        while (roundOptions.size() < 3) {
            Card candidate = allCards.get(random.nextInt(allCards.size()));
            if (!roundOptions.contains(candidate)) {
                roundOptions.add(candidate);
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
