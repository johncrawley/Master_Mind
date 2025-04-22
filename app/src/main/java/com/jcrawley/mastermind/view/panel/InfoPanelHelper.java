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
        setupPanelDetails();
    }


    private void setupPanelDetails(){
        setupClueTip(R.id.clueTip1, R.string.clues_tip_green, R.color.clue_bull);
        setupClueTip(R.id.clueTip2, R.string.clues_tip_yellow, R.color.clue_cow);
    }


    private void setupClueTip(int parentId, int strId, int colorId){
        ViewGroup parent = mainActivity.findViewById(parentId);
        setupClueTipColor(parent, colorId);
        setupClueTipText(parent, strId);
    }


    private void setupClueTipColor(ViewGroup parent, int colorId){
        ViewGroup clue = parent.findViewById(R.id.clue);
        int color = mainActivity.getColor(colorId);
        clue.setBackgroundColor(color);
    }


    private void setupClueTipText(ViewGroup parent, int strId){
        TextView clueText = parent.findViewById(R.id.clueTipText);
        String str = mainActivity.getString(strId);
        clueText.setText(str);
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
