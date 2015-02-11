package com.example.greed;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MyActivity är aktiviteten som startas när programmet körs. Den har sex objekt av klassen Die i en arrayList. Dessa
 * tärningar kastas om med metoden throwDice och en annan klass ScoreCalculator används sedan för att beräkna de möjliga
 * poängen spelaren kan få. ScoreCalculator returnerar hur många tärningar med olika värden som gav poäng i en hashmap,
 * hashmapen används då i metoden diceSelecter för att markera för användaren vilka tärningar som ger poäng.
 *
 */
public class MyActivity extends Activity {

    private ScoreCalculator calculator;
    private ArrayList<Die> dice = new ArrayList<Die>();
    private HashMap<Integer, Integer> diceMapper = new HashMap<Integer, Integer>();
    private TextView totalScoreView;
    private TextView turnScoreView;
    private TextView currentRoundView;
    private TextView selectedScoreView;
    private TextView potentialScoreView;
    private Button saveButton;
    private int round;
    private boolean firstThrow;
    private boolean secondThrow;

    /**
     * OnCreate instantierar alla de objekt som behövs för att spela och den finner även alla vyer som tillhör den
     * layout aktiviteten tillhör och sparar dessa.
     * Ifall en rotatio skett och aktiviteten skapats om så används två olika layout filer beroende på vilken
     * orientering det nu är, all data från den tidigare aktiviteten som förstördes hämtas då ur Bundlen savedInstanceState.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            System.out.println("Portrait");
            setContentView(R.layout.main);
        } else if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            System.out.println("LandScape");
            setContentView(R.layout.main_landscape);
        }

        if(savedInstanceState != null) {
            int totalScore = savedInstanceState.getInt("totalScore");
            int turnScore = savedInstanceState.getInt("turnScore");
            int potentialThrowScore =  savedInstanceState.getInt("potentialThrowScore");
            int selectedScore = savedInstanceState.getInt("selectedScore");
            round = savedInstanceState.getInt("round");
            int[] diceValues = savedInstanceState.getIntArray("diceValues");
            boolean[] diceLocked = savedInstanceState.getBooleanArray("diceLocked");
            boolean[] diceSelected = savedInstanceState.getBooleanArray("diceSelected");
            boolean[] diceSelectable = savedInstanceState.getBooleanArray("diceSelectable");
            firstThrow = savedInstanceState.getBoolean("firstThrow");
            secondThrow = savedInstanceState.getBoolean("secondThrow");
            calculator = new ScoreCalculator(totalScore, turnScore, potentialThrowScore, selectedScore);

            for(int i = 1; i < 7; i++) {
                String idForButton = "die_" + i;
                int identifier = getResources().getIdentifier(idForButton, "id", "com.example.greed");
                System.out.println("Identifier: " + identifier);
                Die die = (Die) findViewById(identifier);
                die.setDieValue(diceValues[i-1]);
                die.setLocked(diceLocked[i-1]);
                die.setSelected(diceSelected[i-1]);
                die.setSelectable(diceSelectable[i-1]);
                if(die.isLocked()) {
                    die.setRedDieImage(die.getDieValue());
                } else if(die.isSelected()) {
                    die.setRedDieImage(die.getDieValue());
                    if(calculator.thisThrowScore.get(die.getDieValue()) != null) {
                        calculator.thisThrowScore.put(die.getDieValue(), calculator.thisThrowScore.get(die.getDieValue()) + 1);
                    } else {
                        calculator.thisThrowScore.put(die.getDieValue(), 1);
                    }
                } else if(die.isSelectable()) {
                    die.setGreyDieImage(die.getDieValue());
                } else {
                    die.setWhiteDieImage(die.getDieValue());
                }
                dice.add(die);
            }
        } else {
            round = 1;
            firstThrow = true;
            secondThrow = false;
            calculator = new ScoreCalculator();

            for(int i = 1; i < 7; i++) {
                String idForButton = "die_" + i;
                int identifier = getResources().getIdentifier(idForButton, "id", "com.example.greed");
                Die die = (Die) findViewById(identifier);
                dice.add(die);
            }
        }

        totalScoreView = (TextView) findViewById(R.id.total_score);
        turnScoreView = (TextView) findViewById(R.id.turn_score);
        currentRoundView = (TextView) findViewById(R.id.current_round);
        selectedScoreView = (TextView) findViewById(R.id.selected_score);
        potentialScoreView = (TextView) findViewById(R.id.potential_score);
        saveButton = (Button) findViewById(R.id.save_button);

        totalScoreView.setText(String.format(getResources().getString(R.string.total_score), calculator.getTotalScore()));
        turnScoreView.setText(String.format(getResources().getString(R.string.turn_score), calculator.getTurnScore()));
        currentRoundView.setText(String.format(getResources().getString(R.string.current_round), round));
        selectedScoreView.setText(String.format(getResources().getString(R.string.selected_score), calculator.getSelectedScore()));
        potentialScoreView.setText(String.format(getResources().getString(R.string.potential_score), calculator.getPotentialThrowScore()));
    }

    /**
     * Körs när aktiviteten förstörs, sparar viktig data så att datan kan användas igen när aktiviteten återskapas.
     * Detta är viktigt ifall det skett en rotatation.
     *
     * Den viktiga datan är framförallt alla tärningars värden samt ifall de är låsta, ger poäng för senaste kastet
     * eller är markerade. Annan viktig data är den totala poängen, rundans poäng, möjliga poäng för senaste kastet,
     * markerade poäng för senaste kastet, vilken runda det är och ifall det är första, andra eller ett senare kast
     * på rundan.
     * @param savedInstanceState - En Bundle där den viktiga datan sparas så att den kan användas igen.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        int[] diceValues = new int[6];
        boolean[] diceLocked = new boolean[6];
        boolean[] diceSelected = new boolean[6];
        boolean[] diceSelectable = new boolean[6];
        int i = 0;
        for(Die die : dice) {
            diceValues[i] = die.getDieValue();
            diceLocked[i] = die.isLocked();
            diceSelected[i] = die.isSelected();
            diceSelectable[i] = die.isSelectable();
            i++;
        }
        savedInstanceState.putIntArray("diceValues", diceValues);
        savedInstanceState.putBooleanArray("diceLocked", diceLocked);
        savedInstanceState.putBooleanArray("diceSelected", diceSelected);
        savedInstanceState.putBooleanArray("diceSelectable", diceSelectable);
        savedInstanceState.putInt("totalScore", calculator.getTotalScore());
        savedInstanceState.putInt("turnScore", calculator.getTurnScore());
        savedInstanceState.putInt("potentialThrowScore", calculator.getPotentialThrowScore());
        savedInstanceState.putInt("selectedScore", calculator.getSelectedScore());
        savedInstanceState.putInt("round", round);
        savedInstanceState.putBoolean("firstThrow", firstThrow);
        savedInstanceState.putBoolean("secondThrow", secondThrow);
    }

    /**
     * Körs när användaren klickar på bakåtknappen.
     * Om bakåtknappen används så ska användaren hamna i hemplatsen för enheten.
     */
    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    /**
     * Körs när användaren klickar på throwknappen.
     * Denna metod kastar alla tärningar som inte är markerade eller låsta sedan tidigare, eller alla tärningar om alla
     * tärningar är markerade eller låsta sedan tidigare. För de tärningar som kastas så används ScoreCalculator för att
     * räkna ut vad poängen blev av kasten. Om poängen är tillräcklig enligt spelreglerna så byts bilden för tärningarna
     * som gav poäng ut till en grå bild för att markera för användaren att dessa ger poäng, dessa tärningar kan
     * användaren nu markera för att välja vilka som ska låsas inför nästa kast. Om poängen ej är tillräcklig så sätts
     * poängen för denna runda till noll och sedan körs metoden save som nollställer alla tärningar så att de kan kastas
     * om.
     *
     * Det går ej att kasta om tärningarna ifall poängen från de markerade tärningarna är mindre än gränsen för att klara
     * ett kast samtidigt som den totala poängen från kastet är högre än gränsen. Detta är så att användaren inte råkar
     * missa ett kast som gett poäng.
     * @param view - Den View som påkallat metoden, används aldrig i metoden.
     */
    public void throwDice(View view) {
        if(!firstThrow && secondThrow && calculator.getSelectedScore() < 300) {

        } else if (!firstThrow && calculator.getSelectedScore() == 0) {

        } else {
            int nrOfSelected = 0;
            for (Die die : dice) {
                if(die.isSelected()) {
                    nrOfSelected++;
                }
            }
            boolean throwAll;
            if(nrOfSelected == 6) {
                throwAll = true;
            } else {
                throwAll = false;
            }
            calculator.setTurnScore(calculator.getTurnScore() + calculator.getSelectedScore());
            turnScoreView.setText(String.format(getResources().getString(R.string.turn_score), calculator.getTurnScore()));
            currentRoundView.setText(String.format(getResources().getString(R.string.current_round), round));
            calculator.resetScoreCalculator();
            diceMapper.clear();
            for (Die die : dice) {
                if (die.isLocked() == false && die.isSelected() == false) {
                    die.throwDie();
                } else if (throwAll == true) {
                    die.setLocked(false);
                    die.setSelected(false);
                    die.setSelectable(false);
                    die.throwDie();
                } else {
                    die.setLocked(true);
                    die.setSelected(true);
                    die.setSelectable(false);
                    die.setRedDieImage(die.getDieValue());
                }
                int temp = 0;
                if (diceMapper.get(die.getDieValue()) != null) {
                    temp = diceMapper.get(die.getDieValue());
                }
                if (die.isLocked() == false) {
                    diceMapper.put(die.getDieValue(), temp + 1);
                }
            }
            secondThrow = false;
            HashMap<Integer, Integer> selectableDice = calculator.scoreCalculator(diceMapper);

            if (firstThrow && calculator.getPotentialThrowScore() < 300) {
                calculator.setPotentialThrowScore(0);
                calculator.setTurnScore(0);
                calculator.setSelectedScore(0);
                save(saveButton);
            } else if (firstThrow && calculator.getPotentialThrowScore() >= 300) {
                firstThrow = false;
                secondThrow = true;
                saveButton.setClickable(true);
                diceSelecter(selectableDice);
            } else if (!firstThrow && calculator.getPotentialThrowScore() > 0) {
                saveButton.setClickable(true);
                diceSelecter(selectableDice);
            } else if (!firstThrow && calculator.getPotentialThrowScore() == 0) {
                calculator.setPotentialThrowScore(0);
                calculator.setTurnScore(0);
                calculator.setSelectedScore(0);
                save(saveButton);
            }
            potentialScoreView.setText(String.format(getResources().getString(R.string.potential_score), calculator.getPotentialThrowScore()));
        }
    }

