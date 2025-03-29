package com.jcrawley.mastermind.game;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {
    private List<GameRow> gameRows;
    private final int numberOfRows;
    private final int pegsPerRow;

    public GameGrid(int numberOfRows, int pegsPerRow){
        this.numberOfRows = numberOfRows;
        this.pegsPerRow = pegsPerRow;
        init();
    }


    public void setClues(int rowIndex, List<Clue> clues){
        gameRows.get(rowIndex).setClues(clues);
    }


    public List<Clue> getCluesAtRow(int rowIndex){
        return gameRows.get(rowIndex).getClues();
    }


    public List<PegColor> getPegColorsAtRow(int rowIndex){
        return gameRows.get(rowIndex).getPegColors();
    }


    public void setPegColor(int rowIndex, int pegIndex, PegColor pegColor){
        gameRows.get(rowIndex).setPegColor(pegIndex, pegColor);
    }


    public PegColor getPegColorAt(int rowIndex, int columnIndex){
        return gameRows.get(rowIndex).getPegColors().get(columnIndex);
    }


    public void init(){
        gameRows = new ArrayList<>(numberOfRows);
        for(int i = 0; i < numberOfRows; i++){
            gameRows.add(new GameRow(pegsPerRow));
        }
    }


}
