package com.jcrawley.mastermind.service.game;

import static com.jcrawley.mastermind.game.PegColor.*;


import static org.junit.Assert.*;

import com.jcrawley.mastermind.game.Game;
import com.jcrawley.mastermind.game.PegColor;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class GameTest {


    private Game game;
    private MockView mockView;


    @Before
    public void init(){
        game = new Game();
        mockView = new MockView();
        game.setView(mockView);
        game.init();
    }

    @Test
    public void canFillARowWithCorrectColors(){
        var expectedPegs = List.of(BLUE, BROWN, RED, YELLOW);
        expectedPegs.forEach(pegColor -> game.addPeg(pegColor));
        var pegsPlaced = game.getPegsAtRow(0);
        assertEquals(expectedPegs.size(), pegsPlaced.size());

        for(int i = 0; i < expectedPegs.size(); i++){
            assertEquals(expectedPegs.get(i), pegsPlaced.get(i));
        }
    }


    @Test
    public void currentIndexAdvances(){
        assertEquals(0, game.getCurrentIndex());
        game.addPeg(PegColor.BLUE);
        assertEquals(1, game.getCurrentIndex());
        game.addPeg(BROWN);
        assertEquals(2, game.getCurrentIndex());
    }


    @Test
    public void currentRowAdvances(){
        assertEquals(0, game.getCurrentRow());
        for(int i = 0; i < game.getPegsPerRow(); i++){
            game.addPeg(PegColor.BLUE);
        }
        assertEquals(0, game.getCurrentIndex());
        assertEquals(1, game.getCurrentRow());
    }


    @Test
    public void canRollBackChoice(){
        assertIndex(0);
        game.addPeg(RED);
        assertIndex(1);
        game.addPeg(BLUE);
        assertIndex(2);

        assertViewPegColorChange(2);
        assertIndex(2);
        game.removePeg();
        assertPegRemoved(3, 1);

        game.removePeg();
        assertPegRemoved(4, 0);

        game.removePeg();
        assertIndex(0);
    }


    private void assertPegRemoved(int expectedCallsToView, int expectedIndex){
        assertIndex(expectedIndex);
        assertViewPegColorChange(expectedCallsToView);
        assertEquals(EMPTY, game.getPegColorAt(game.getCurrentIndex()));
    }


    private void assertViewPegColorChange(int expected){
        assertEquals(expected, mockView.getCallsToSetPegColor());
    }


    private void assertIndex(int expected){
        assertEquals(expected, game.getCurrentIndex());
    }
}