    /**
     * Körs när användaren klickar på någon av tärningarna.
     * Om tärningen som klickats inte är låst och markerbar "selectable" så kan två saker hända beroende på om tärningen
     * är markerad eller inte. Om den ej är markerad så blir den markerad och byter färg från grå till röd samt att
     * poängen för de markerade tärningarna omberäknas och denna poäng skrivs ut i interfacet.
     * Om den är markerad så blir den omarkerad och byter färg till från röd till grå samt att poängen för de markerade
     * tärningarna omberäknas och denna poäng skrivs ut i interfacet.
     *
     * @param view - Den View som påkallat metoden, är även tärningen och innehåller all information för tärningen.
     */
    public void onButtonClick(View view) {
        Die die = (Die)view;

        if(die.isLocked() == false) {
            if(die.isSelectable()) {
                if(die.isSelected()) {
                    die.setGreyDieImage(die.getDieValue());
                    die.setSelected(false);
                    calculator.calculateScoreForDeSelectingDiceValue(die.getDieValue());
                    selectedScoreView.setText(String.format(getResources().getString(R.string.selected_score), calculator.getSelectedScore()));
                } else {
                    die.setRedDieImage(die.getDieValue());
                    die.setSelected(true);
                    calculator.calculateScoreForSelectingDiceValue(die.getDieValue());
                    selectedScoreView.setText(String.format(getResources().getString(R.string.selected_score), calculator.getSelectedScore()));
                }
            }
        }
    }

