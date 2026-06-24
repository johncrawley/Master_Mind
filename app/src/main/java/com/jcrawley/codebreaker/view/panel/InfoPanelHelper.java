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
    private final Context context;


    public InfoPanelHelper(Context context, Fragment parentFragment, View parentView){
        this.context = context;
        panel = parentView.findViewById(R.id.infoPanelInclude);
        executor = Executors.newSingleThreadScheduledExecutor();
        onBackButtonPressed(parentFragment, this::dismissPanel);
        panel.setOnClickListener(v -> dismissPanel());
        setupPanelDetails(parentView);
    }


    private void setupPanelDetails(View parent){
        setupClueTip(parent, R.id.clueTip1, R.string.clues_tip_green, R.color.clue_bull);
        setupClueTip(parent, R.id.clueTip2, R.string.clues_tip_yellow, R.color.clue_cow);
    }


    private void setupClueTip(View parent, int parentId, int strId, int colorId){
        ViewGroup viewGroup = parent.findViewById(parentId);
        setupClueTipColor(viewGroup, colorId);
        setupClueTipText(viewGroup, strId);
    }


    private void setupClueTipColor(ViewGroup parent, int colorId){
        ViewGroup clue = parent.findViewById(R.id.clue);
        int color = context.getColor(colorId);
        clue.setBackgroundColor(color);
    }


    private void setupClueTipText(ViewGroup parent, int strId){
        TextView clueText = parent.findViewById(R.id.clueTipText);
        String str = context.getString(strId);
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
