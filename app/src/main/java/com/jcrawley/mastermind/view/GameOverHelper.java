package com.jcrawley.mastermind.view;

import static android.view.View.VISIBLE;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jcrawley.mastermind.MainActivity;
import com.jcrawley.mastermind.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameOverHelper {

    private ViewGroup gameOverPanel;
    private TextView gameOverTitleText, gameOverMessageText;
    private final MainActivity mainActivity;
    private final AtomicBoolean hasGameOverBeenDismissed = new AtomicBoolean(false);


    public GameOverHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        setupGameOverScreen();
    }


    public void showBadGameOver() {
        gameOverMessageText.setText(R.string.game_over_message_fail);
        gameOverTitleText.setText(R.string.game_over_title);
        AnimationHelper.showPanel(gameOverPanel);
    }


    public void showGoodGameOver(int numberOfTries) {
        gameOverTitleText.setText(R.string.game_over_title_success);
        showSuccessMessage(numberOfTries);
        AnimationHelper.showPanel(gameOverPanel);
    }


    private void setupGameOverScreen() {
        gameOverPanel = mainActivity.findViewById(R.id.gameOverPanelInclude);
        gameOverTitleText = mainActivity.findViewById(R.id.gameOverTitleText);
        gameOverMessageText = mainActivity.findViewById(R.id.gameOverMessageText);
        if(gameOverPanel == null){
            return;
        }
        gameOverPanel.setOnClickListener(v -> dismissGameOver());
    }


    private void dismissGameOver(){
        if(hasGameOverBeenDismissed.get() || gameOverPanel.getVisibility() != VISIBLE){
            return;
        }
        hasGameOverBeenDismissed.set(true);
        AnimationHelper.hidePanel(gameOverPanel, ()->{
            mainActivity.resetAllRows();
            gameOverPanel.setZ(-1);
            var game = mainActivity.getGame();
            if(game != null){
                game.setupNewGame();
            }
            hasGameOverBeenDismissed.set(false);
        });

    }


    private void showSuccessMessage(int numberOfTries) {
        if (numberOfTries == 1) {
            gameOverMessageText.setText(R.string.number_of_tries_one);
        } else {
            var msg = mainActivity.getString(R.string.number_of_tries, numberOfTries);
            gameOverMessageText.setText(msg);
        }
    }


}
