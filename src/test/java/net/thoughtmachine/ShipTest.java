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


}