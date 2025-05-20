package com.jcrawley.mastermind.game;

import androidx.lifecycle.viewmodel.CreationExtras;

import com.jcrawley.mastermind.R;

public enum PegColor {
    RED(R.color.game_red, R.string.button_image_desc_red),
    BLUE(R.color.game_blue, R.string.button_image_desc_blue),
    GREEN(R.color.game_green, R.string.button_image_desc_green),
    YELLOW(R.color.game_yellow, R.string.button_image_desc_yellow),
    ORANGE(R.color.game_orange, R.string.button_image_desc_orange),
    PURPLE(R.color.game_purple, R.string.button_image_desc_purple),
    BROWN(R.color.game_brown, R.string.button_image_desc_brown),
    PINK(R.color.game_pink, R.string.button_image_desc_pink),

    EMPTY(R.color.peg_default_background, R.string.button_image_desc_red, false),
    TEST(R.color.teal_200, R.string.button_image_desc_red, false);


    private final int colorId;
    private final boolean canColorBePicked;
    private final int contentDescriptionId;


    PegColor(int colorId, int contentDescriptionId) {
        this(colorId, contentDescriptionId, true);
    }


    PegColor(int colorId, int contentDescriptionId, boolean canColorBePicked) {
        this.canColorBePicked = canColorBePicked;
        this.contentDescriptionId = contentDescriptionId;
        this.colorId = colorId;
    }


    public int getColorId(){
        return colorId;
    }

    public int getContentDescriptionId(){
        return contentDescriptionId;
    }

    public boolean canColorBePicked(){
        return canColorBePicked;
    }
}