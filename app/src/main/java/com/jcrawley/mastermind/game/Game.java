package com.jcrawley.mastermind.game;

import com.jcrawley.mastermind.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private final List<PegColor> currentAnswer = new ArrayList<>();
    private int numberOfTries;
    private Random random;
    PegColor[] possibleColors = PegColor.values();
    List<PegColor> solution = new ArrayList<>(pegsPerRow);
    private GameGrid gameGrid = new GameGrid(numberOfRows);


    private GameView gameView;


    public void setView(GameView gameView){
        this.gameView = gameView;
        updateViewWithGameGrid();
    }

    private void updateViewWithGameGrid(){
        for(int i = 0; i < numberOfRows; i++){
            gameView.update(i, gameGrid.getCluesAtRow(i));
        }
    }


    public int getPegsPerRow(){
        return pegsPerRow;
    }


    public void checkCurrentAnswer() {
        var clues = GameUtils.generateClues(currentAnswer, solution);
        gameView.update(currentRow, clues);
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

        gameView.setPegColor(pegColor, currentRow, currentIndex);
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


    private void highlightCurrentBackgroundRow(){
        if(currentRow < numberOfRows){
            gameView.highlightRowBackground(currentRow);
        }
    }


    private void setupRandomAnswer(){
        random = new Random(System.currentTimeMillis());
        solution.clear();
        for(int i = 0; i < pegsPerRow; i++ ){
            solution.add(getRandomPegColor());
        }
        gameView.setupSolutionPegs(solution);
    }


    private PegColor getRandomPegColor(){
        int randomIndex = random.nextInt(possibleColors.length);
        return possibleColors[randomIndex];
    }
}
