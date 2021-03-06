# The Battleships
=================
The net.thoughtmachine.Application is the starting point. It takes two optional parameters, the input resource name and
the output file. If none of them are given, the default input is /input.txt which will load the file from the classpath.
The default output is the standard output (console).

The grid for the board and coordinates has its origin in the bottom left (first quadrant) so that X grows towards E and
Y grows towards N, and no negative coordinates are used.

If the input file cannot be parsed or the information is incomplete, the exception InvalidInputFileFormat is thrown.
If the operations to perform on the board are invalid, then IllegalOperationException is thrown. There are three main
reasons for this:

- A move sequence ends up in a location currently occupied by an ship that is not sunk.
- A move sequence gets out of the playing board, not necessarily at the end.
- A move sequence indicates a location where there is no ship, or there is a ship, it is sunk.

The final state is printed at the end of the execution of operations as requested.

There are two dependencies added, but it can do without them. They are:
- sl4fj - Simple logging framework for java
- logback - the actual logging library
- jsr305 - Only used to add annotations that help with the IDE.

They are all included in the build.gradle file.


