package com.jcrawley.mastermind;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {



    public static List<Clue> generateClues(List<PegColor> currentAnswer, List<PegColor> solution){
        log("generate clues current answer size: " + currentAnswer.size());
        var clues = new ArrayList<Clue>(solution.size());
        List<PegColor> solutionCopy = new ArrayList<>(solution);

        var answerRemainders = new ArrayList<PegColor>();
        var solutionRemainders = new ArrayList<PegColor>();

        for(int i = 0; i < solutionCopy.size(); i++){
            var answerColor = currentAnswer.get(i);
            var solutionColor = solution.get(i);
            if(answerColor == solutionColor){
                clues.add(Clue.BULL);
            }
            else{
                answerRemainders.add(answerColor);
                solutionRemainders.add(solutionColor);
            }
        }

        for(var color : solutionRemainders){
            if(answerRemainders.contains(color)){
                answerRemainders.remove(color);
                clues.add(Clue.COW);
            }
        }
        for(var ignored : answerRemainders){
            clues.add(Clue.WRONG);
        }
        log("generateClues() generated clues: " + clues.get(0) + " " + clues.get(1) + " " +  clues.get(2) + " " + clues.get(3));
        return clues;
    }

    private static void log(String msg){
        System.out.println("^^^ GameUtils: " + msg);
    }

}
