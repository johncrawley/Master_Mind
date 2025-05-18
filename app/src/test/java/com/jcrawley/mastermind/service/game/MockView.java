package com.jcrawley.mastermind.service.game;

import com.jcrawley.mastermind.game.Clue;
import com.jcrawley.mastermind.game.PegColor;
import com.jcrawley.mastermind.view.GameView;

import java.util.List;

public class MockView implements GameView {

    private int callsToSetPegColor;
    private boolean isUndoDisabled = true;
    private boolean haveAllRowsBeenReset = false;



    public int getCallsToSetPegColor(){
        return callsToSetPegColor;
    }


    public boolean isUndoDisabled(){
        return isUndoDisabled;
    }


    public boolean haveAllRowsBeenReset(){
        return this.haveAllRowsBeenReset;
    }


    public boolean isUndoEnabled(){
        return !isUndoDisabled;
    }


    @Override
    public void disableUndoButton() {
        isUndoDisabled = true;
    }


    @Override
    public void enableUndoButton() {
        isUndoDisabled = false;
    }


    @Override
    public void showGoodGameOver(int numberOfTries) {

    }

    @Override
    public void showBadGameOver() {

    }

    @Override
    public void updateClues(int rowIndex, List<Clue> clues) {

    }

    @Override
    public void updateRow(int row, List<PegColor> pegColors, List<Clue> clues) {

    }

    @Override
    public void setPegColor(int row, int index, PegColor pegColor) {
        callsToSetPegColor++;
    }

    @Override
    public void highlightRowBackground(int rowIndex) {

    }

    @Override
    public void resetAllRows() {
        this.haveAllRowsBeenReset = true;
    }


    @Override
    public void resetAllRowsInstantly() {

    }

    @Override
    public void setupSolutionPegs(List<PegColor> pegs) {

    }

    @Override
    public void resetRowBackground(int index) {

    }

    @Override
    public void resetAllCluesIn(int index) {

    }

    @Override
    public void highlightAllRowsUpToAndIncluding(int rowNumber) {

    }

    @Override
    public void notifyInitializationComplete() {

    }
}
