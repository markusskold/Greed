package com.example.greed;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * WinActivity är aktiviteten som startas när användaren fått mer eller lika med 10000 poäng. Aktiviteten använder filen
 * win.xml som layoutfil. Den gratulerar användaren för att ha klarat spelet och anger exakt hur mycket poäng som
 * uppnåtts och på hur många rundor.
 */
public class WinActivity extends Activity {

    /**
     * Påkallas när aktiviteten startas. Tar in en Bundle som innehåller den totala poängen och antalet rundor.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win);


        Bundle info = getIntent().getExtras();
        int totalScore = info.getInt("totalScore");
        int round = info.getInt("round");

        Resources res = getResources();
        String roundText = res.getQuantityString(R.plurals.numberOfRounds, round);
        String text = String.format(res.getString(R.string.win_2), totalScore, round, roundText);

        TextView scoreRoundMessage = (TextView) findViewById(R.id.score_round);
        scoreRoundMessage.setText(text);
    }

    /**
     * Körs när användaren klickar på bakåtknappen.
     * Om bakåtknappen används så ska användaren hamna i MyActivity och börja på en ny spelomgång.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Körs när användaren klickar på tryAgainknappen.
     * Om tryAgainknappen används så ska användaren hamna i MyActivity och börja på en ny spelomgång.
     */
    public void tryAgain(View view) {
        Intent intent = new Intent(this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}
