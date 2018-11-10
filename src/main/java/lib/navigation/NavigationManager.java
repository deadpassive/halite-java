package lib.navigation;

import lib.hlt.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationManager {

    private List<Position> occupiedPositions = new ArrayList<>();

    public NavigationManager() {

    }

    /**
     * Creates a command for each ship based on the ships {@link DirectionScore}s and positions that are already
     * occupied.
     * @return List of {@link Command}s
     */
    public ArrayList<Command> resolveShipDirections(Collection<? extends ShipNavigationInterface> ships, GameMap gameMap) {
        ArrayList<Command> commands = new ArrayList<>();
        // Sort the ships so the ships with high scores get their first choice (errors otherwise to do with trying to move without halite)
        List<ShipNavigationInterface> sortedShips = ships
                .stream()
                .sorted(Comparator.comparing((ShipNavigationInterface s) -> s.getDirectionScores().stream().max(Comparator.comparingInt(DirectionScore::getScore)).map(DirectionScore::getScore).get()).reversed())
                .collect(Collectors.toList());
        Log.log(String.format("sorted ships: %s", sortedShips));

        for (ShipNavigationInterface ship : sortedShips) {
            List<DirectionScore> sortedDirectionScores = ship.getDirectionScores().stream()
                    // Sort the move scores by their score so that we can try the best move first
                    .sorted(Comparator.comparingDouble(DirectionScore::getScore).reversed())
                    .collect(Collectors.toList());

            for (DirectionScore directionScore : sortedDirectionScores) {
                Position targetPosition = gameMap.normalize(ship.getPosition().directionalOffset(directionScore.getDirection()));

                if (!occupiedPositions.contains(targetPosition)) {
                    // If the ships position modified by the direction isnt already occupied
                    // then add the command to move the ship in that direction
                    commands.add(ship.move(directionScore.getDirection()));
                    // add the resulting position to the list of occupied positions
                    markOccupied(targetPosition);
                    // break out of this ships loop over the sorted direction scores
                    break;
                }
            }
        }
        return commands;
    }

    public List<Position> getOccupiedPositions() {
        return occupiedPositions;
    }

    public void onTurnStart(Game game) {
        // For now just set the occupied positions to empty array, in the future this might want to be enemy positions
        Log.log(String.format("Clearing occupied positions: %s", occupiedPositions));
        occupiedPositions.clear();
    }

    public void markOccupied(Position occupiedPosition) {
        occupiedPositions.add(occupiedPosition);
    }
}
