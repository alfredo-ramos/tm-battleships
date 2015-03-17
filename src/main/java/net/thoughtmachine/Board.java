package net.thoughtmachine;

import java.io.PrintWriter;
import java.util.Optional;

/**
 * Encapsulates the board and related methods.
 */
public class Board {

    private final Optional<Ship>[][] board;

    public Board(int size) {
        //noinspection unchecked
        board = (Optional<Ship>[][])new Optional[size][size];
        // Initializes to empty all coordinates of the board.
        for(int x = 0; x<board.length; x++) {
            for(int y = 0; y<board.length; y++) {
                board[x][y] = Optional.empty();
            }
        }
    }

    public int size() {
        return board.length;
    }

    public void setShip(Ship ship) {
        board[ship.getX()][ship.getY()] = Optional.of(ship);
    }

    public void checkCoordinates(int x, int y, int lineNumber) {
        if(x >= board.length || x < 0) {
            throw new IllegalOperationException(
                    String.format("Invalid X coordinate %d for operation at line %d", x, lineNumber));
        }
        if(y >= board.length || y < 0) {
            throw new IllegalOperationException(
                    String.format("Invalid Y coordinate %d for operation at line %d", y, lineNumber));
        }
    }


    public Optional<Ship> at(int x, int y) {
        return board[x][y];
    }


    public void remove(int x, int y) {
        board[x][y] = Optional.empty();
    }

    public void printState(PrintWriter writer) {
        for (Optional<Ship>[] row : board) {
            for (int y = 0; y < board.length; y++) {
                row[y].ifPresent(ship -> ship.print(writer));
            }
        }
    }

}
