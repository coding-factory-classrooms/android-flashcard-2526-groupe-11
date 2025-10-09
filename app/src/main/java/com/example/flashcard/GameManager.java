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
        allCards.add(new Card(R.drawable.card_prince, R.raw.prince_voice));
        allCards.add(new Card(R.drawable.card_valkiry, R.raw.valkyrie_voice));
        allCards.add(new Card(R.drawable.card_bats, R.raw.bats_voice));
        allCards.add(new Card(R.drawable.card_bomber, R.raw.bomber_voice));
        allCards.add(new Card(R.drawable.card_elite_barbarians, R.raw.elite_barbarians_voice));
        allCards.add(new Card(R.drawable.card_firecracker, R.raw.firecracker_voice));
        allCards.add(new Card(R.drawable.card_wizard, R.raw.wizard_voice));
        allCards.add(new Card(R.drawable.card_minion_horde, R.raw.minion_horde_voice));
        allCards.add(new Card(R.drawable.archer_queen, R.raw.archer_queen_voice));
        allCards.add(new Card(R.drawable.boss_bandit_card_frame_alpha, 0));
        allCards.add(new Card(R.drawable.card_battle_ram_evolution_01, R.raw.evo_battle_ram_voice));
        allCards.add(new Card(R.drawable.card_dagger_duchess_tower_troop_frame, R.raw.dagger_duchess_voice));
        allCards.add(new Card(R.drawable.card_dark_prince, R.raw.dark_prince_voice));
        allCards.add(new Card(R.drawable.card_electro_giant, R.raw.electro_giant_voice));
        allCards.add(new Card(R.drawable.card_electro_wizard, R.raw.electro_wizard_voice));
        allCards.add(new Card(R.drawable.card_electro_sprit, R.raw.electro_spirit_voice));
        allCards.add(new Card(R.drawable.card_fire_spirit, R.raw.fire_spirit_voice));
        allCards.add(new Card(R.drawable.card_fireball, 0));
        allCards.add(new Card(R.drawable.card_giant_bomber, R.raw.giant_skeleton_voice));
        allCards.add(new Card(R.drawable.card_goblin_gang, R.raw.goblin_gang_voice));
        allCards.add(new Card(R.drawable.card_goblin_machine_01, 0));
        allCards.add(new Card(R.drawable.card_ice_golem_super, 0));
        allCards.add(new Card(R.drawable.card_ice_spirit_evolution_frame, R.raw.evo_ice_spirit_voice));
        allCards.add(new Card(R.drawable.card_lumberjack, R.raw.lumberjack_voice));
        allCards.add(new Card(R.drawable.card_magic_archer_super, 0));
        allCards.add(new Card(R.drawable.card_magic_archer, 0));
        allCards.add(new Card(R.drawable.card_mega_knight, 0));
        allCards.add(new Card(R.drawable.card_monk, R.raw.monk_voice));
        allCards.add(new Card(R.drawable.card_mother_witch, R.raw.mother_witch_voice));
        allCards.add(new Card(R.drawable.card_pheonix, R.raw.phoenix_voice));
        allCards.add(new Card(R.drawable.card_princess, R.raw.princess_voice));
        allCards.add(new Card(R.drawable.card_skeleton_army, R.raw.skeleton_army_voice));
        allCards.add(new Card(R.drawable.card_skeleton_dragons, R.raw.skeleton_dragons_voice));
        allCards.add(new Card(R.drawable.card_tesla, R.raw.tesla_voice));
        allCards.add(new Card(R.drawable.card_the_log, R.raw.log_voice));
        allCards.add(new Card(R.drawable.card_three_musketeers, 0));
        allCards.add(new Card(R.drawable.executioner_evolution_card_alpha, 0));

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
