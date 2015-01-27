package com.example.greed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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
    private int round = 0;
    private boolean firstThrow = true;
    private boolean throwAll = false;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        calculator = new ScoreCalculator();

        totalScoreView = (TextView) findViewById(R.id.total_score);
        turnScoreView = (TextView) findViewById(R.id.turn_score);
        currentRoundView = (TextView) findViewById(R.id.current_round);
        selectedScoreView = (TextView) findViewById(R.id.selected_score);
        potentialScoreView = (TextView) findViewById(R.id.potential_score);
        saveButton = (Button) findViewById(R.id.save_button);

        for(int i = 1; i < 7; i++) {
            String idForButton = "die_" + i;
            int identifier = getResources().getIdentifier(idForButton, "id", "com.example.greed");
            System.out.println("Identifier: " + identifier);
            Die die = (Die) findViewById(identifier);
            dice.add(die);
        }

        totalScoreView.setText(getResources().getString(R.string.total_score) + calculator.getTotalScore());
        turnScoreView.setText(getResources().getString(R.string.turn_score) + calculator.getTurnScore());
        currentRoundView.setText(getResources().getString(R.string.current_round) + round);
        selectedScoreView.setText(getResources().getString(R.string.selected_score) + 0);
        potentialScoreView.setText(getResources().getString(R.string.potential_score) + 0);
    }


    public void throwDice(View view) {
        calculator.setTurnScore(calculator.getTurnScore() + calculator.getSelectedScore());
        turnScoreView.setText(getResources().getString(R.string.turn_score) + calculator.getTurnScore());
        currentRoundView.setText(getResources().getString(R.string.current_round) + round);
        calculator.resetScoreCalculator();
        System.out.println("1111111111");
        diceMapper.clear();
        for(Die die : dice) {
            if(die.isLocked() == false && die.isSelected() == false) {
                die.throwDie();
            } else if(throwAll == true) {
                die.setLocked(false);
                die.setSelectable(false);
                die.throwDie();
            } else {
                die.setLocked(true);
                die.setSelected(false);
                die.setSelectable(false);
                die.setRedDieImage(die.getDieValue());
            }
            int temp = 0;
            if(diceMapper.get(die.getDieValue()) != null) {
                temp = diceMapper.get(die.getDieValue());
            }
            if(die.isLocked() == false) {
                diceMapper.put(die.getDieValue(), temp + 1);
            }
        }
        System.out.println("222222222");
        throwAll = false;
        HashMap<Integer, Integer> selectableDice = calculator.scoreCalculator(diceMapper);

        System.out.println("3333333333");
        if(firstThrow && calculator.getPotentialThrowScore() < 300) {
            firstThrow = true;
            calculator.setTurnScore(0);
            round = round + 1;
            saveButton.setClickable(false);
        } else if(firstThrow && calculator.getPotentialThrowScore() >= 300){
            firstThrow = false;
            saveButton.setClickable(true);
            diceSelecter(selectableDice);
        } else if(!firstThrow && calculator.getPotentialThrowScore() > 0) {
            saveButton.setClickable(true);
            diceSelecter(selectableDice);
        } else if(!firstThrow && calculator.getPotentialThrowScore() == 0) {
            firstThrow = true;
            calculator.setTurnScore(0);
            round = round + 1;
            saveButton.setClickable(false);
        }
        System.out.println("4444444444");
        potentialScoreView.setText(getResources().getString(R.string.potential_score) + calculator.getPotentialThrowScore());
    }

    public void onButtonClick(View view) {
        Die die = (Die)view;

        if(die.isLocked() == false) {
            if(die.isSelectable()) {
                if(die.isSelected()) {
                    die.setGreyDieImage(die.getDieValue());
                    die.setSelected(false);
                    calculator.calculateScoreForDeSelectingDiceValue(die.getDieValue());
                    selectedScoreView.setText(getResources().getString(R.string.selected_score) + calculator.getSelectedScore());
                } else {
                    die.setRedDieImage(die.getDieValue());
                    die.setSelected(true);
                    calculator.calculateScoreForSelectingDiceValue(die.getDieValue());
                    selectedScoreView.setText(getResources().getString(R.string.selected_score) + calculator.getSelectedScore());
                }
            }
        }
    }

    public void save(View view) {
        calculator.setTurnScore(calculator.getTurnScore() + calculator.getSelectedScore());
        calculator.setTotalScore(calculator.getTotalScore() + calculator.getTurnScore());
        round = round + 1;
        calculator.setTurnScore(0);
        calculator.resetScoreCalculator();
        firstThrow = true;
        throwAll = false;
        for(Die die : dice) {
            die.setLocked(false);
            die.setSelected(false);
            die.setSelectable(false);
            die.setWhiteDieImage(die.getDieValue());
        }
        totalScoreView.setText(getResources().getString(R.string.total_score) + calculator.getTotalScore());
        turnScoreView.setText(getResources().getString(R.string.turn_score) + calculator.getTurnScore());
        view.setClickable(false);
    }


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
