package com.jcrawley.mastermind.view;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationHelper {

    public static void showPanel(View panel){
        var animation = new AlphaAnimation(0.0f, 1.0f);
        panel.setZ(100);
        animation.setDuration(450);
        animation.setFillAfter(true);
        panel.setVisibility(VISIBLE);
        panel.startAnimation(animation);
    }


    public static void hidePanel(View panel, Runnable onFinish){
        var animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                panel.setVisibility(GONE);
                onFinish.run();
            }
        });
        panel.startAnimation(animation);
    }

}
