package net.thoughtmachine;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The battleship operation to execute. Two operations are possible: A sequence of moves and a shot.
 */
public abstract class Operation {
    private static final Pattern MOVE_PATTERN = Pattern.compile("\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)\\s*([MRL]+)");
    private static final Pattern SHOT_PATTERN = Pattern.compile("\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");

    enum Move { M, R, L }

    final int x;
    final int y;

    /**
     * Parses an input line that represents an operation and returns the corresponding instance.
     *
     * @param line The line to parse
     * @return The ship operation to apply to the board.
     * @throws InvalidInputFileFormat if the line cannot be parsed as a valid operation.
     */
    @Nonnull
    public static Operation parse(String line) {
        line = line.trim();
        Matcher moveMatcher = MOVE_PATTERN.matcher(line);
        Matcher shotMatcher = SHOT_PATTERN.matcher(line);
        if(moveMatcher.matches()) {
            int x = Integer.parseInt(moveMatcher.group(1));
            int y = Integer.parseInt(moveMatcher.group(2));
            String moveSequence = moveMatcher.group(3);
            List<Move> moves = IntStream.range(0, moveSequence.length()).mapToObj(moveSequence::charAt)
                    .map(String::valueOf).map(Move::valueOf).collect(Collectors.toList());
            return new MoveOperation(x,y,moves);
        } else if(shotMatcher.matches()) {
            int x = Integer.parseInt(shotMatcher.group(1));
            int y = Integer.parseInt(shotMatcher.group(2));
            return new ShootOperation(x,y);
        }
        throw new InvalidInputFileFormat(String.format("Invalid operation '%s'", line));
    }

    protected Operation(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Performs the operation on the board.
     * @param board The board where to perform the operation that contains the current state of the game.
     * @throws IllegalOperationException if the attempted operation breaks the rules of the game.
     */
    public abstract void apply(Board board);

    /**
     * Performs a move operation, which is a sequence of moves for a ship at the given coordinates.
     */
    static class MoveOperation extends Operation {
        final List<Move> moves;

        public MoveOperation(int x, int y, @Nonnull List<Move> moves) {
            super(x, y);
            this.moves = moves;
        }

        @Override
        public void apply(Board board) {
            board.checkCoordinates(x, y);
            Optional<Ship> position = board.at(x,y);
            if(!position.isPresent()) {
                throw new IllegalOperationException(
                        String.format("There is no ship to move at (%d,%d).", x, y));
            }
            Ship ship = position.get();
            if(ship.isSunk()) {
                throw new IllegalOperationException(
                        String.format("Cannot move a sunk ship (%d,%d).", x, y));
            }
            Ship copy = ship.copy();
            moves.forEach(move -> copy.move(move, board));
            Optional<Ship> finalPosition = board.at(copy.getX(), copy.getY());
            finalPosition.ifPresent(otherShip -> { if( otherShip != ship && !otherShip.isSunk()) {
                throw new IllegalOperationException(
                        String.format("Cannot move to a location occupied by another not-sunken ship at (%d,%d).",
                                copy.getX(), copy.getY()));
                }
            });
            // no ship there, then set the new location
            board.remove(ship.getX(), ship.getY());
            board.setShip(copy);
        }


    }

    /**
     * Performs a attempt to shot a ship at the given coordinates, the operation is ignpred (no state change) if there
     * is no ship at the given coordinates.
     */
    public static class ShootOperation extends Operation {
        public ShootOperation(int x, int y) {
            super(x, y);
        }

        @Override
        public void apply(Board board) {
            board.checkCoordinates(x, y);
            board.at(x,y).ifPresent(Ship::sink);
        }
    }
}
