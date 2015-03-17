package net.thoughtmachine;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ApplicationTest {

    @Test
    public void testHappyBoard() throws Exception {
        Application application = new Application();

        Board board = application.loadInput("/test-input.txt");
        // first ship must be moved and sunk
        assertThat(board.at(1,3).isPresent(), is(false));
        assertThat(board.at(0,5).isPresent(), is(true));
        assertThat(board.at(0,5).get().isSunk(), is(true));

        // second ship just moved
        assertThat(board.at(6,2).isPresent(), is(false));
        assertThat(board.at(4,3).isPresent(), is(true));
        assertThat(board.at(4,3).get().isSunk(), is(false));

        // third moved and was shot
        assertThat(board.at(5,8).isPresent(), is(false));
        assertThat(board.at(6,7).isPresent(), is(true));
        assertThat(board.at(6,7).get().isSunk(), is(true));

        // fourth one only rotated
        assertThat(board.at(3,9).isPresent(), is(true));
        assertThat(board.at(3,9).get().getDirection(), is(Direction.S));
    }




}
