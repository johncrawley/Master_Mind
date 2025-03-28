package com.jcrawley.mastermind.game;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {
    private List<GameRow> gameRows;
    private final int numberOfRows;

    public GameGrid(int numberOfRows){
        this.numberOfRows = numberOfRows;
        init();
    }


    public void setClues(List<Clue> clues, int rowIndex){
        gameRows.get(rowIndex).setClues(clues);
    }


    public List<Clue> getCluesAtRow(int rowIndex){
        return gameRows.get(rowIndex).getClues();
    }


    public void addPegColor(PegColor pegColor, int rowIndex){
        gameRows.get(rowIndex).addPegColor(pegColor);
    }


    public PegColor getPegColorAt(int rowIndex, int columnIndex){
        return gameRows.get(rowIndex).getPegColors().get(columnIndex);
    }


    public void init(){
        gameRows = new ArrayList<>(numberOfRows);
        for(int i = 0; i < numberOfRows; i++){
            gameRows.add(new GameRow());
        }
    }


}
