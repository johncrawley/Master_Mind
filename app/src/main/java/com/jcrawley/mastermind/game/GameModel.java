package com.jcrawley.mastermind.game;

import com.jcrawley.mastermind.game.model.GameGrid;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameModel {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private int numberOfTries;
    private final GameSolution gameSolution = new GameSolution(pegsPerRow);
    private final GameGrid gameGrid = new GameGrid(numberOfRows, pegsPerRow);
    private boolean isGameInProgress;
    private final AtomicBoolean isUserInputEnabled = new AtomicBoolean(false);
    private GamePhase gamePhase = GamePhase.RUNNING;


    public GamePhase getGamePhase(){
        return gamePhase;
    }


    public int getNumberOfTries() {
        return numberOfTries;
    }


    public void setGamePhase(GamePhase gamePhase){
        this.gamePhase = gamePhase;
    }


    public int getCurrentIndex(){
        return currentIndex;
    }


    public int getCurrentRow(){
        return currentRow;
    }


    public int getPegsPerRow(){
        return pegsPerRow;
    }


    public List<Clue> assignClues(){
        var currentAnswer = gameGrid.getPegColorsAtRow(currentRow);
        var clues = GameUtils.generateClues(currentAnswer, gameSolution.get());
        gameGrid.setClues(currentRow, clues);
        return clues;
    }


    public boolean isAnswerCorrect(List<Clue> clues){
        return clues.stream()
                .allMatch(c -> c == Clue.BULL);
    }


    public List<PegColor> getSolution(){
        return gameSolution.get();
    }


    public void incrementCurrentIndex(){
        currentIndex++;
    }


    public void addPeg(PegColor pegColor){
        if(!isUserInputEnabled()){
            return;
        }
        isGameInProgress = true;
        pegsPlaced++;
        if(pegsPlaced > MAX_PEGS){
            return;
        }
        gameGrid.setPegColor(currentRow, currentIndex, pegColor);
    }


    public List<PegColor> generateSolution(){
        var solution = gameSolution.generate();
        gameGrid.setSolutionPegs(solution);
        return solution;
    }


    public  void setSolution(PegColor... solutionPegs){
        gameSolution.set(solutionPegs);
    }


    public void removePeg(){
        if(currentIndex == 0){
            return;
        }
        currentIndex--;
        pegsPlaced--;
        gameGrid.setPegColor(currentRow, currentIndex, PegColor.EMPTY);
    }


    public PegColor getPegColorAt(int index){
        return gameGrid.getPegColorsAtRow(currentRow).get(index);
    }


    public int getPegCount(){
        return pegsPlaced;
    }


    public void moveToNextRow(){
        currentIndex = 0;
        numberOfTries++;
        currentRow++;
    }


    public boolean isGameInProgress(){
        return isGameInProgress;
    }


    public GameGrid getGrid(){
        return gameGrid;
    }


    public void setupFirstGame(){
        numberOfTries = 1;
        currentRow = 0;
        currentIndex = 0;
        pegsPlaced = 0;
        gameGrid.init();
        var solution = gameSolution.generate();
        gameGrid.setSolutionPegs(solution);
        isGameInProgress = false;
        enableUserInput();
        gamePhase = GamePhase.RUNNING;
    }




    public int getNumberOfRows(){
        return numberOfRows;
    }

    public boolean isCurrentRowValid(){
        return currentRow < numberOfRows;
    }

    public boolean isCurrentIndexAtEndOfRow(){
        return currentIndex >= pegsPerRow;
    }

    public boolean areAllPegsPlaced(){
        return pegsPlaced >= MAX_PEGS;
    }

    public void enableUserInput(){
        isUserInputEnabled.set(true);
    }


    public void disableUserInput(){
        isUserInputEnabled.set(false);
    }


    public boolean isUserInputEnabled(){
        return isUserInputEnabled.get() && gamePhase == GamePhase.RUNNING;
    }


}
