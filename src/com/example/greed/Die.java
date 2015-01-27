package com.example.greed;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Random;

/**
 * Created by Markus on 22.1.2015.
 */
public class Die extends ImageButton{

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

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }


    public int getDieValue() {
        return dieValue;
    }

    public void throwDie() {
        Random diceRoller = new Random();
        dieValue = diceRoller.nextInt(6) + 1;
        setWhiteDieImage(dieValue);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
    }
}
