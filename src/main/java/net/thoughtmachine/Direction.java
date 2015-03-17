package net.thoughtmachine;

/**
* Specifies the direction of a ship, used when moving forward. It would change after a rotation.
*/
public enum Direction {

    N(0,1), S(0,-1), E(1,0), W(-1,0);

    private final int deltaX;
    private final int deltaY;

    private Direction(int deltaX, int deltaY) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /** The change in the X coordinate when applying this direction to a move */
    public int getDeltaX() {
        return deltaX;
    }

    /** The change in the Y coordinate when applying this direction to a move */
    public int getDeltaY() {
        return deltaY;
    }

    public Direction nextRight() {
        switch (this) {
            case N: return E;
            case E: return S;
            case S: return W;
            case W: return N;
        }
        throw new IllegalStateException();
    }

    public Direction nextLeft() {
        switch (this) {
            case N: return W;
            case W: return S;
            case S: return E;
            case E: return N;
        }
        throw new IllegalStateException();
    }
}
