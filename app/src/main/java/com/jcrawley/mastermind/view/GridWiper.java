package com.jcrawley.mastermind.view;

import android.os.Handler;
import android.os.Looper;
import com.jcrawley.mastermind.game.Game;
import com.jcrawley.mastermind.game.PegColor;

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
        resetRowAfterDelay(numberOfRows - 1);
    }


    private void resetRowAfterDelay(int index){
        if(index < 0){
            gameView.highlightRowBackground(0);
            game.enableUserInput();
            return;
        }
        new Handler(Looper.getMainLooper())
                .postDelayed(()->{
                    resetRow(index);
                    resetRowAfterDelay(index - 1);
                }, 90);
    }


    private void resetRow(int index){
        resetAllPegsIn(index);
        gameView.resetAllCluesIn(index);
        gameView.resetRowBackground(index);
    }


    private void resetAllPegsIn(int rowIndex) {
        for (int i = 0; i < game.getPegsPerRow(); i++) {
            gameView.setPegColor(rowIndex, i, PegColor.EMPTY);
        }
    }

}
