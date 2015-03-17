package net.thoughtmachine;

import java.io.Serializable;

/**
 * Exception thrown when the input file does not conform the input file specifications.
 */
public class InvalidInputFileFormat extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public InvalidInputFileFormat(String message) {
        super(message);
    }
}
