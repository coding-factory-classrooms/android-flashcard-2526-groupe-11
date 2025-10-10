package com.example.flashcard;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GlobalStatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_global_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





        TextView totalQuiz = findViewById(R.id.totalQuizTextview);
        TextView goodAnswer = findViewById(R.id.goodAnswerTextView);
        TextView badAnswer = findViewById(R.id.badAnswerTextView);
        TextView totalTimePlay = findViewById(R.id.totaltimePerQuestionTextView);
        TextView meanQuizTIme = findViewById(R.id.meanQuizTImeTextView);

        GlobalStats globalStats = new GlobalStats(0,0,0,0,0);

        if (!globalStats.jsonExist(this, "globalStats.json"))
        {
            globalStats.jsonWrite(this, "globalStats.json");

        }
        GlobalStats  globalStatsJson = globalStats.jsonRead(this, "globalStats.json");

        totalQuiz.setText("Quiz joués: " + globalStatsJson.getTotalQuiz());
        goodAnswer.setText("Bonnes réponses: "  +globalStatsJson.getGoodAnswer());
        badAnswer.setText("Mauvaises réponses: " +globalStatsJson.getBadAnswer());
        totalTimePlay.setText("Temps de jeu: " +globalStatsJson.getTotalTimePlay());
        meanQuizTIme.setText("temps moyen d'un quiz: " + globalStatsJson.getMeanQuizTime());










    }


}