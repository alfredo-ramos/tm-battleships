package net.thoughtmachine;

/**
 * A generic exception while processing/parsing the game.
 */
public class GameException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GameException(String message, Exception e) {
        super(message, e);
    }
}