    /**
     * Körs när användaren klickar på saveknappen eller från throwDice metoden.
     * Ingenting händer ifall den markerade poängen är mindre än potentiella poängen, så att användaren inte råkar spara
     * innan all potentiell poäng markerats.
     *
     * Annars så flyttas poängen för rundan över till den totala poängen, alla tärningar nollställs så att de kan kastas
     * om och vyerna uppdatteras. Om den totala poängen blir mer än 10000 så startas en ny aktivitet som gratulerar
     * användaren över att ha klarat spelet på en viss mängd rundor.
     *
     * @param view - Den View som påkallat metoden, sätts till oklickbar i slutet av metoden.
     */
    public void save(View view) {
        if(calculator.getSelectedScore() < calculator.getPotentialThrowScore()) {

        } else {
            calculator.setTurnScore(calculator.getTurnScore() + calculator.getSelectedScore());
            calculator.setTotalScore(calculator.getTotalScore() + calculator.getTurnScore());

            if(calculator.getTotalScore() >= 10000) {
                Intent intent = new Intent(this, WinActivity.class);
                Bundle info = new Bundle();
                info.putInt("totalScore", calculator.getTotalScore());
                info.putInt("round", round);
                intent.putExtras(info);
                startActivity(intent);
                finish();
            }

            round = round + 1;
            calculator.setTurnScore(0);
            calculator.resetScoreCalculator();
            firstThrow = true;
            for(Die die : dice) {
                die.setLocked(false);
                die.setSelected(false);
                die.setSelectable(false);
                die.setWhiteDieImage(die.getDieValue());
            }
            totalScoreView.setText(String.format(getResources().getString(R.string.total_score), calculator.getTotalScore()));
            turnScoreView.setText(String.format(getResources().getString(R.string.turn_score), calculator.getTurnScore()));
            currentRoundView.setText(String.format(getResources().getString(R.string.current_round), round));
            selectedScoreView.setText(String.format(getResources().getString(R.string.selected_score), 0));
            potentialScoreView.setText(String.format(getResources().getString(R.string.potential_score), 0));
            view.setClickable(false);
        }
    }

    /**
     * Körs från metoden throwDice.
     * Tar in en hashmap som säger hur många tärningar med värdet 1,2..6 som gav poäng i det senaste kastet och som
     * därför ska bli markerbara.
     * Tärningarna som finns i en ArrayList i den här klassen loopas igenom och sätts markerbara (selectable) ifall
     * tärningens värde finns som nyckel i hashmapen och ifall antalet tärningar med detta värde som ska sättas ej
     * uppnåtts.
     *
     * @param selectableDice - En hashmap med antalet tärningar som ska bli markerbara för varje tärningsvärde.
     */
    protected void diceSelecter(HashMap<Integer, Integer> selectableDice) {
        for(HashMap.Entry<Integer, Integer> entry : selectableDice.entrySet()) {
            int diceValue = entry.getKey();
            int numberOfDice = entry.getValue();
            int diceSet = 0;
            for(Die die : dice) {
                if(die.getDieValue() == diceValue && die.isLocked() == false) {
                    if(diceSet < numberOfDice) {
                        die.setSelectable(true);
                        diceSet = diceSet + 1;
                        die.setGreyDieImage(die.getDieValue());
                    }
                }
            }
        }
    }
}
