package com.example.greed;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

/**
 * Denna klass håller data för en tärning vilket är dess värde, om den är låst, om den är markerbar och om den är
 * markerad. Denna klass förlänger klassen ImageButton och är därför en view i layouten för aktiviteten myActivity, den
 * har därför tre konstruktorer. Klassen har metoder för att sätta bakgrunden på sig med olika bilder, dessa bilder
 * kan vara tre olika färger (vid, grå, röd) och ha 1-6 prickar på sig så som tärningar har.
 * Klassen har även en metod för att slumpa fram ett värde på tärningen mellan 1 och 6.
 */
public class Die extends ImageButton {

    private int dieValue;
    private boolean isLocked = false;
    private boolean isSelected = false;
    private boolean isSelectable = false;

    public Die(Context context) {
        super(context);
        setWhiteDieImage(1);
    }

    public Die(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWhiteDieImage(1);
    }

    public Die(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWhiteDieImage(1);
    }

    /**
     * Sätter bakgrunden till en grå tärning med ett angett värde.
     * @param number - En int som är värdet tärningen i bilden ska ha.
     */
    public void setGreyDieImage(int number) {
        if(number == 1) {
            this.setBackgroundResource(R.drawable.grey1);
        } else if(number == 2) {
            this.setBackgroundResource(R.drawable.grey2);
        } else if(number == 3) {
            this.setBackgroundResource(R.drawable.grey3);
        } else if(number == 4) {
            this.setBackgroundResource(R.drawable.grey4);
        } else if(number == 5) {
            this.setBackgroundResource(R.drawable.grey5);
        } else if(number == 6) {
            this.setBackgroundResource(R.drawable.grey6);
        }
    }

    /**
     * Sätter bakgrunden till en vit tärning med ett angett värde.
     * @param number - En int som är värdet tärningen i bilden ska ha.
     */
    public void setWhiteDieImage(int number) {
        if(number == 1) {
            this.setBackgroundResource(R.drawable.white1);
        } else if(number == 2) {
            this.setBackgroundResource(R.drawable.white2);
        } else if(number == 3) {
            this.setBackgroundResource(R.drawable.white3);
        } else if(number == 4) {
            this.setBackgroundResource(R.drawable.white4);
        } else if(number == 5) {
            this.setBackgroundResource(R.drawable.white5);
        } else if(number == 6) {
            this.setBackgroundResource(R.drawable.white6);
        }
    }

    /**
     * Sätter bakgrunden till en röd tärning med ett angett värde.
     * @param number - En int som är värdet tärningen i bilden ska ha.
     */
    public void setRedDieImage(int number) {
        if(number == 1) {
            this.setBackgroundResource(R.drawable.red1);
        } else if(number == 2) {
            this.setBackgroundResource(R.drawable.red2);
        } else if(number == 3) {
            this.setBackgroundResource(R.drawable.red3);
        } else if(number == 4) {
            this.setBackgroundResource(R.drawable.red4);
        } else if(number == 5) {
            this.setBackgroundResource(R.drawable.red5);
        } else if(number == 6) {
            this.setBackgroundResource(R.drawable.red6);
        }
    }

    /**
     *
     * @return - En boolean om tärningen är låst eller inte.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Sätter om tärningen ska vara låst eller inte.
     * @param isLocked - En boolean om tärningen ska vara låst eller inte.
     */
    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    /**
     *
     * @return - En int som är värdet på tärningen.
     */
    public int getDieValue() {
        return dieValue;
    }

    /**
     * Sätter värdet på tärningen.
     * @param dieValue - En int som är vad värdet på tärningen ska vara.
     */
    public void setDieValue(int dieValue) {
        this.dieValue = dieValue;
    }

    /**
     * Slumpar fram ett värde mellan 1 och 6 som tärningen ska ha.
     */
    public void throwDie() {
        Random diceRoller = new Random();
        dieValue = diceRoller.nextInt(6) + 1;
        setWhiteDieImage(dieValue);
    }

    /**
     *
     * @return - En boolean om tärningen är markerad eller inte.
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sätter om tärningen ska vara markerad eller inte.
     * @param isSelected - En boolean om tärningen ska vara markerad eller inte.
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     *
     * @return - En boolean om tärningen är markerbar eller inte.
     */
    public boolean isSelectable() {
        return isSelectable;
    }

    /**
     * Sätter om tärningen ska vara markerbar eller inte.
     * @param isSelectable - En boolean om tärningen ska vara markerbar eller inte.
     */
    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
}
