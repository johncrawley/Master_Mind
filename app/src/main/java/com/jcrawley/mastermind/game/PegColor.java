package com.jcrawley.mastermind.game;

import androidx.lifecycle.viewmodel.CreationExtras;

import com.jcrawley.mastermind.R;

public enum PegColor {
    RED(R.color.game_red),
    BLUE(R.color.game_blue),
    GREEN(R.color.game_green),
    YELLOW(R.color.game_yellow),
    ORANGE(R.color.game_orange),
    PURPLE(R.color.game_purple),
    BROWN(R.color.game_brown),
    PINK(R.color.game_pink),
    EMPTY(R.color.peg_default_background, false),
    TEST(R.color.teal_200, false);


    private final int colorId;
    private final boolean canColorBePicked;


    PegColor(int colorId) {
        this(colorId, true);
    }

    PegColor(int colorId, boolean canColorBePicked) {
        this.canColorBePicked = canColorBePicked;
        this.colorId = colorId;
    }


    public int getColorId(){
        return colorId;
    }

    public boolean canColorBePicked(){
        return canColorBePicked;
    }
}