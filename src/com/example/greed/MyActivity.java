package com.example.greed;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyActivity extends Activity {

    private ArrayList<Die> dice = new ArrayList<Die>();
    private HashMap<Integer, Integer> diceMapper = new HashMap<Integer, Integer>();
    private int totalScore = 0;
    private int turnScore = 0;
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

        dice.add((Die) findViewById(R.id.die_1));
        dice.add((Die) findViewById(R.id.die_2));
        dice.add((Die) findViewById(R.id.die_3));
        dice.add((Die) findViewById(R.id.die_4));
        dice.add((Die) findViewById(R.id.die_5));
        dice.add((Die) findViewById(R.id.die_6));
    }


    public void throwDice(View view) {
        diceMapper.clear();
        for(Die die : dice) {
            if(die.isLocked() == false) {
                die.throwDie();
            } else if(throwAll == true) {
                die.setUnLocked();
                die.throwDie();
            }
            int temp = 0;
            if(diceMapper.get(die.getDieValue()) != null) {
                temp = diceMapper.get(die.getDieValue());
            }
            diceMapper.put(die.getDieValue(), temp + 1);
        }
        throwAll = false;
        scoreCalculator();

        Button saveButton = (Button) findViewById(R.id.save_button);
        int nrOfLocked = 0;

        if(firstThrow && turnScore < 300) {
            for(Die die : dice) {
                die.setWhiteDieImage(die.getDieValue());
            }
            turnScore = 0;
            round = round + 1;
            saveButton.setClickable(false);
        } else if(firstThrow && turnScore >= 300){
            for(Die die : dice) {
                if(die.isLocked()) {
                    die.setGreyDieImage(die.getDieValue());
                    nrOfLocked = nrOfLocked + 1;
                } else {
                    die.setWhiteDieImage(die.getDieValue());
                }
            }
            firstThrow = false;
            saveButton.setClickable(true);
        } else if(!firstThrow && turnScore > 0) {
            for(Die die : dice) {
                if(die.isLocked()) {
                    die.setGreyDieImage(die.getDieValue());
                    nrOfLocked = nrOfLocked + 1;
                } else {
                    die.setWhiteDieImage(die.getDieValue());
                }
            }
            saveButton.setClickable(true);
        } else if(!firstThrow && turnScore == 0) {
            for(Die die : dice) {
                die.setWhiteDieImage(die.getDieValue());
            }
            firstThrow = true;
            turnScore = 0;
            round = round + 1;
            saveButton.setClickable(false);
        }
        if(nrOfLocked == 6) {
            throwAll = true;
        }
        TextView turnScore = (TextView) findViewById(R.id.textView2);
        turnScore.setText(getResources().getString(R.string.turn_score) + turnScore);
    }

    public void save(View view) {
        totalScore = totalScore + turnScore;
        round = round + 1;
        turnScore = 0;
        firstThrow = true;
        for(Die die : dice) {
            die.setUnLocked();
            die.setWhiteDieImage(die.getDieValue());
        }
        TextView totalScoreView = (TextView) findViewById(R.id.textView);
        totalScoreView.setText(getResources().getString(R.string.total_score) + totalScore);
        view.setClickable(false);
    }


    protected void scoreCalculator() {
        for(int i = 1; i < 7; i++) {
            if(diceMapper.get(i) != null) {
                if(diceMapper.get(i) >= 3) {
                    if(i == 1) {
                        turnScore = turnScore + 1000;
                        turnScore = turnScore + ((diceMapper.get(i) - 3)*100);
                        diceLocker(i, diceMapper.get(i));
                    } else if(i == 5) {
                        turnScore = turnScore + (i*100);
                        turnScore = turnScore + ((diceMapper.get(i) - 3)*50);
                        diceLocker(i, diceMapper.get(i));
                    }  else {
                        turnScore = turnScore + (i*100);
                        diceLocker(i, 3);
                    }
                } else {
                    if(i == 1) {
                        turnScore = turnScore + (diceMapper.get(i)*100);
                        diceLocker(i, diceMapper.get(i));
                    } else if(i == 5) {
                        turnScore = turnScore + (diceMapper.get(i)*50);
                        diceLocker(i, diceMapper.get(i));
                    }
                }
                if(diceMapper.size() == 6) {
                    diceLocker(i, 1);
                }
            }
        }
        if(diceMapper.size() == 6) {
            turnScore = turnScore + 1000;
        }
    }

    protected void diceLocker(int diceValue, int numberOfDice) {
        int diceSet = 0;
        for(Die die : dice) {
            if(die.getDieValue() == diceValue && die.isLocked() == false) {
                if(diceSet < numberOfDice) {
                    die.setLocked();
                    diceSet = diceSet + 1;
                }
            }
        }
    }
}
