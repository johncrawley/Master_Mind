package com.jcrawley.mastermind.game;

import java.util.ArrayList;
import java.util.List;

public class GameRow {

    private final List<PegColor> pegColors = new ArrayList<>();
    private List<Clue> clues;

    public void addPegColor(PegColor pegColor){
        pegColors.add(pegColor);
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

