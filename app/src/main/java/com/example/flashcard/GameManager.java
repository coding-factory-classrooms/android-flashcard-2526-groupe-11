package com.example.flashcard;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameManager {
    private List<Card> allCards; // all the cards in the game
    private Card correctCard;    // the correct answer card
    private List<Card> roundOptions; // cards displayed in the current round
    Context context;

    public GameManager(Context context) {
        allCards = new ArrayList<>();
        this.context = context;
//        for (card in jsonCard){
//
//        }
//

//
//        allCards.add(new Card(context.getResources().getIdentifier("card_goblins", "drawable",
//                context.getPackageName()).getResources().getIdentifier("gobelins_voice", "raw",
//                context.getPackageName())));
//        allCards.add(new Card(R.drawable.card_hog_rider, R.raw.hog_rider_voice));
//        allCards.add(new Card(R.drawable.card_mini_pekka, R.raw.mini_pekka_voice));
//        allCards.add(new Card(R.drawable.card_prince, R.raw.prince_voice));
//        allCards.add(new Card(R.drawable.card_valkiry, R.raw.valkyrie_voice));
//        allCards.add(new Card(R.drawable.card_bats, R.raw.bats_voice));
//        allCards.add(new Card(R.drawable.card_bomber, R.raw.bomber_voice));
//        allCards.add(new Card(R.drawable.card_elite_barbarians, R.raw.elite_barbarians_voice));
//        allCards.add(new Card(R.drawable.card_firecracker, R.raw.firecracker_voice));
//        allCards.add(new Card(R.drawable.card_wizard, R.raw.wizard_voice));
//        allCards.add(new Card(R.drawable.card_minion_horde, R.raw.minion_horde_voice));
//        allCards.add(new Card(R.drawable.archer_queen, R.raw.archer_queen_voice));
//        allCards.add(new Card(R.drawable.boss_bandit_card_frame_alpha, 0));
//        allCards.add(new Card(R.drawable.card_battle_ram_evolution_01, R.raw.evo_battle_ram_voice));
//        allCards.add(new Card(R.drawable.card_dagger_duchess_tower_troop_frame, R.raw.dagger_duchess_voice));
//        allCards.add(new Card(R.drawable.card_dark_prince, R.raw.dark_prince_voice));
//        allCards.add(new Card(R.drawable.card_electro_giant, R.raw.electro_giant_voice));
//        allCards.add(new Card(R.drawable.card_electro_wizard, R.raw.electro_wizard_voice));
//        allCards.add(new Card(R.drawable.card_electro_sprit, R.raw.electro_spirit_voice));
//        allCards.add(new Card(R.drawable.card_fire_spirit, R.raw.fire_spirit_voice));
//        allCards.add(new Card(R.drawable.card_fireball, 0));
//        allCards.add(new Card(R.drawable.card_giant_bomber, R.raw.giant_skeleton_voice));
//        allCards.add(new Card(R.drawable.card_goblin_gang, R.raw.goblin_gang_voice));
//        allCards.add(new Card(R.drawable.card_goblin_machine_01, 0));
//        allCards.add(new Card(R.drawable.card_ice_golem_super, 0));
//        allCards.add(new Card(R.drawable.card_ice_spirit_evolution_frame, R.raw.evo_ice_spirit_voice));
//        allCards.add(new Card(R.drawable.card_lumberjack, R.raw.lumberjack_voice));
//        allCards.add(new Card(R.drawable.card_magic_archer_super, 0));
//        allCards.add(new Card(R.drawable.card_magic_archer, 0));
//        allCards.add(new Card(R.drawable.card_mega_knight, 0));
//        allCards.add(new Card(R.drawable.card_monk, R.raw.monk_voice));
//        allCards.add(new Card(R.drawable.card_mother_witch, R.raw.mother_witch_voice));
//        allCards.add(new Card(R.drawable.card_pheonix, R.raw.phoenix_voice));
//        allCards.add(new Card(R.drawable.card_princess, R.raw.princess_voice));
//        allCards.add(new Card(R.drawable.card_skeleton_army, R.raw.skeleton_army_voice));
//        allCards.add(new Card(R.drawable.card_skeleton_dragons, R.raw.skeleton_dragons_voice));
//        allCards.add(new Card(R.drawable.card_tesla, R.raw.tesla_voice));
//        allCards.add(new Card(R.drawable.card_the_log, R.raw.log_voice));
//        allCards.add(new Card(R.drawable.card_three_musketeers, 0));
//        allCards.add(new Card(R.drawable.executioner_evolution_card_alpha, 0));



        allCards.add(new Card("card_goblins", "gobelins_voice"));
        allCards.add(new Card("card_hog_rider", "hog_rider_voice"));
        allCards.add(new Card("card_mini_pekka", "mini_pekka_voice"));
        allCards.add(new Card("card_prince", "prince_voice"));
        allCards.add(new Card("card_valkiry", "valkyrie_voice"));
        allCards.add(new Card("card_bats", "bats_voice"));
        allCards.add(new Card("card_bomber", "bomber_voice"));
        allCards.add(new Card("card_elite_barbarians", "elite_barbarians_voice"));
        allCards.add(new Card("card_firecracker", "firecracker_voice"));
        allCards.add(new Card("card_wizard", "wizard_voice"));
        allCards.add(new Card("card_minion_horde", "minion_horde_voice"));
        allCards.add(new Card("archer_queen", "archer_queen_voice"));
        allCards.add(new Card("boss_bandit_card_frame_alpha", null)); // pas de son
        allCards.add(new Card("card_battle_ram_evolution_01", "evo_battle_ram_voice"));
        allCards.add(new Card("card_dagger_duchess_tower_troop_frame", "dagger_duchess_voice"));
        allCards.add(new Card("card_dark_prince", "dark_prince_voice"));
        allCards.add(new Card("card_electro_giant", "electro_giant_voice"));
        allCards.add(new Card("card_electro_wizard", "electro_wizard_voice"));
        allCards.add(new Card("card_electro_sprit", "electro_spirit_voice"));
        allCards.add(new Card("card_fire_spirit", "fire_spirit_voice"));
        allCards.add(new Card("card_fireball", null));
        allCards.add(new Card("card_giant_bomber", "giant_skeleton_voice"));
        allCards.add(new Card("card_goblin_gang", "goblin_gang_voice"));
        allCards.add(new Card("card_goblin_machine_01", null));
        allCards.add(new Card("card_ice_golem_super", null));
        allCards.add(new Card("card_ice_spirit_evolution_frame", "evo_ice_spirit_voice"));
        allCards.add(new Card("card_lumberjack", "lumberjack_voice"));
        allCards.add(new Card("card_magic_archer_super", null));
        allCards.add(new Card("card_magic_archer", null));
        allCards.add(new Card("card_mega_knight", null));
        allCards.add(new Card("card_monk", "monk_voice"));
        allCards.add(new Card("card_mother_witch", "mother_witch_voice"));
        allCards.add(new Card("card_pheonix", "phoenix_voice"));
        allCards.add(new Card("card_princess", "princess_voice"));
        allCards.add(new Card("card_skeleton_army", "skeleton_army_voice"));
        allCards.add(new Card("card_skeleton_dragons", "skeleton_dragons_voice"));
        allCards.add(new Card("card_tesla", "tesla_voice"));
        allCards.add(new Card("card_the_log", "log_voice"));
        allCards.add(new Card("card_three_musketeers", null));
        allCards.add(new Card("executioner_evolution_card_alpha", null));

    }

    public void startNewRound()  {
        Random random = new Random();

        Api api = new Api();

//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(() -> {
//            String suicide;
//            try {
//                suicide = api.getApi();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Log.d("Couilles","AledOscour : "+suicide);
//        });
        try {
            api.getApi("https://students.gryt.tech/api/L2/clashroyaleblindtest/", new ApiCallback() {
                @Override
                public void onSuccess(String result) {
                    //result récupère le json et tout mais jsp comment l'utiliser
                    //Log.d("API", "Réponse : " + data.get(0).audioResId);
                }

                @Override
                public void onError(Exception e) {
                    Log.e("API", "Erreur : " + e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //String jsonString = String.valueOf(api.getJson());

//        Card card = gson.fromJson(jsonString, Card.class);

        // keep only the cards that have audio
        List<Card> cardsWithAudio = new ArrayList<>();
        for (Card card : allCards) {
            Log.d("GameManager",card.getImageResId(context)+"");
            if (card.hasAudio()) {
                cardsWithAudio.add(card);
                Log.d("GameManager",card.getAudioResId(context)+"");
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
