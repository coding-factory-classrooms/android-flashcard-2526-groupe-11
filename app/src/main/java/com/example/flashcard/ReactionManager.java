package com.example.flashcard;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.List;

public class ReactionManager {

    private final Context context;
    private final ImageView emoteEncadre;
    private final ImageView typeResponse;
    private final ImageButton response1, response2, response3;

    private final int[] winEmotes = {
            R.drawable.emote_battle_healer_self_heal_perfect,
            R.drawable.emote_electro_giant_win_perfect,
            R.drawable.emote_electro_wizard_thumbs_up_perfect,
            R.drawable.emote_giant_thumbs_up_perfect,
            R.drawable.emote_goblin_crl_2019_perfect,
            R.drawable.emote_goblin_trophy_perfect,
            R.drawable.emote_goblin_thumbs_up_perfect,
            R.drawable.emote_heal_spirit_buddy_perfect,
            R.drawable.emote_king_nr1_perfect,
            R.drawable.emote_princess_deal_with_it_perfect,
            R.drawable.emote_princess_peace_perfect,
            R.drawable.emote_princess_thumbs_up_perfect
    };

    private final int[] loseEmotes = {
            R.drawable.emote_dark_prince_cry_lose,
            R.drawable.emote_goblin_ohno_lose,
            R.drawable.emote_king_cry_lose,
            R.drawable.emote_loading_lose,
            R.drawable.emote_log_cry_lose,
            R.drawable.emote_skeleton_cry_lose,
            R.drawable.emote_skeleton_hands_crossed_lose
    };

    public ReactionManager(Context context, ImageView emoteEncadre, ImageView typeResponse,
                           ImageButton response1, ImageButton response2, ImageButton response3) {
        this.context = context;
        this.emoteEncadre = emoteEncadre;
        this.typeResponse = typeResponse;
        this.response1 = response1;
        this.response2 = response2;
        this.response3 = response3;
    }

    public boolean showReaction(boolean correct, ImageButton clickedButton, Card correctCard, List<Card> roundOptions) {
        emoteEncadre.setVisibility(ImageView.VISIBLE);
        typeResponse.setVisibility(ImageView.VISIBLE);

        if (correct) {
            int randomIndex = (int) (Math.random() * winEmotes.length);
            emoteEncadre.setImageResource(winEmotes[randomIndex]);
            typeResponse.setImageResource(R.drawable.text_true);
            return true;
        } else {
            int randomIndex = (int) (Math.random() * loseEmotes.length);
            emoteEncadre.setImageResource(loseEmotes[randomIndex]);
            typeResponse.setImageResource(R.drawable.text_false);

            // highlight correct answer
            if (correctCard == roundOptions.get(0)) {
                response1.setColorFilter(context.getResources().getColor(R.color.correct_answer, null));
            } else if (correctCard == roundOptions.get(1)) {
                response2.setColorFilter(context.getResources().getColor(R.color.correct_answer, null));
            } else if (correctCard == roundOptions.get(2)) {
                response3.setColorFilter(context.getResources().getColor(R.color.correct_answer, null));
            }

            if (clickedButton != null) {
                clickedButton.setColorFilter(context.getResources().getColor(R.color.wrong_answer, null));
            }
            return false;
        }
    }

    public void hideReactionAfterDelay() {
        new Handler().postDelayed(() -> {
            emoteEncadre.setVisibility(ImageView.GONE);
            typeResponse.setVisibility(ImageView.GONE);
            response1.clearColorFilter();
            response2.clearColorFilter();
            response3.clearColorFilter();
        }, 2000);
    }
}
