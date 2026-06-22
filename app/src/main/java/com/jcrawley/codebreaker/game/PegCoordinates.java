package com.jcrawley.codebreaker.game;

public record PegCoordinates(int row, int column) {

    public PegCoordinates(){
        this(-1, -1);
    }


    public boolean isNull(){
        return row == -1 || column == -1;
    }
}
