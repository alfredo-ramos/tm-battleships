package net.thoughtmachine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Application {

    private static final String DEFAULT_INPUT = "/input.txt";
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private static final Pattern LOCATION_PATTERN = Pattern.compile("\\(([0-9]+),\\s*([0-9]+),\\s*([NSEW])\\)");
    // limiting input to a practical size
    private static final int MAX_BOARD_SIZE = 1024;

    @Nonnull
    public Board loadInput(String input) throws IOException {
        LOG.debug("Using input resource: {}", input);
        InputStream is = getClass().getResourceAsStream(input);
        if(is == null) {
            throw new IllegalArgumentException(String.format("Input resource '%s' could not be found.", input));
        }
        int lineNumber = 0;
        try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            lineNumber = reader.getLineNumber();
            int boardSize = readSize(reader);

            Board board = new Board(boardSize);
            readLocations(reader, board);

            String line;
            while ((line = reader.readLine()) != null) {
                Operation operation = Operation.parse(line);
                operation.apply(board);
            }
            return board;
        } catch(IllegalOperationException | InvalidInputFileFormat e) {
            throw new GameException(String.format("Error while processing line %d.", lineNumber), e);
        }
    }

    // Keeping the old signature in case it was expected.
    public void loadInput() {
        try {
            loadInput(DEFAULT_INPUT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Reads the line that contains the ship positions and populates the board. */
    private void readLocations(LineNumberReader reader, Board board) throws IOException {
        String line = reader.readLine();
        if(line == null) {
            throw new InvalidInputFileFormat("Incomplete input file, missing second line with ship locations.");
        }
        Matcher matcher = LOCATION_PATTERN.matcher(line);
        int position = 0;
        while(matcher.find(position)) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            Direction direction = Direction.valueOf(matcher.group(3));

            // Not using board checkCoordinates(int,int) in order to give more details of the parsing state here.
            if(x >= board.size()) {
                throw new InvalidInputFileFormat(String.format("Invalid X coordinate %d at line %d and position %d.",
                        x, reader.getLineNumber(), matcher.start(1))
                );
            }
            if(y >= board.size()) {
                throw new InvalidInputFileFormat(String.format("Invalid Y coordinate %d at line %d and position %d.",
                        y, reader.getLineNumber(), matcher.start(2))
                );
            }
            board.setShip(new Ship(x,y,direction));
            position = matcher.end();
        }
    }

    /** Reads the board size */
    int readSize(LineNumberReader reader) throws IOException {
        String line = reader.readLine();
        if(line == null) {
            throw new InvalidInputFileFormat("Invalid empty input file.");
        }
        line = line.trim();
        try {
            int size = Integer.parseInt(line);
            if(size <= 0 || size > MAX_BOARD_SIZE) {
                throw new InvalidInputFileFormat("Invalid board size " + size + " at line " + reader.getLineNumber());
            }
            return size;
        } catch (NumberFormatException e) {
            throw new InvalidInputFileFormat("Invalid board size " + line + " at line " + reader.getLineNumber());
        }
    }

    public static void main(String... args) throws IOException {
        String inputResource = DEFAULT_INPUT;
        if (args.length > 0) {
            inputResource = args[0];
        }
        Application app = new Application();
        Board board;
        try {
            board = app.loadInput(inputResource);
        } catch (IOException e) {
            LOG.error("Could not read the input file '{}'.", inputResource);
            throw e;
        }

        if(args.length > 1) {
            try(PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(args[1]), StandardCharsets.UTF_8)))) {
                board.printState(writer);
            }
        } else {
            PrintWriter writer = new PrintWriter(System.out);
            board.printState(writer);
            writer.flush();
        }
    }
}
