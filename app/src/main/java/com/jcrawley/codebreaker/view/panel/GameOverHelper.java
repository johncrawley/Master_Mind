package com.jcrawley.codebreaker.view.panel;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.jcrawley.codebreaker.R;
import com.jcrawley.codebreaker.view.AnimationHelper;
import com.jcrawley.codebreaker.game.GameView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameOverHelper {

    private ViewGroup panel;
    private TextView gameOverTitleText, gameOverMessageText;
    private final AtomicBoolean hasPanelBeenDismissed = new AtomicBoolean(false);
    private final AtomicBoolean isDismissTimerActive = new AtomicBoolean(false);
    private final ScheduledExecutorService executor;
    private OnBackPressedCallback dismissPanelOnBackPressedCallback;
    private final Context context;
    private final GameView gameView;

    public GameOverHelper(Context context, View parentView, GameView gameView, Fragment parentFragment){
        this.gameView = gameView;
        setupGameOverScreen(parentView);
        this.context = context;
        executor = Executors.newSingleThreadScheduledExecutor();
        onBackButtonPressed(parentFragment, this::dismissPanel);
    }


    private void onBackButtonPressed(Fragment parentFragment, Runnable action){
        dismissPanelOnBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                action.run();
            }
        };
        parentFragment.requireActivity()
                .getOnBackPressedDispatcher()
                .addCallback(parentFragment.getViewLifecycleOwner(), dismissPanelOnBackPressedCallback);
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


    private void setupGameOverScreen(View parentView) {
        panel = parentView.findViewById(R.id.gameOverPanelInclude);
        gameOverTitleText = parentView.findViewById(R.id.gameOverTitleText);
        gameOverMessageText = parentView.findViewById(R.id.createdByText);
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
        gameView.resetAllRows();
        panel.setZ(-1);
        var game = gameView.getGame();
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
            var msg = context.getString(R.string.number_of_tries, numberOfTries);
            gameOverMessageText.setText(msg);
        }
    }


}
