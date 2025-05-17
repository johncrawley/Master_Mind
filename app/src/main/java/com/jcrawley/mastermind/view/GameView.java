package com.jcrawley.mastermind.view;

import com.jcrawley.mastermind.game.Clue;
import com.jcrawley.mastermind.game.PegColor;

import java.util.List;

public interface GameView {

    void showGoodGameOver(int numberOfTries);
    void showBadGameOver();

    void updateClues(int rowIndex, List<Clue> clues);
    void updateRow(int row, List<PegColor> pegColors, List<Clue> clues);
    void setPegColor(int row, int index, PegColor pegColor);
    void highlightRowBackground(int rowIndex);
    void resetAllRows();
    void resetAllRowsInstantly();
    void setupSolutionPegs(List<PegColor> pegs);

    void resetRowBackground(int index);
    void resetAllCluesIn(int index);
    void highlightAllRowsUpToAndIncluding(int rowNumber);
    void notifyInitializationComplete();

    void disableUndoButton();
    void enableUndoButton();
}
