package com.example.flashcard;

import android.widget.ImageView;

import java.util.Random;

public class EnvironmentManager {

    private ImageView towerView;
    private ImageView decorView;
    private int[] towerSkins;
    private int[] decorImages;
    private Random random;

    public EnvironmentManager(ImageView towerView, ImageView decorView) {
        this.towerView = towerView;
        this.decorView = decorView;

        random = new Random();

        // Array of tower images
        towerSkins = new int[]{
                R.drawable.tower_skin_1, R.drawable.tower_skin_2, R.drawable.tower_skin_3,
                R.drawable.tower_skin_4, R.drawable.tower_skin_5, R.drawable.tower_skin_6,
                R.drawable.tower_skin_7, R.drawable.tower_skin_8, R.drawable.tower_skin_9,
                R.drawable.tower_skin_10
        };

        // Array of attack decor images
        decorImages = new int[]{
                R.drawable.decor_attack_1, R.drawable.decor_attack_2, R.drawable.decor_attack_3,
                R.drawable.decor_attack_4, R.drawable.decor_attack_5, R.drawable.decor_attack_6,
                R.drawable.decor_attack_7, R.drawable.decor_attack_8
        };
    }

    // Selects a random tower and displays it
    public void setRandomTower() {
        int index = random.nextInt(towerSkins.length);
        towerView.setImageResource(towerSkins[index]);
    }

    // Selects a random decor and displays it
    public void setRandomDecor() {
        int index = random.nextInt(decorImages.length);
        decorView.setImageResource(decorImages[index]);
    }

    // Changes both the tower and the decor at the same time
    public void setRandomEnvironment() {
        setRandomTower();
        setRandomDecor();
    }
}
