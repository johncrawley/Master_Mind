package com.jcrawley.mastermind;

import androidx.lifecycle.ViewModel;

import com.jcrawley.mastermind.game.GameModel;

public class MainViewModel extends ViewModel {

    public GameModel gameModel = new GameModel();
}