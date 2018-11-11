package lib.models.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.hlt.*;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import lib.models.ships.*;
import lib.navigation.DirectionScore;
import lib.navigation.NavigationManager;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GeneticBot extends AbstractBot<GeneticShip> {

    private final static String OPTIMAL_SHIP_GENEs_FILE_PATH = "optimal-ship-genes.json";
    private final static String OPTIMAL_BOT_GENEs_FILE_PATH = "optimal-bot-genes.json";

    private ShipGenes shipGenes;
    private BotGenes botGenes;

    private NavigationManager navigationManager = new NavigationManager();

    private int initialHalite;
    private int remainingHalite;

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
     * @throws IOException if there is a problem loading the ship genes
     */
    private void initialiseOptimalShipGenes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMAL_SHIP_GENEs_FILE_PATH);
        shipGenes = mapper.readValue(IOUtils.toString(genes, "utf8"), ShipGenes.class);
    }

    /**
     * Loads {@link ShipGenes} from json file.
     * @throws IOException if there is a problem loading the ship genes
     */
    private void initialiseOptimalBotGenes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMAL_BOT_GENEs_FILE_PATH);
        botGenes = mapper.readValue(IOUtils.toString(genes, "utf8"), BotGenes.class);
    }

    /**
     * Update the {@link ShipMode}s
     */
    private void setShipModes() {
        for (GeneticShip ship : this.ships.values()) {
            // If the ship doesn't have enough halite to get off the cell without dying
            if (cantAffordToMove(ship)) {
                // then set the ships mode to forced gathering
                ship.setShipMode(ShipMode.FORCED_GATHERING);
                continue;
            }

            // If the ship isn't full yet
            if (!ship.isFull()
                    // and the amount of halite at the ships position meets the threshold gene
                    && (ship.getShipGenes().getGatherPositionHaliteAmount() * ((double)(Constants.MAX_TURNS - game.turnNumber) / Constants.MAX_TURNS)) < game.gameMap.at(ship.getPosition()).halite) {
                // then set the ships mode to gathering
                ship.setShipMode(ShipMode.GATHERING);
                continue;
            }

            // If the ship has less than the return threshold
            if (ship.getHalite() > ship.getShipGenes().getReturnHaliteAmount()) {
                // then set the ships mode to returning
                ship.setShipMode(ShipMode.RETURNING);
            } else {
                // otherwise set the ships mode to migrating
                ship.setShipMode(ShipMode.MIGRATING);
            }
        }
        Log.log(String.format("%03d ships GATHERING", ships.values().stream().filter(s -> s.getShipMode() == ShipMode.GATHERING).collect(Collectors.toList()).size()));
        Log.log(String.format("%03d ships RETURNING", ships.values().stream().filter(s -> s.getShipMode() == ShipMode.RETURNING).collect(Collectors.toList()).size()));
        Log.log(String.format("%03d ships MIGRATING", ships.values().stream().filter(s -> s.getShipMode() == ShipMode.MIGRATING).collect(Collectors.toList()).size()));

    }

    /**
     * Check that the ship has enough halite to move off the current cell that its on.
     * @param ship The {@link AbstractShip} to check for
     * @return true if the ship cant move, false if it can
     */
    private boolean cantAffordToMove(AbstractShip ship) {
        return ship.getHalite() < ((double) game.gameMap.at(ship.getPosition()).halite * 0.1);
    }

    /**
     * Refresh the list of {@link DirectionScore} on each ship considering new game state and new {@link ShipMode}s
     */
    private void updateShipDirectionScores() {
        for (GeneticShip ship : ships.values()) {
            ship.updateDirectionScores(game);
        }
    }

    private int haliteOnMap(GameMap gameMap) {
        int halite = 0;
        for (int x = 0; x < gameMap.width; x++) {
            for (int y = 0; y < gameMap.height; y++) {
                halite += gameMap.cells[x][y].halite;
            }
        }
        return halite;
    }

    private boolean haliteRemainingBelowThreshold(double threshold) {
        int haliteDepleted = initialHalite - remainingHalite;
        double proportionHaliteRemaining = (double) haliteDepleted / (double) initialHalite;
        Log.log(String.format("initialHalite: %s", initialHalite));
        Log.log(String.format("remainingHalite: %s", remainingHalite));
        Log.log(String.format("haliteDepleted: %s", haliteDepleted));
        Log.log(String.format("proportionHaliteRemaining: %s", proportionHaliteRemaining));
        Log.log(String.format("threshold: %s", threshold));

        Log.log(String.format("haliteRemainingBelowThreshold: %s", proportionHaliteRemaining < threshold));
        return proportionHaliteRemaining < threshold;
    }

    private boolean turnsRemainingBelowThreshold(double threshold) {
        double proportionTurnsRemaining = ((double) game.turnNumber / (double) Constants.MAX_TURNS);
        Log.log(String.format("turnsRemainingBelowThreshold: %s", proportionTurnsRemaining < threshold));

        return proportionTurnsRemaining < threshold;
    }

    @Override
    protected void onTurnStart() {
        navigationManager.onTurnStart(game);
        remainingHalite = haliteOnMap(game.gameMap);
        Log.log(String.format("Remaining halite: %s", remainingHalite));
    }

    @Override
    protected void onTurnEnd() {

    }

    @Override
    protected void onGameStart() {
        try {
            // Load ship genes from json file in resource folder
            initialiseOptimalShipGenes();
            Log.log(String.format("Ship genes: %s", shipGenes));
        } catch (IOException e){
            Log.log("Failed to load ship genes");
            e.printStackTrace();
        }
        try {
            // Load bot genes from json file in resource folder
            initialiseOptimalBotGenes();
            Log.log(String.format("Bot genes: %s", botGenes));
        } catch (IOException e){
            Log.log("Failed to load bot genes");
            e.printStackTrace();
        }
        initialHalite = haliteOnMap(game.gameMap);
        Log.log(String.format("Initial halite: %s", initialHalite));
        remainingHalite = initialHalite;
    }

    @Override
    protected void onGameEnd() {

    }

    @Override
    protected ArrayList<Command> getCommandQueue() {

        ArrayList<Command> commands = new ArrayList<>();

        // For each ship check some conditions and set the mode that the ship should run in
        setShipModes();
        // For each ship create a list of direction - score pairs
        updateShipDirectionScores();
        // Convert the ship direction scores into the best possible set of commands
        commands.addAll(navigationManager.resolveShipDirections(ships.values(), game.gameMap));

        // If the bot has enough halite to buy a ship
        if (game.me.halite >= Constants.SHIP_COST &&
                // and the create ship threshold is less than the proportion of turns left
                (haliteRemainingBelowThreshold(botGenes.getCreateShipHaliteRemainingThreshold()) && turnsRemainingBelowThreshold(botGenes.getCreateShipTurnRemainingThreshold())) &&

                // and the shipyard doesnt wont have a ship on it
                !navigationManager.getOccupiedPositions().contains(game.gameMap.at(game.me.shipyard).position))
        {
            // tell this shipyard to spawn
            commands.add(game.me.shipyard.spawn());
            // Aller the navigation manager that we are spawning a ship
            navigationManager.markOccupied(game.me.shipyard.position);
        }


        return commands;
    }

    @Override
    protected GeneticShip createShip(Ship initialStatus) {
        return new GeneticShip(initialStatus, shipGenes);
    }
}
