package com.jcrawley.mastermind;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final int pegsPerRow = 4;
    private int currentIndex, currentRow, pegsPlaced;
    private final int numberOfRows = 10;
    private final int MAX_PEGS = pegsPerRow * numberOfRows;
    private final List<PegColor> currentAnswer = new ArrayList<>();
    private int numberOfTries;
    private Random random;
    PegColor[] possibleColors = PegColor.values();
    List<PegColor> solution = new ArrayList<>(pegsPerRow);


}
