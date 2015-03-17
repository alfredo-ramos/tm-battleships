package net.thoughtmachine;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

public class OperationTest {

    @Test
    public void testParseShot() {
        Operation operation = Operation.parse("(4,5)", 6);

        assertThat(operation, is(instanceOf(Operation.ShootOperation.class)));
        Operation.ShootOperation shoot = (Operation.ShootOperation) operation;
        assertThat(shoot.x, is(4));
        assertThat(shoot.y, is(5));
        assertThat(shoot.lineNumber, is(6));
    }

    @Test
    public void testParseShotWithSpaces() {
        Operation operation = Operation.parse(" ( 4 ,  5 ) ", 6);

        assertThat(operation, is(instanceOf(Operation.ShootOperation.class)));
        Operation.ShootOperation shoot = (Operation.ShootOperation) operation;
        assertThat(shoot.x, is(4));
        assertThat(shoot.y, is(5));
        assertThat(shoot.lineNumber, is(6));
    }

    @Test
    public void testParseMove() {
        Operation operation = Operation.parse("(2,1) RMMLM", 7);

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

}