package com.jcrawley.mastermind;

public enum PegColor {
    RED(R.color.game_red),
    BLUE(R.color.game_blue),
    GREEN(R.color.game_green),
    YELLOW(R.color.game_yellow),
    ORANGE(R.color.game_orange),
    PURPLE(R.color.game_purple),
    BROWN(R.color.game_brown),
    PINK(R.color.game_pink);


    private final int colorId;

    PegColor(int colorId) {
        this.colorId = colorId;
    }


    public int getColorId(){
        return colorId;
    }
}