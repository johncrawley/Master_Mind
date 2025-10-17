package com.jcrawley.mastermind.game;

import com.jcrawley.mastermind.game.model.GameGrid;
import com.jcrawley.mastermind.view.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private int numberOfTries;
    private final GameSolution gameSolution = new GameSolution(pegsPerRow);
    private final GameGrid gameGrid = new GameGrid(numberOfRows, pegsPerRow);
    private GameView gameView;
    private boolean isGameInProgress;
    private final AtomicBoolean isUserInputEnabled = new AtomicBoolean(false);
    private enum GamePhase { RUNNING, GAME_OVER_GOOD, GAME_OVER_BAD }
    private GamePhase gamePhase = GamePhase.RUNNING;


    public void init(){
        updateUndoButtonState();
        if(!isGameInProgress){
            gameView.resetAllRowsInstantly();
            setupFirstGame();
        }
        else{
            updateViewWithGameState();
            updateViewWithGamePhase();
            gameView.notifyInitializationComplete();
        }
    }


    public List<PegColor> getPegsAtRow(int row){
        return gameGrid.getPegColorsAtRow(row);
    }


    public int getCurrentIndex(){
        return currentIndex;
    }


    public int getCurrentRow(){
        return currentRow;
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
        var clues = generateClues();
        gameGrid.setClues(currentRow, clues);
        gameView.updateClues(currentRow, clues);
        if (isAnswerCorrect(clues)) {
            handleCorrectAnswer();
        }
        else if (pegsPlaced >= MAX_PEGS) {
            handleGameOver();
        }
    }


    private void handleCorrectAnswer(){
        disableUserInput();
        gamePhase = GamePhase.GAME_OVER_GOOD;
        gameView.showGoodGameOver(numberOfTries);
    }


    private void handleGameOver(){
        disableUserInput();
        gamePhase = GamePhase.GAME_OVER_BAD;
        gameView.showBadGameOver();
    }


    private List<Clue> generateClues(){
        var currentAnswer = gameGrid.getPegColorsAtRow(currentRow);
        return GameUtils.generateClues(currentAnswer, gameSolution.get());
    }


    private boolean isAnswerCorrect(List<Clue> clues){
        return clues.stream()
                .allMatch(c -> c == Clue.BULL);
    }


    public List<PegColor> getSolution(){
        return gameSolution.get();
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
        setPegColor(pegColor);
        checkAnswerAtRowEnd();
        updateUndoButtonState();
    }


    private void setPegColor(PegColor pegColor){
        gameGrid.setPegColor(currentRow, currentIndex, pegColor);
        gameView.setPegColor(currentRow, currentIndex, pegColor);
    }


    private void checkAnswerAtRowEnd(){
        if(++currentIndex >= pegsPerRow){
            checkCurrentAnswer();
            moveToNextRow();
        }
    }


    private void updateUndoButtonState(){
        if(currentIndex == 0){
            gameView.disableUndoButton();
        }
        else if(currentIndex == 1){
            gameView.enableUndoButton();
        }
    }


    private void setUndoButtonState(){
        if(currentIndex == 0){
            gameView.disableUndoButton();
        }
        else {
            gameView.enableUndoButton();
        }
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
        gameView.setPegColor(currentRow, currentIndex, PegColor.EMPTY);
        gameGrid.setPegColor(currentRow, currentIndex, PegColor.EMPTY);
        setUndoButtonState();
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
        highlightCurrentBackgroundRow();
    }


    public void setupFirstGame(){
        numberOfTries = 1;
        currentRow = 0;
        currentIndex = 0;
        pegsPlaced = 0;
        gameGrid.init();
        setupRandomAnswer();
        isGameInProgress = false;
        enableUserInput();
        gamePhase = GamePhase.RUNNING;
    }


    public void startNewGame(){
        setupFirstGame();
        disableUserInput();
        gameView.resetAllRows();
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
