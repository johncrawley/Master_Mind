package com.jcrawley.mastermind.view.panel;

import static android.view.View.VISIBLE;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;

import com.jcrawley.mastermind.MainActivity;
import com.jcrawley.mastermind.R;
import com.jcrawley.mastermind.view.AnimationHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameOverHelper {

    private ViewGroup panel;
    private TextView gameOverTitleText, gameOverMessageText;
    private final MainActivity mainActivity;
    private final AtomicBoolean hasPanelBeenDismissed = new AtomicBoolean(false);
    private final AtomicBoolean isDismissTimerActive = new AtomicBoolean(false);
    private final ScheduledExecutorService executor;
    private OnBackPressedCallback dismissPanelOnBackPressedCallback;


    public GameOverHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        setupGameOverScreen();
        executor = Executors.newSingleThreadScheduledExecutor();
        setupDismissSearchOnBackPressed();
    }


    private void setupDismissSearchOnBackPressed(){
        dismissPanelOnBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                dismissPanel();
            }
        };
        mainActivity.getOnBackPressedDispatcher().addCallback(mainActivity, dismissPanelOnBackPressedCallback);
    }


    public void showBadGameOver() {
        startDismissTimer();
        gameOverMessageText.setText(R.string.game_over_message_fail);
        gameOverTitleText.setText(R.string.game_over_title);
        AnimationHelper.showPanel(panel);
        dismissPanelOnBackPressedCallback.setEnabled(true);
    }


    public void showGoodGameOver(int numberOfTries) {
        startDismissTimer();
        gameOverTitleText.setText(R.string.game_over_title_success);
        showSuccessMessage(numberOfTries);
        AnimationHelper.showPanel(panel);
        dismissPanelOnBackPressedCallback.setEnabled(true);
    }


    private void startDismissTimer(){
        isDismissTimerActive.set(true);
        executor.schedule(()-> isDismissTimerActive.set(false), 1, TimeUnit.SECONDS);
    }


    private void setupGameOverScreen() {
        panel = mainActivity.findViewById(R.id.gameOverPanelInclude);
        gameOverTitleText = mainActivity.findViewById(R.id.gameOverTitleText);
        gameOverMessageText = mainActivity.findViewById(R.id.gameOverMessageText);
        if(panel == null){
            return;
        }
        panel.setOnClickListener(v -> dismissPanel());
    }


    private void dismissPanel(){
        if(hasPanelBeenDismissed.get()
                || panel.getVisibility() != VISIBLE
                || isDismissTimerActive.get()){
            return;
        }
        hasPanelBeenDismissed.set(true);
        AnimationHelper.hidePanel(panel, this::afterDismissed);
    }


    private void afterDismissed(){
        mainActivity.resetAllRows();
        panel.setZ(-1);
        var game = mainActivity.getGame();
        if(game != null){
            game.setupFirstGame();
        }
        hasPanelBeenDismissed.set(false);
        dismissPanelOnBackPressedCallback.setEnabled(false);
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
