package com.jcrawley.mastermind.game;

import static com.jcrawley.mastermind.game.GamePhase.GAME_OVER_BAD;
import static com.jcrawley.mastermind.game.GamePhase.GAME_OVER_GOOD;

import com.jcrawley.mastermind.view.GameView;

import java.util.List;

public class Game {

    private GameView view;
    private final GameModel model;


    public Game(GameModel gameModel, GameView gameView){
        this.model = gameModel;
        this.view = gameView;
    }


    public void init(){
        updateUndoButtonState();
        if(!model.isGameInProgress()){
            view.resetAllRowsInstantly();
            setupFirstGame();
        }
        else{
            updateViewWithGameState();
            updateViewWithGamePhase();
            view.notifyInitializationComplete();
        }
    }


    public List<PegColor> getPegsAtRow(int row){
        var gameGrid = model.getGrid();
        return gameGrid.getPegColorsAtRow(row);
    }


    public int getCurrentIndex(){
        return model.getCurrentIndex();
    }


    public int getCurrentRow(){
        return model.getCurrentRow();
    }



    public void setView(GameView gameView){
        this.view = gameView;
    }


    public void updateViewWithGameState(){
        var grid = model.getGrid();
        for(int i = 0; i < model.getNumberOfRows(); i++){
            view.updateClues(i, grid.getCluesAtRow(i));
            view.updateRow(i, grid.getPegColorsAtRow(i), grid.getCluesAtRow(i));
        }
        view.setupSolutionPegs(grid.getSolutionPegs());
        view.highlightAllRowsUpToAndIncluding(model.getCurrentRow());
    }


    private void updateViewWithGamePhase(){
        var phase = model.getGamePhase();
        if(phase == GAME_OVER_GOOD){
            view.showGoodGameOver(model.getNumberOfTries());
        }
        else if(phase == GAME_OVER_BAD){
            view.showBadGameOver();
        }
    }


    public int getPegsPerRow(){
        return model.getPegsPerRow();
    }


    public void checkCurrentAnswer() {
        var clues = model.assignClues();
        view.updateClues(model.getCurrentRow(), clues);
        if (model.isAnswerCorrect(clues)) {
            handleCorrectAnswer();
        }
        else if (model.areAllPegsPlaced()) {
            handleGameOver();
        }
    }


    private void handleCorrectAnswer(){
        disableUserInput();
        model.setGamePhase(GAME_OVER_GOOD);
        view.showGoodGameOver(model.getNumberOfTries());
    }


    private void handleGameOver(){
        disableUserInput();
        model.setGamePhase(GAME_OVER_BAD);
        view.showBadGameOver();
    }


    public List<PegColor> getSolution(){
        return model.getSolution();
    }


    public void addPeg(PegColor pegColor){
        model.addPeg(pegColor);
        setViewPeg(pegColor);
        checkAnswerAtRowEnd();
        updateUndoButtonState();
    }


    private void checkAnswerAtRowEnd(){
        model.incrementCurrentIndex();
        if(model.isCurrentIndexAtEndOfRow()){
            checkCurrentAnswer();
            model.moveToNextRow();
            highlightCurrentBackgroundRow();
        }
    }


    private void updateUndoButtonState(){
        int index = model.getCurrentIndex();
        if(index == 0){
            view.disableUndoButton();
        }
        else if(index == 1){
            view.enableUndoButton();
        }
    }


    private void setUndoButtonState(){
        if(model.getCurrentIndex() == 0){
            view.disableUndoButton();
        }
        else {
            view.enableUndoButton();
        }
    }


    public void setSolution(PegColor... solutionPegs){
       model.setSolution(solutionPegs);
    }


    public void removePeg(){
        if(model.getCurrentIndex() == 0){
            return;
        }
        model.removePeg();
        setViewPeg(PegColor.EMPTY);
        setUndoButtonState();
    }


    private void setViewPeg(PegColor pegColor){
        view.setPegColor(model.getCurrentRow(), model.getCurrentIndex(), pegColor);
    }


    public PegColor getPegColorAt(int index){
        return model.getPegColorAt(index);
    }


    public int getPegCount(){
        return model.getPegCount();
    }


    public void startNewGame(){
        setupFirstGame();
        disableUserInput();
        view.resetAllRows();
    }


    public void setupFirstGame(){
        model.setupFirstGame();
        view.setupSolutionPegs(model.getSolution());
    }


    public int getNumberOfRows(){
        return model.getNumberOfRows();
    }


    public void enableUserInput(){
        model.enableUserInput();
    }


    public void disableUserInput(){
        model.disableUserInput();
    }


    private void highlightCurrentBackgroundRow(){
        if(model.isCurrentRowValid()){
            view.highlightRowBackground(model.getCurrentRow());
        }
    }

}
