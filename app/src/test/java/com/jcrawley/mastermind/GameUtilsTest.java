package com.jcrawley.mastermind;

import static com.jcrawley.mastermind.Clue.*;
import static com.jcrawley.mastermind.PegColor.*;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.List;

public class GameUtilsTest {


    @Test
    public void correctCluesAreGenerated(){

        var solution = List.of(BLUE, RED, YELLOW, PINK);

        assertClueGeneration(solution,
                List.of(GREEN, GREEN, GREEN, GREEN),
                WRONG, WRONG, WRONG, WRONG);

        assertClueGeneration(solution,
                List.of(GREEN, RED, GREEN, GREEN),
                BULL, WRONG, WRONG, WRONG);

        assertClueGeneration(solution,
                List.of(GREEN, BROWN, GREEN, RED),
                COW, WRONG, WRONG, WRONG);


        assertClueGeneration(solution,
                List.of(GREEN, GREEN, BLUE, RED),
                COW, COW, WRONG, WRONG);

        assertClueGeneration(solution,
                List.of(PINK, YELLOW, BLUE, RED),
                COW, COW, COW, COW);

        assertClueGeneration(solution,
                List.of(PINK, YELLOW, BLUE, RED),
                COW, COW, COW, COW);

        assertClueGeneration(solution,
                List.of(BLUE, GREEN, GREEN, RED),
                BULL, COW, WRONG, WRONG);


        assertClueGeneration(solution,
                List.of(BLUE, RED, GREEN, GREEN),
                BULL, BULL, WRONG, WRONG);


        assertClueGeneration(solution,
                List.of(BLUE, RED, GREEN, YELLOW),
                BULL, BULL, COW, WRONG);

        assertClueGeneration(solution,
                List.of(BLUE, RED, YELLOW, PINK),
                BULL, BULL, BULL, BULL);


    }

    private void assertClueGeneration(List<PegColor> solution, List<PegColor> answer, Clue ...expectedClues){
        var clues = GameUtils.generateClues(answer, solution);
        assertClues(clues, expectedClues);
    }

    private void assertClues(List<Clue> clues, Clue ... expected){
        for(int i = 0; i < clues.size(); i++){
            assertSame(clues.get(i), expected[i]);
        }

    }
}
