package com.jcrawley.mastermind.game;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {



    public static List<Clue> generateClues(List<PegColor> currentAnswer, List<PegColor> solution){
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
        return clues;
    }

}
