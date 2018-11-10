package lib.navigation;

import lib.hlt.Direction;
import lib.hlt.GameMap;
import lib.hlt.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NavigationUtils {
    private static final List<Direction> cyclicalDirections = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH);

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

    public static int totalHaliteAtPositions(GameMap gameMap, List<Position> positions) {
        return positions.stream().mapToInt(p -> gameMap.at(p).halite).sum();
    }


    private static Direction leftPerpendicularDirection(Direction direction) {
        return cyclicalDirections.get(cyclicalDirections.lastIndexOf(direction) - 1);
    }

    private static Direction rightPerpendicularDirection(Direction direction) {
        return cyclicalDirections.get(cyclicalDirections.indexOf(direction) + 1);
    }

}
