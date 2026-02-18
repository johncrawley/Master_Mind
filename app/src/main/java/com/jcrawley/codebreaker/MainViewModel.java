package com.jcrawley.codebreaker;

import androidx.lifecycle.ViewModel;

import com.jcrawley.codebreaker.game.GameModel;

public class MainViewModel extends ViewModel {

    public GameModel gameModel = new GameModel();
}