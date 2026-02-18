package com.jcrawley.codebreaker.game.model;

import com.jcrawley.codebreaker.game.Clue;
import com.jcrawley.codebreaker.game.PegColor;

import java.util.ArrayList;
import java.util.List;

public class GameRow {

    private List<PegColor> pegColors;
    private List<Clue> clues;
    private final int pegsPerRow;


    public GameRow(int pegsPerRow){
        this.pegsPerRow = pegsPerRow;
        initPegColors();
        initClues();
    }


    private void initPegColors(){
        pegColors = new ArrayList<>(pegsPerRow);
        for( int i = 0; i < pegsPerRow; i++){
            pegColors.add(PegColor.EMPTY);
        }
    }


    private void initClues(){
        clues = new ArrayList<>(pegsPerRow);
        for( int i  = 0; i < pegsPerRow; i++){
            clues.add(Clue.WRONG);
        }
    }


    public void setPegColor(int index, PegColor pegColor){
        pegColors.set(index, pegColor);
    }


    public void setClues(List<Clue> clues){
        this.clues = new ArrayList<>(clues);
    }


    public List<Clue> getClues(){
        return clues;
    }


    public List<PegColor> getPegColors(){
        return pegColors;
    }
}

