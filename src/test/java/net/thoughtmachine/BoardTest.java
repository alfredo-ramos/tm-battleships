package net.thoughtmachine;


import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(4);
    }

    @Test
    public void testEmptyBoard() {
        Board board = new Board(4);
        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                assertThat( board.at(i,j), is(Optional.empty()));
            }
        }
    }

    @Test
    public void testSize() {
        assertThat(board.size(), is(4));
    }

    @Test
    public void testSetShip() {
        Ship ship = new Ship(1,2, Direction.E);
        board.setShip(ship);

        assertThat(board.at(1,2).isPresent(), is(true));
        assertThat(board.at(1,2).get(), is(ship));
    }

    @Test
    public void testRemove() {
        Ship ship = new Ship(1,1, Direction.N);
        board.setShip(ship);

        board.remove(1,1);
        assertThat(board.at(1,1).isPresent(), is(false));
    }



}