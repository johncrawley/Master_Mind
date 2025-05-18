package com.jcrawley.mastermind.service.game;

import static com.jcrawley.mastermind.game.PegColor.*;


import static org.junit.Assert.*;

import com.jcrawley.mastermind.game.Game;
import com.jcrawley.mastermind.game.PegColor;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GameTest {


    private Game game;
    private MockView mockView;


    @Before
    public void init(){
        game = new Game();
        mockView = new MockView();
        game.setView(mockView);
        game.init();
        // add a manual solution so that tests won't hit a random solution by accident
        game.setSolution(PURPLE, PURPLE, PURPLE, PURPLE);
    }

    @Test
    public void canFillARowWithCorrectColors(){
        var expectedPegs = new PegColor[] { BLUE, BROWN, RED, YELLOW };

        Arrays.stream(expectedPegs)
                .forEach(pegColor -> game.addPeg(pegColor));
        assertPegsPlaced(0, expectedPegs);
    }


    @Test
    public void currentIndexAdvances(){
        assertIndex(0);
        game.addPeg(PegColor.BLUE);
        assertIndex(1);
        game.addPeg(BROWN);
        assertIndex(2);
    }


    @Test
    public void currentRowAdvances(){
        assertEquals(0, game.getCurrentRow());
        for(int i = 0; i < game.getPegsPerRow(); i++){
            game.addPeg(PegColor.BLUE);
        }
        assertIndex(0);
        assertRow(1);
    }


    @Test
    public void canRemovePegFromCurrentRow(){
        assertIndex(0);
        game.addPeg(RED);
        assertIndex(1);
        game.addPeg(BLUE);
        assertIndex(2);

        assertViewPegColorChange(2);
        assertIndex(2);
        game.removePeg();
        assertPegRemoved(3, 1, 1);

        game.removePeg();
        assertPegRemoved(4, 0, 0);
    }


    @Test
    public void cannotRemovePegFromPreviousRow(){
        assertIndex(0);
        game.addPeg(RED);
        game.addPeg(BLUE);
        game.addPeg(RED);
        game.addPeg(BLUE);
        assertRow(1);
        assertIndex(0);
        game.removePeg();
        assertRow(1);
        assertIndex(0);
    }


    @Test
    public void correctPegsArePlacedAfterRollback(){
        assertIndex(0);
        game.addPeg(RED);
        game.addPeg(RED);
        game.addPeg(RED);
        assertPegsPlaced(0, RED, RED, RED ,EMPTY);
        game.removePeg();
        game.removePeg();
        game.removePeg();
        assertPegsPlaced(0, EMPTY, EMPTY, EMPTY,EMPTY);
        game.addPeg(YELLOW);
        game.addPeg(GREEN);
        game.addPeg(PINK);
        assertPegsPlaced(0, YELLOW, GREEN, PINK, EMPTY);
        game.removePeg();
        assertPegsPlaced(0, YELLOW, GREEN, EMPTY, EMPTY);
        game.addPeg(BLUE);
        assertPegsPlaced(0, YELLOW, GREEN, BLUE, EMPTY);
    }

    @Test
    public void undoButtonInViewIsDisabledOnNewRow(){
        assertTrue(mockView.isUndoDisabled());
        game.addPeg(BLUE);
        assertFalse(mockView.isUndoDisabled());
        game.addPeg(GREEN);
        game.addPeg(GREEN);
        game.addPeg(GREEN);
        assertRow(1);
        assertIndex(0);
        assertTrue(mockView.isUndoDisabled());
    }


    @Test
    public void undoButtonStateIsCopiedWhenViewIsAssigned(){
        game.addPeg(BLUE);
        game.addPeg(GREEN);
        game.addPeg(PINK);
        game.addPeg(RED);
        game.addPeg(YELLOW);
        game.addPeg(BROWN);

        assertRow(1);
        assertIndex(2);
        mockView = new MockView();
        game.setView(mockView);
        game.init();
        assertUndoButtonState(true);
    }


    @Test
    public void correctUndoButtonStateAfterPegRemoved(){
        game.addPeg(BLUE);
        game.addPeg(GREEN);
        assertIndex(2);
        assertUndoButtonState(true);
        game.removePeg();
        assertUndoButtonState(true);
        game.removePeg();
        assertIndex(0);
        assertUndoButtonState(false);
    }


    private void assertUndoButtonState(boolean isEnabled){
        assertEquals(isEnabled, mockView.isUndoEnabled());
    }


    private void assertPegsPlaced(int rowIndex, PegColor... expected){
        var pegsPlaced = game.getPegsAtRow(rowIndex);
        assertEquals(expected.length, pegsPlaced.size());

        for(int i = 0; i < expected.length; i++){
            assertEquals(expected[i], pegsPlaced.get(i));
        }
    }


    private void assertPegRemoved(int expectedCallsToView, int expectedIndex, int expectedPegsPlaced){
        assertIndex(expectedIndex);
        assertViewPegColorChange(expectedCallsToView);
        assertEquals(expectedPegsPlaced, game.getPegCount());
        assertEquals(EMPTY, game.getPegColorAt(game.getCurrentIndex()));
    }


    private void assertViewPegColorChange(int expected){
        assertEquals(expected, mockView.getCallsToSetPegColor());
    }


    private void assertIndex(int expected){
        assertEquals(expected, game.getCurrentIndex());
    }


    private void assertRow(int expected){
        assertEquals(expected, game.getCurrentRow());
    }
}
