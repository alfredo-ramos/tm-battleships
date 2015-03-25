package net.thoughtmachine;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static net.thoughtmachine.Operation.Move.L;
import static net.thoughtmachine.Operation.Move.M;
import static net.thoughtmachine.Operation.Move.R;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class OperationTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board(4);
    }

    @Test
    public void testParseShot() {
        Operation operation = Operation.parse("(4,5)");

        assertThat(operation, is(instanceOf(Operation.ShootOperation.class)));
        Operation.ShootOperation shoot = (Operation.ShootOperation) operation;
        assertThat(shoot.x, is(4));
        assertThat(shoot.y, is(5));
    }

    @Test
    public void testParseShotWithSpaces() {
        Operation operation = Operation.parse(" ( 4 ,  5 ) ");

        assertThat(operation, is(instanceOf(Operation.ShootOperation.class)));
        Operation.ShootOperation shoot = (Operation.ShootOperation) operation;
        assertThat(shoot.x, is(4));
        assertThat(shoot.y, is(5));
    }

    @Test
    public void testParseMove() {
        Operation operation = Operation.parse("(2,1) RMMLM");

        assertThat(operation, is(instanceOf(Operation.MoveOperation.class)));
        Operation.MoveOperation move = (Operation.MoveOperation)operation;
        assertThat(move.x, is(2));
        assertThat(move.y, is(1));
        assertThat(move.moves.size(), is(5));
        assertThat(move.moves.get(0), is(Operation.Move.R));
        assertThat(move.moves.get(1), is(Operation.Move.M));
        assertThat(move.moves.get(2), is(Operation.Move.M));
        assertThat(move.moves.get(3), is(Operation.Move.L));
        assertThat(move.moves.get(4), is(Operation.Move.M));
    }

    @Test(expected = IllegalOperationException.class)
    public void testMoveOutsideBoard() {
        board.setShip(new Ship(2,1,Direction.N));
        Operation.MoveOperation move = new Operation.MoveOperation(2,1, moves(M, L, M, M, M, R, M, R, M));
        move.apply(board);
    }

    @Test(expected = IllegalOperationException.class)
    public void moveToAnotherShip() {
        board.setShip(new Ship(2,3,Direction.W));
        board.setShip(new Ship(2,0, Direction.N));

        Operation.MoveOperation move = new Operation.MoveOperation(2,0, moves(M,M,M));
        move.apply(board);
    }

    @Test
    public void movePassAnotherShip() {
        board.setShip(new Ship(2,2,Direction.W));
        board.setShip(new Ship(2,0, Direction.N));

        Operation.MoveOperation move = new Operation.MoveOperation(2,0, moves(M,M,M));
        move.apply(board);

        assertThat(board.at(2, 0).isPresent(), is(false));
        assertThat(board.at(2, 3).isPresent(), is(true));
    }

    @Test
    public void moveToASunkShip() {
        board.setShip(new Ship(0,2,Direction.W).sink());

        board.setShip(new Ship(2,0, Direction.W));

        Operation.MoveOperation move = new Operation.MoveOperation(2,0, moves(M,M,R,M,M));
        move.apply(board);

        assertThat(board.at(2, 0).isPresent(), is(false));
        assertThat(board.at(0, 2).isPresent(), is(true));
        assertThat(board.at(0, 2).get().isSunk(), is(false)); // not sunk
    }

    static List<Operation.Move> moves(Operation.Move...move) {
        return Arrays.asList(move);
    }


}