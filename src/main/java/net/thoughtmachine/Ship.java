package net.thoughtmachine;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

/**
 * Represents a ship with its coordinates, direction and condition.
 */
public class Ship {
    private int x;
    private int y;
    private Direction direction;
    private boolean sunk = false;

    public Ship(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.sunk = false;
    }

    private Ship copy() {
        return new Ship(x,y,direction);
    }

    /**
     * Sinks the ship as a result of a shot.
     */
    public void sink() {
        this.sunk = true;
    }

    public void move(List<Operation.Move> moves, Board board, int lineNumber) {
        Ship copy = this.copy();
        moves.forEach(move -> copy.move(move, board, lineNumber));
        Optional<Ship> position = board.at(copy.getX(), copy.getY());
        position.ifPresent(ship -> { if( ship != this && !ship.isSunk()) {
                throw new IllegalOperationException(
                        String.format("Cannot move to a location occupied by another not-sunken ship at (%d,%d).",
                                copy.getX(), copy.getY()));
            }
        });
        // no ship there, then set the new location
        board.remove(this.getX(), this.getY());
        board.setShip(copy);
    }


    private void move(Operation.Move move, Board board, int lineNumber) {
        switch (move) {
            case M:
                this.forward();
                // In case it gets out of the board during the moves.
                board.checkCoordinates(this.x, this.y, lineNumber);
                break;
            case R:
                this.rotateRight();
                break;
            case L:
                this.rotateLeft();
                break;
        }
    }

    private void rotateLeft() {
        this.direction = this.direction.nextLeft();
    }

    private void rotateRight() {
        this.direction = this.direction.nextRight();
    }

    private void forward() {
        this.x += this.direction.getDeltaX();
        this.y += this.direction.getDeltaY();
    }

    public boolean isSunk() {
        return sunk;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void print(PrintWriter writer) {
        writer.println(String.format("(%d, %d, %s) %s", x, y, direction.name(), sunk ? "SUNK" : ""));
    }

    public Direction getDirection() {
        return direction;
    }
}
