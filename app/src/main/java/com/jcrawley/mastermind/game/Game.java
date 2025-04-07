package com.jcrawley.mastermind.game;

import com.jcrawley.mastermind.game.model.GameGrid;
import com.jcrawley.mastermind.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private boolean isGameInProgress;
    private final AtomicBoolean isUserInputEnabled = new AtomicBoolean(false);
    private enum GamePhase { RUNNING, GAME_OVER_GOOD, GAME_OVER_BAD }
    private GamePhase gamePhase = GamePhase.RUNNING;


    public void init(){
        if(!isGameInProgress){
            setupNewGame();
        }
    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }


    public void updateViewWithGameState(){
        for(int i = 0; i < numberOfRows; i++){
            gameView.updateClues(i, gameGrid.getCluesAtRow(i));
            gameView.updateRow(i, gameGrid.getPegColorsAtRow(i), gameGrid.getCluesAtRow(i));
        }
        gameView.setupSolutionPegs(gameGrid.getSolutionPegs());
        gameView.highlightAllRowsUpToAndIncluding(currentRow);
        updateViewWithGamePhase();
    }


    private void updateViewWithGamePhase(){
        if(gamePhase == GamePhase.GAME_OVER_GOOD){
            gameView.showGoodGameOver(numberOfTries);
        }
        else if(gamePhase == GamePhase.GAME_OVER_BAD){
            gameView.showBadGameOver();
        }
    }


    public int getPegsPerRow(){
        return pegsPerRow;
    }


    public void checkCurrentAnswer() {
        var clues = GameUtils.generateClues(currentAnswer, gameSolution.get());
        gameGrid.setClues(currentRow, clues);
        gameView.updateClues(currentRow, clues);
        if (isAnswerCorrect(clues)) {
            disableUserInput();
            gamePhase = GamePhase.GAME_OVER_GOOD;
            gameView.showGoodGameOver(numberOfTries);
            return;
        }
        if (pegsPlaced >= MAX_PEGS) {
            disableUserInput();
            gamePhase = GamePhase.GAME_OVER_BAD;
            gameView.showBadGameOver();
        }
    }


    private boolean isAnswerCorrect(List<Clue> clues){
        return clues.stream()
                .allMatch(c -> c == Clue.BULL);
    }


    public void addPegColorAtCurrentIndex(PegColor pegColor){
        if(!isUserInputEnabled()){
            return;
        }
        isGameInProgress = true;
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


    public void setupNewGame(){
        gameView.resetAllRows();
        numberOfTries = 1;
        currentRow = 0;
        currentIndex = 0;
        pegsPlaced = 0;
        currentAnswer.clear();
        gameGrid.init();
        setupRandomAnswer();
        isGameInProgress = false;
        enableUserInput();
        gamePhase = GamePhase.RUNNING;
    }


    public int getNumberOfRows(){
        return numberOfRows;
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
