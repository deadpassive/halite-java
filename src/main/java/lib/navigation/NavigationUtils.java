package lib.navigation;

import lib.hlt.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationUtils {
    private static final List<Direction> cyclicalDirections = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);

    /**
     * For a given position calculate a ray from that position in the specific direction.
     * ship:        >
     * positions:   #
     *
     *       #
     *     # #
     *   # # #
     * > # # #
     *   # # #
     *     # #
     *       #
     *
     * @param shipPosition start position of the ray
     * @param rayDirection direction of the ray
     * @param rayLength length of the ray
     * @return {@link Position}s within the ray
     */
    public static List<Position> getMapCellsInRay(Position shipPosition, Direction rayDirection, int rayLength) {
        // TODO normalize these positions?
        List<Position> positions = new ArrayList<>();

        Position position = shipPosition.directionalOffset(rayDirection);

        for (int i = 1; i < rayLength; i++){
            positions.add(position);

            Position leftPosition = position;
            Position rightPosition = position;

            for (int ii = 0; ii < i; ii++) {
                leftPosition = leftPosition.directionalOffset(leftPerpendicularDirection(rayDirection));
                positions.add(leftPosition);

                rightPosition = rightPosition.directionalOffset(rightPerpendicularDirection(rayDirection));
                positions.add(rightPosition);
            }
            position = position.directionalOffset(rayDirection);
        }
        return positions;
    }

    public static Position closestDepository(Position position, Game game) {
        List<Position> positions = game.me.dropoffs.values().stream().map(d -> d.position).collect(Collectors.toList());
        positions.add(game.me.shipyard.position);
        return closestPosition(position, positions, game.gameMap);
    }

    public static Position closestPosition(Position position, List<Position> positions, GameMap gameMap) {
        return positions.stream().sorted(Comparator.comparing(p -> gameMap.calculateDistance(p, position))).findFirst().get();
    }

    /**
     * Returns the total amount of halite in a list of positions
     * @param gameMap {@link GameMap} to extract halite amounts from
     * @param positions {@link Position} positions to check halite for
     * @return amount of halite at the positions
     */
    public static int totalHaliteAtPositions(GameMap gameMap, List<Position> positions) {
        return positions.stream().mapToInt(p -> gameMap.at(p).halite).sum();
    }

    /**
     * Returns the sum of the (halite at a position divided by the distance of that position from the start position.
     * @param gameMap {@link GameMap} to extract halite amounts from
     * @param positions {@link Position}s to check halite for at
     * @param startPosition {@link Position} to calculate the distance to other positions
     * @return sum of halite modified by distance from start position
     */
    public static int totalHaliteAtPositions(GameMap gameMap, List<Position> positions, Position startPosition) {
        return positions.stream().mapToInt(p -> gameMap.at(p).halite / gameMap.calculateDistance(p, startPosition)).sum();
    }

    private static Direction leftPerpendicularDirection(Direction direction) {
        return cyclicalDirections.get(cyclicalDirections.lastIndexOf(direction) - 1);
    }

    private static Direction rightPerpendicularDirection(Direction direction) {
        return cyclicalDirections.get(cyclicalDirections.indexOf(direction) + 1);
    }

}
