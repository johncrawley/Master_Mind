package com.jcrawley.codebreaker.game.model;

import com.jcrawley.codebreaker.game.Clue;
import com.jcrawley.codebreaker.game.PegColor;

import java.util.ArrayList;
import java.util.List;

public class GameGrid {
    private List<GameRow> gameRows;
    private final int numberOfRows;
    private final int pegsPerRow;
    private List<PegColor> solutionPegs;

    public GameGrid(int numberOfRows, int pegsPerRow){
        System.out.println("Entering GameGrid()");
        this.numberOfRows = numberOfRows;
        this.pegsPerRow = pegsPerRow;
        initSolutionPegs();
        init();
        System.out.println("exiting GameGrid()");
    }


    private void initSolutionPegs(){
        solutionPegs = new ArrayList<>();
        for(int i = 0; i < pegsPerRow; i++){
            solutionPegs.add(PegColor.EMPTY);
        }
    }


    public void init(){
        gameRows = new ArrayList<>(numberOfRows);
        for(int i = 0; i < numberOfRows; i++){
            gameRows.add(new GameRow(pegsPerRow));
        }
    }


    public void setClues(int rowIndex, List<Clue> clues){
        gameRows.get(rowIndex).setClues(clues);
    }


    public void setSolutionPegs(List<PegColor> solutionPegs){
        this.solutionPegs = new ArrayList<>(solutionPegs);
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


    public List<PegColor> getSolutionPegs(){
        return new ArrayList<>(solutionPegs);
    }


}
