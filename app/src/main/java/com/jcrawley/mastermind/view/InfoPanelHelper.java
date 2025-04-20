package com.jcrawley.mastermind.view;

import static android.view.View.VISIBLE;

import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;

import com.jcrawley.mastermind.MainActivity;
import com.jcrawley.mastermind.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class InfoPanelHelper {

    private final ViewGroup panel;
    private OnBackPressedCallback dismissPanelOnBackPressedCallback;
    private final AtomicBoolean hasPanelBeenDismissed = new AtomicBoolean(false);
    private final AtomicBoolean isDismissTimerActive = new AtomicBoolean(false);
    private final ScheduledExecutorService executor;
    private final MainActivity mainActivity;


    public InfoPanelHelper(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        panel = mainActivity.findViewById(R.id.infoPanelInclude);
        executor = Executors.newSingleThreadScheduledExecutor();
        setupDismissSearchOnBackPressed();
        panel.setOnClickListener(v -> dismissPanel());
    }


    public void showPanel() {
        startDismissTimer();
        AnimationHelper.showPanel(panel);
        dismissPanelOnBackPressedCallback.setEnabled(true);
    }


    private void startDismissTimer(){
        isDismissTimerActive.set(true);
        executor.schedule(()-> isDismissTimerActive.set(false), 1, TimeUnit.SECONDS);
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
        panel.setZ(-1);
        hasPanelBeenDismissed.set(false);
        dismissPanelOnBackPressedCallback.setEnabled(false);
    }


}
