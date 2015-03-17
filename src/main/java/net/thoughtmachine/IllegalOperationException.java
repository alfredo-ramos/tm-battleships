package net.thoughtmachine;

/**
 * Thrown when the execution of an operation is not possible, as it would break the rules of the game.
 */
public class IllegalOperationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IllegalOperationException(String message) {
        super(message);
    }
}
