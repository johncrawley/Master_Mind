package com.jcrawley.mastermind.view;

import com.jcrawley.mastermind.game.Clue;
import com.jcrawley.mastermind.game.PegColor;

import java.util.List;

public interface GameView {

    void showGoodGameOver(int numberOfTries);
    void showBadGameOver();

    void update(int rowIndex, List<Clue> clues);
    void updateRow(int row, List<PegColor> pegColors, List<Clue> clues);
    void setPegColor(PegColor pegColor, int row, int index);
    void highlightRowBackground(int rowIndex);
    void resetAllRows();
    void setupSolutionPegs(List<PegColor> pegs);
}
