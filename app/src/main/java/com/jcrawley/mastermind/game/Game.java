package com.jcrawley.mastermind.game;

import com.jcrawley.mastermind.game.model.GameGrid;
import com.jcrawley.mastermind.view.GameView;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private final List<PegColor> currentAnswer = new ArrayList<>();
    private int numberOfTries;
    private final GameSolution gameSolution = new GameSolution(pegsPerRow);
    private final GameGrid gameGrid = new GameGrid(numberOfRows, pegsPerRow);
    private GameView gameView;


    public void setView(GameView gameView){
        this.gameView = gameView;
        updateViewWithGameGrid();
    }


    private void updateViewWithGameGrid(){
        for(int i = 0; i < numberOfRows; i++){
            gameView.updateClues(i, gameGrid.getCluesAtRow(i));
            gameView.updateRow(i, gameGrid.getPegColorsAtRow(i), gameGrid.getCluesAtRow(i));
        }
        gameView.setupSolutionPegs(gameGrid.getSolutionPegs());
    }


    public int getPegsPerRow(){
        return pegsPerRow;
    }


    public void checkCurrentAnswer() {
        var clues = GameUtils.generateClues(currentAnswer, gameSolution.get());
        gameGrid.setClues(currentRow, clues);
        gameView.updateClues(currentRow, clues);
        if (isAnswerCorrect(clues)) {
            gameView.showGoodGameOver(numberOfTries);
            return;
        }
        if (pegsPlaced >= MAX_PEGS) {
            gameView.showBadGameOver();
        }
    }


    private boolean isAnswerCorrect(List<Clue> clues){
        return clues.stream()
                .allMatch(c -> c == Clue.BULL);
    }


    public void addPegColorAtCurrentIndex(PegColor pegColor){
        pegsPlaced++;
        if(pegsPlaced > MAX_PEGS){
            return;
        }
        gameGrid.setPegColor(currentRow, currentIndex, pegColor);
        gameView.setPegColor(currentRow, currentIndex, pegColor);
        currentAnswer.add(pegColor);
        if(++currentIndex >= pegsPerRow){
            checkCurrentAnswer();
            moveToNextRow();
        }
    }


    public void moveToNextRow(){
        currentIndex = 0;
        numberOfTries++;
        currentRow++;
        currentAnswer.clear();
        highlightCurrentBackgroundRow();
    }


    public void resetGame(){
        gameView.resetAllRows();
        numberOfTries = 1;
        currentRow = 0;
        currentIndex = 0;
        pegsPlaced = 0;
        currentAnswer.clear();
        gameGrid.init();
        setupRandomAnswer();
        highlightCurrentBackgroundRow();
    }


    public int getNumberOfRows(){
        return numberOfRows;
    }


    private void setupRandomAnswer(){
        var solution = gameSolution.generate();
        gameView.setupSolutionPegs(solution);
        gameGrid.setSolutionPegs(solution);
    }


    private void highlightCurrentBackgroundRow(){
        if(currentRow < numberOfRows){
            gameView.highlightRowBackground(currentRow);
        }
    }

}
