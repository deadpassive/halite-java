package lib.navigation;

import lib.hlt.Command;
import lib.hlt.Position;
import lib.models.ships.GeneticShip;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NavigationUtils {
    /**
     * Creates a command for each ship based on the ships {@link DirectionScore}s and positions that are already
     * occupied.
     * @return List of {@link Command}s
     */
    public static ArrayList<Command> resolveShipDirections(Collection<GeneticShip> ships) {
        // TODO occupied positions should be a bot property?
        List<Position> occupiedPositions = new ArrayList<>();
        ArrayList<Command> commands = new ArrayList<>();
        // Sort the ships so the ships with high scores get their first choice (errors otherwise to do with trying to move without halite)
        List<GeneticShip> sortedShips = ships
                .stream()
                .sorted(Comparator.comparing((GeneticShip s) -> s.getDirectionScores().stream().max(Comparator.comparingInt(DirectionScore::getScore).reversed()).map(DirectionScore::getScore).get()).reversed())
                .collect(Collectors.toList());


        for (GeneticShip ship : sortedShips) {
            List<DirectionScore> sortedDirectionScores = ship.getDirectionScores().stream()
                    // Sort the move scores by their score so that we can try the best move first
                    .sorted(Comparator.comparingDouble(DirectionScore::getScore).reversed())
                    .collect(Collectors.toList());

            for (DirectionScore directionScore : sortedDirectionScores) {
                if (!occupiedPositions.contains(ship.getPosition().directionalOffset(directionScore.getDirection()))) {
                    // If the ships position modified by the direction isnt already occupied
                    // then add the command to move the ship in that direction
                    commands.add(ship.move(directionScore.getDirection()));
                    // add the resulting position to the list of occupied positions
                    occupiedPositions.add(ship.getPosition().directionalOffset(directionScore.getDirection()));
                    break;
                }
            }
        }
        return commands;
    }
}
