package com.example.greed;

import java.util.HashMap;

/**
 * Denna klass håller data för den totala poängen, poängen på nuvarande runda, potentiell poäng från senaste kastet och
 * poängen för de tärningar som markerats. Klassen har även metoden för att räkna ut den poäng ett kast ger och metoder
 * för att räkna ut vad den markerade poängen blir vid en markering eller avmarkering av en tärning.
 */
public class ScoreCalculator {
    private int totalScore;
    private int turnScore;
    private int potentialThrowScore;
    private int selectedScore;
    public HashMap<Integer, Integer> thisThrowScore;

    /**
     * Konstruktor som sätter alla poäng till noll.
     */
    public ScoreCalculator() {
        totalScore = 0;
        turnScore = 0;
        potentialThrowScore = 0;
        selectedScore = 0;
        thisThrowScore = new HashMap<Integer, Integer>();
    }

    /**
     * Konstruktor som tar in vad poängenvariablerna ska vara. Denna används av onCreate i MyActivity då en rotation
     * skett på enheten.
     * @param totalScore - En int som håller totala poängen.
     * @param turnScore - En int som håller poängen för rundan.
     * @param potentialThrowScore - En int som håller den potentiella poängen.
     * @param selectedScore - En int som håller den markerade poängen.
     */
    public ScoreCalculator(int totalScore, int turnScore, int potentialThrowScore, int selectedScore) {
        this.totalScore = totalScore;
        this.turnScore = turnScore;
        this.potentialThrowScore = potentialThrowScore;
        this.selectedScore = selectedScore;
        thisThrowScore = new HashMap<Integer, Integer>();
    }

    /**
     * Denna metod räknar ut vad poängen blev för ett tärningskast. Den tar in en hashmap som innehåller antalet
     * tärningar som fått olika värden.
     *
     * @param diceMapper - En hashmap där key är värden som tärningarna fått och value är antalet tärningar som fått
     *                   det värdet.
     * @return - En hashmap som innehåller antalet tärningar som ska bli markerbara för varje tärningsvärde
     */
    public HashMap<Integer, Integer> scoreCalculator(HashMap<Integer, Integer> diceMapper) {
        HashMap<Integer, Integer> selectableDice = new HashMap<Integer, Integer>();
        for(int i = 1; i < 7; i++) {
            if(diceMapper.get(i) != null) {
                if(diceMapper.size() == 6) {
                    selectableDice.put(i, 1);
                } else if(diceMapper.get(i) >= 3 && diceMapper.get(i) != 6) {
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
                } else if(diceMapper.get(i) == 6) {
                    if(i == 1) {
                        potentialThrowScore += 2*1000;
                        selectableDice.put(i, diceMapper.get(i));
                    } else if(i == 5) {
                        potentialThrowScore += 2*(i*100);
                        selectableDice.put(i, diceMapper.get(i));
                    }  else {
                        potentialThrowScore += 2*(i*100);
                        selectableDice.put(i, 6);
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
            }
        }
        if(diceMapper.size() == 6) {
            potentialThrowScore += 1000;
        }
        return selectableDice;
    }

    /**
     * Denna metod räknar ut vad den markerade poängen blir när en tärning markerats.
     * @param value - En int med värdet för tärningen som markerats.
     */
    public void calculateScoreForSelectingDiceValue(int value) {
        if(thisThrowScore.get(value) != null) {
            thisThrowScore.put(value, thisThrowScore.get(value) + 1);
        } else {
            thisThrowScore.put(value, 1);
        }

        if(thisThrowScore.size() == 6) {
            selectedScore = 1000;
        } else if(thisThrowScore.get(value) == 3) {
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
            }
        }
    }

    /**
     * Denna metod räknar ut vad den markerade poängen blir när en tärning avmarkerats.
     * @param value - En int med värdet för tärningen som avmarkerats.
     */
    public void calculateScoreForDeSelectingDiceValue(int value) {
        if(thisThrowScore.get(value) != null && thisThrowScore.get(value) == 1) {
            thisThrowScore.remove(value);
        } else if(thisThrowScore.get(value) != null){
            thisThrowScore.put(value, thisThrowScore.get(value) - 1);
        }

        if(thisThrowScore.size() == 5 && thisThrowScore.containsKey(value) == false) {
            if(value == 1) {
                selectedScore = 50;
            } else if (value == 5) {
                selectedScore = 100;
            } else {
                selectedScore = 150;
            }
        } else if(thisThrowScore.get(value) != null && thisThrowScore.get(value) == 2) {
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
            }
        }
    }

    /**
     * Återställer en hashmap som används i den här klassen för beräkningar samt den markerade poängen och potentiella
     * poängen.
     */
    public void resetScoreCalculator() {
        thisThrowScore.clear();
        selectedScore = 0;
        potentialThrowScore = 0;
    }

    /**
     *
     * @return - En int med den potentiella poängen.
     */
    public int getPotentialThrowScore() {
        return potentialThrowScore;
    }

    /**
     * Sätter den potentiella poängen.
     * @param potentialThrowScore - En int som potentiella poängen ska bli.
     */
    public void setPotentialThrowScore(int potentialThrowScore) {
        this.potentialThrowScore = potentialThrowScore;
    }

    /**
     *
     * @return - En int med poängen för rundan.
     */
    public int getTurnScore() {
        return turnScore;
    }

    /**
     * Sätter poängen för rundan.
     * @param turnScore - En int som poängen för rundan ska bli.
     */
    public void setTurnScore(int turnScore) {
        this.turnScore = turnScore;
    }

    /**
     *
     * @return - En int med totala poängen.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Sätter den totala poängen.
     * @param totalScore - En int som totala poängen ska bli.
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     *
     * @return - En int med den markerade poängen.
     */
    public int getSelectedScore() { return selectedScore; }

    /**
     * Sätter den markerade poängen.
     * @param selectedScore - En int som den markerade poängen ska bli.
     */
    public void setSelectedScore(int selectedScore) {this.selectedScore = selectedScore; }
}
