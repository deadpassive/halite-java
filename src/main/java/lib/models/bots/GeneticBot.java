package lib.models.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.hlt.*;
import lib.models.ships.DirectionScore;
import lib.models.ships.ShipGenes;
import lib.models.ships.ShipMode;
import lib.models.ships.GeneticShip;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticBot extends AbstractBot<GeneticShip> {

    private final static String OPTIMAL_GENE_FILE_PATH = "optimal-ship-genes.json";

    private ShipGenes shipGenes;

    private static GeneticBot INSTANCE;

    public static void main(String[] args) {
        SimpleBot.getInstance().run(args);
    }

    private GeneticBot() {}

    public static GeneticBot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeneticBot();
        }
        return INSTANCE;
    }

    /**
     * Loads {@link ShipGenes} from json file.
     * @throws IOException
     */
    private void initialiseOptimalShipGenes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMAL_GENE_FILE_PATH);
        shipGenes = mapper.readValue(IOUtils.toString(genes, "utf8"), ShipGenes.class);
    }

    private void setShipModes() {
        for (GeneticShip ship : this.ships.values()) {
            if (ship.isFull()) {
                // If the ship is full set the ships mode to depositing
                ship.setShipMode(ShipMode.DEPOSITING);
            } else if (ship.getShipGenes().getCollectThreshold() < game.gameMap.at(ship.getPosition()).halite) {
                // If the ships current position has more halite than the ships collection threshold set the ships
                // mode to gathering
                ship.setShipMode(ShipMode.GATHERING);
            } else {
                // Otherwise set the ships mode to migrating
                ship.setShipMode(ShipMode.MIGRATING);
            }
        }
    }

    private void updateShipDirectionScores() {
        for (GeneticShip ship : ships.values()) {
            ship.updateDirectionScores();
        }
    }
    /**
     * Creates a command for each ship based on the ships {@link DirectionScore}s and positions that are already
     * occupied.
     * @return List of {@link Command}s
     */
    private ArrayList<Command> resolveShipDirections() {
        // TODO occupied positions should be a bot property?
        List<Position> occupiedPositions = new ArrayList<>();

        ArrayList<Command> commands = new ArrayList<>();
        for (GeneticShip ship : ships.values()) {
            List<DirectionScore> sortedDirectionScores = ship.getDirectionScores().stream()
                    // Sort the move scores by their score so that we can try the best move first
                    .sorted(Comparator.comparingDouble(DirectionScore::getScore).reversed())
                    .collect(Collectors.toList());
            Log.log(String.format("Sorted direction scores: %s", sortedDirectionScores));

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

    @Override
    protected void onTurnStart() {

    }

    @Override
    protected void onTurnEnd() {

    }

    @Override
    protected void onGameStart() {
        try {
            // Load genes from json file in resource folder
            initialiseOptimalShipGenes();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onGameEnd() {

    }

    @Override
    protected ArrayList<Command> getCommandQueue() {
        // For each ship check some conditions and set the mode that the ship should run in
        setShipModes();
        // For each ship create a list of direction - score pairs
        updateShipDirectionScores();
        // Convert the ship direction scores into the best possible set of commands
        ArrayList<Command> commands = resolveShipDirections();

        if (game.me.halite >= Constants.SHIP_COST &&
                // Check bot has enough halite
                ships.size() < 2 &&
                // Limit the number of ships
                // TODO dont check isOccupied, check against our list of occupied positions thats generated by resolveDirectionScores()
                !game.gameMap.at(game.me.shipyard).isOccupied())
                // Check that the shipyard is empty
        {
            commands.add(game.me.shipyard.spawn());
        }

        return commands;
    }

    @Override
    protected GeneticShip createShip(Ship initialStatus) {
        return new GeneticShip(initialStatus, shipGenes);
    }
}
