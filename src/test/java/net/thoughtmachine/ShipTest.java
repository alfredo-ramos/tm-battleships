package net.thoughtmachine;

import net.thoughtmachine.Operation.Move;
import static net.thoughtmachine.Operation.Move.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ShipTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(4);
    }

    @Test(expected = IllegalOperationException.class)
    public void testMoveOutsideBoard() {
        Ship ship = new Ship(2,1,Direction.N);
        ship.move(moves(M, L, M, M, M, R, M, R, M), board, 1);
    }

    @Test(expected = IllegalOperationException.class)
    public void moveToAnotherShip() {
        Ship ship1 = new Ship(2,3,Direction.W);
        board.setShip(ship1);

        Ship ship2 = new Ship(2,0, Direction.N);
        ship2.move(moves(M,M,M), board, 2);
    }

    @Test
    public void movePassAnotherShip() {
        Ship ship1 = new Ship(2,2,Direction.W);
        board.setShip(ship1);

        Ship ship2 = new Ship(2,0, Direction.N);
        ship2.move(moves(M,M,M), board, 2);
        assertThat(board.at(2,0).isPresent(), is(false));
        assertThat(board.at(2,3).isPresent(), is(true));
    }

    @Test
    public void moveToASunkShip() {
        Ship ship1 = new Ship(0,2,Direction.W);
        board.setShip(ship1);
        ship1.sink();

        Ship ship2 = new Ship(2,0, Direction.W);
        ship2.move(moves(M,M,R,M,M), board, 2);
        assertThat(board.at(2,0).isPresent(), is(false));
        assertThat(board.at(0,2).isPresent(), is(true));
        assertThat(board.at(0,2).get().isSunk(), is(false)); // not sunk
    }

    static List<Move> moves(Move ...move) {
        return Arrays.asList(move);
    }

}