package com.jcrawley.codebreaker.view.game;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;

import com.jcrawley.codebreaker.game.GameView;
import com.jcrawley.codebreaker.game.PegColor;

import java.util.List;

public class InputButtonHelper {


    private final GameView gameView;
    private boolean isInitialized;

    public InputButtonHelper(GameView gameView){
        this.gameView = gameView;
    }


    public void setupInputButtonRow(Context context, View parent, int panelId, List<PegColor> pegColors){
        ViewGroup panel = parent.findViewById(panelId);
        for(int i = 0; i < pegColors.size(); i++){
            setupInputButton(context, panel, i, pegColors.get(i));
        }
    }


    public void setInitialized(boolean isInitialized){
        this.isInitialized = isInitialized;
    }


    private void setupInputButton(Context context, ViewGroup panel, int index, PegColor pegColor){
        var buttonLayout = (ViewGroup) panel.getChildAt(index);
        var button = buttonLayout.getChildAt(0);
        setContentDescription(context, button, pegColor);
        setColorTint(context, button, pegColor);
        setClickListenerOn(button, pegColor);
    }


    private void setContentDescription(Context context, View view, PegColor pegColor){
        var description = context.getString(pegColor.getContentDescriptionId());
        view.setContentDescription(description);
    }


    private void setClickListenerOn(View view, PegColor pegColor){
        view.setOnClickListener((v -> {
            var game = gameView.getGame();
            if(game != null && isInitialized){
                game.addPeg(pegColor);
            }
        }));
    }


    private void setColorTint(Context context, View view, PegColor pegColor){
        if(context == null){
            return;
        }
        int color = context.getColor(pegColor.getColorId());
        var colorStateList = ColorStateList.valueOf(color);
        view.setBackgroundTintList(colorStateList);
    }

}
