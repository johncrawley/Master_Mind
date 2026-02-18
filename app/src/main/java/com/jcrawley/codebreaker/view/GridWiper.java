package com.jcrawley.codebreaker.view;

import android.os.Handler;
import android.os.Looper;
import com.jcrawley.codebreaker.game.Game;
import com.jcrawley.codebreaker.game.PegColor;

public class GridWiper {

    private final GameView gameView;
    private final Game game;
    private final int numberOfRows;

    public GridWiper(GameView gameView, Game game){
        this.gameView = gameView;
        this.game = game;
        this.numberOfRows = game.getNumberOfRows();
    }


    public void resetAllRows(){
        resetRowAfterDelay(numberOfRows - 1, 90);
    }


    public void resetAllRowsInstantly(){
        resetRowAfterDelay(numberOfRows - 1, 1);
    }


    private void resetRowAfterDelay(int index, int delayPerRow){
        if(index < 0){
            gameView.highlightRowBackground(0);
            gameView.notifyInitializationComplete();
            game.enableUserInput();
            return;
        }
        new Handler(Looper.getMainLooper())
                .postDelayed(()->{
                    resetRow(index);
                    resetRowAfterDelay(index - 1, delayPerRow);
                }, delayPerRow);
    }


    private void resetRow(int index){
        resetAllPegsInRow(index);
        gameView.resetAllCluesIn(index);
        gameView.resetRowBackground(index);
    }


    private void resetAllPegsInRow(int rowIndex) {
        for (int i = 0; i < game.getPegsPerRow(); i++) {
            gameView.setPegColor(rowIndex, i, PegColor.EMPTY);
        }
    }

}
