package com.jcrawley.mastermind.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameSolution {

    private Random random;
    private final List<PegColor> solution;
    private final int pegsPerRow;
    private List<PegColor> possibleColors;


    public GameSolution(int pegsPerRow){
        this.pegsPerRow = pegsPerRow;
        solution = new ArrayList<>(pegsPerRow);
        setupPossibleColors();
    }


    private void setupPossibleColors(){
        possibleColors = Arrays.stream(PegColor.values())
                .filter( pc -> pc != PegColor.EMPTY)
                .collect(Collectors.toList());
    }


    public List<PegColor> get(){
        return new ArrayList<>(solution);
    }


    public List<PegColor> generate(){
        solution.clear();
        random = new Random(System.currentTimeMillis());
        for(int i = 0; i < pegsPerRow; i++ ){
            solution.add(getRandomPegColor());
        }
        return get();
    }


    private PegColor getRandomPegColor(){
        int randomIndex = random.nextInt(possibleColors.size());
        return possibleColors.get(randomIndex);
    }
}
