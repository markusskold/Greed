package com.example.greed;

import java.util.HashMap;

/**
 * Created by oi11msd on 2015-01-27.
 */
public class ScoreCalculator {
    private int totalScore;
    private int turnScore;
    private int potentialThrowScore;
    private int selectedScore;
    private HashMap<Integer, Integer> thisThrowScore;

    public ScoreCalculator() {
        totalScore = 0;
        turnScore = 0;
        potentialThrowScore = 0;
        selectedScore = 0;
        thisThrowScore = new HashMap<Integer, Integer>();
    }


    public HashMap<Integer, Integer> scoreCalculator(HashMap<Integer, Integer> diceMapper) {
        HashMap<Integer, Integer> selectableDice = new HashMap<Integer, Integer>();
        for(int i = 1; i < 7; i++) {
            if(diceMapper.get(i) != null) {
                if(diceMapper.get(i) >= 3) {
                    if(i == 1) {
                        potentialThrowScore += 1000;
                        potentialThrowScore += ((diceMapper.get(i) - 3)*100);
                        selectableDice.put(i, diceMapper.get(i));
                    } else if(i == 5) {
                        potentialThrowScore += (i*100);
                        potentialThrowScore += ((diceMapper.get(i) - 3)*50);
                        selectableDice.put(i, diceMapper.get(i));
                    }  else {
                        potentialThrowScore += (i*100);
                        selectableDice.put(i, 3);
                    }
                } else {
                    if(i == 1) {
                        potentialThrowScore += (diceMapper.get(i)*100);
                        selectableDice.put(i, diceMapper.get(i));
                    } else if(i == 5) {
                        potentialThrowScore += (diceMapper.get(i)*50);
                        selectableDice.put(i, diceMapper.get(i));
                    }
                }
                if(diceMapper.size() == 6) {
                    selectableDice.put(i, 1);
                }
            }
        }
        if(diceMapper.size() == 6) {
            potentialThrowScore += 1000;
        }
        return selectableDice;
    }

    public void calculateScoreForSelectingDiceValue(int value) {
        if(thisThrowScore.get(value) != null) {
            thisThrowScore.put(value, thisThrowScore.get(value) + 1);
        } else {
            thisThrowScore.put(value, 1);
        }
        if(thisThrowScore.get(value) == 3) {
            if(value == 1) {
                selectedScore = selectedScore - (2*100);
                selectedScore += 1000;
            } else if(value == 5) {
                selectedScore = selectedScore - (2*50);
                selectedScore += value*100;
            } else {
                selectedScore += value*100;
            }
        } else {
            if(value == 1) {
                selectedScore += 100;
            } else if(value == 5) {
                selectedScore += 50;
            } else if(thisThrowScore.size() == 6) {
                selectedScore += 1000;
            }
        }
    }

    public void calculateScoreForDeSelectingDiceValue(int value) {
        if(thisThrowScore.get(value) != null && thisThrowScore.get(value) == 1) {
            thisThrowScore.remove(value);
        } else if(thisThrowScore.get(value) != null){
            thisThrowScore.put(value, thisThrowScore.get(value) - 1);
        }
        if(thisThrowScore.get(value) != null && thisThrowScore.get(value) == 2) {
            if(value == 1) {
                selectedScore += (2*100);
                selectedScore = selectedScore - 1000;
            } else if(value == 5) {
                selectedScore += (2*50);
                selectedScore = selectedScore - value*100;
            } else {
                selectedScore = selectedScore - value*100;
            }
        } else {
            if(value == 1) {
                selectedScore = selectedScore - 100;
            } else if(value == 5) {
                selectedScore = selectedScore - 50;
            } else if(thisThrowScore.size() == 5 && thisThrowScore.containsKey(value) == false) {
                selectedScore = selectedScore - 1000;
            }
        }
    }

    public void resetScoreCalculator() {
        thisThrowScore.clear();
        selectedScore = 0;
        potentialThrowScore = 0;
    }

    public int getPotentialThrowScore() {
        return potentialThrowScore;
    }

    public void setPotentialThrowScore(int potentialThrowScore) {
        this.potentialThrowScore = potentialThrowScore;
    }

    public int getTurnScore() {
        return turnScore;
    }

    public void setTurnScore(int turnScore) {
        this.turnScore = turnScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getSelectedScore() { return selectedScore; }

    public void setSelectedScore(int selectedScore) {this.selectedScore = selectedScore; }
}
