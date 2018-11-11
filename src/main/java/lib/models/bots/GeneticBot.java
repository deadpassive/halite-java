package lib.models.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.hlt.*;
import lib.investment.Investment;
import lib.investment.InvestmentManager;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import lib.models.modes.BotMode;
import lib.models.modes.ShipMode;
import lib.models.ships.*;
import lib.navigation.DirectionScore;
import lib.navigation.NavigationManager;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GeneticBot extends AbstractBot<GeneticShip> {

    private final static String OPTIMAL_SHIP_GENEs_FILE_PATH = "optimal-ship-genes.json";
    private final static String OPTIMAL_BOT_GENEs_FILE_PATH = "optimal-bot-genes.json";

    private ShipGenes shipGenes;
    private BotGenes botGenes;

    private BotMode botMode = BotMode.INVESTING_IN_SHIPS;

    private NavigationManager navigationManager = new NavigationManager();
    private InvestmentManager investmentManager;

    private int initialHalite;
    private int remainingHalite;
    private double avgDistanceToHalite;

    private GeneticShip dropoffCandidate;

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

    private void setBotMode() {
        // If we are towards the end of the game then set mode to FORCED_RETURNING which will modify the navigation manager
        if (((double) (Constants.MAX_TURNS - game.turnNumber) / Constants.MAX_TURNS) < botGenes.getForcedReturnTurnRemainingThreshold()) {

            botMode = BotMode.FORCED_RETURNING;

            // If the create ship threshold is less than the proportion of halite left
        } else if (haliteRemainingBelowThreshold(botGenes.getCreateShipHaliteRemainingThreshold()) &&
                // and the threshold is less than the proportion of turns left
                turnsRemainingBelowThreshold(botGenes.getCreateShipTurnRemainingThreshold()) &&
                // and the threshold of building a drop off is not met
                avgDistanceToHalite < (botGenes.getCreateDropoffAverageDistanceToHalite())) {

            botMode = BotMode.INVESTING_IN_SHIPS;

            // If the threshold for building a drop off is met
        } else if (haliteRemainingBelowThreshold(botGenes.getCreateDropoffHaliteRemainingThreshold()) &&
                // and the threshold is less than the proportion of turns left
                turnsRemainingBelowThreshold(botGenes.getCreateDropoffTurnRemainingThreshold()) &&
                // and the threshold of building a drop off is met
                avgDistanceToHalite > (botGenes.getCreateDropoffAverageDistanceToHalite())) {

            botMode = BotMode.INVESTING_IN_DROPOFFS;

            // Otherwise stockpile halite
        } else {

            botMode = BotMode.STOCKPILING;
        }
        Log.log(String.format("Bot mode: %s", botMode));
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

            // If the bot is in forced returning mode
            if (botMode.equals(BotMode.FORCED_RETURNING)) {
                // then set the ships mode to returning
                ship.setShipMode(ShipMode.RETURNING);
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

    private void updateManagers() {

        switch (botMode){
            case INVESTING_IN_SHIPS:
                investmentManager.setNextInvestment(Investment.SHIP);
                navigationManager.clearIgnoredPositions();
                break;

            case INVESTING_IN_DROPOFFS:
                dropoffCandidate = ships.values().stream().max(Comparator.comparing(GeneticShip::getDistanceFromDeposit)).get();
                investmentManager.setDropoffCandidate(dropoffCandidate);
                investmentManager.setNextInvestment(Investment.DROPOFF);
                navigationManager.clearIgnoredPositions();
                break;

            case STOCKPILING:
                investmentManager.setNextInvestment(Investment.NONE);
                navigationManager.clearIgnoredPositions();
                break;

            case FORCED_RETURNING:
                investmentManager.setNextInvestment(Investment.NONE);
                List<Position> depositPositions = game.me.dropoffs.values().stream().map(dropoff -> dropoff.position).collect(Collectors.toList());
                depositPositions.add(game.me.shipyard.position);
                navigationManager.addAllIgnoredPosition(depositPositions);
                break;
        }
    }

    private int haliteOnMap(GameMap gameMap) {
        Log.log("Calculating total halite on the map");
        int halite = 0;
        for (int x = 0; x < gameMap.width; x++) {
            for (int y = 0; y < gameMap.height; y++) {
                halite += gameMap.cells[x][y].halite;
            }
        }
        return halite;
    }

    private double calculateAverageDistanceToGatheringShips() {
        OptionalDouble distance = ships.values().stream()
                .filter(s -> s.getShipMode().equals(ShipMode.GATHERING))
                .mapToInt(GeneticShip::getDistanceFromDeposit)
                .average();
        if (distance.isPresent()) {
            return distance.getAsDouble();
        } else {
            return 0.0d;
        }
    }

    private boolean haliteRemainingBelowThreshold(double threshold) {
        int haliteDepleted = initialHalite - remainingHalite;
        double proportionHaliteRemaining = (double) haliteDepleted / (double) initialHalite;

        Log.log(String.format("haliteRemainingBelowThreshold: %s", proportionHaliteRemaining < threshold));
        return proportionHaliteRemaining < threshold;
    }

    private boolean turnsRemainingBelowThreshold(double threshold) {
        double proportionTurnsRemaining = ((double) game.turnNumber / (double) Constants.MAX_TURNS);
        Log.log(String.format("turnsRemainingBelowThreshold: %s", proportionTurnsRemaining < threshold));

        return proportionTurnsRemaining < threshold;
    }

    public ArrayList<Command> cleanseMoveCommand(ArrayList<Command> commands, String id) {
        Log.log(String.format("Attempting to remove move command for %s", id));
        return (ArrayList<Command>) commands.stream().filter(c -> !c.command.contains("m " + id)).collect(Collectors.toList());
    }

    @Override
    protected void onTurnStart() {
        navigationManager.onTurnStart(game);
        investmentManager.onTurnStart();

        // Update each ships distance from its closest deposit
        for (GeneticShip ship : ships.values()) {
            ship.updateDistanceFromDeposit(game);
        }
        avgDistanceToHalite = calculateAverageDistanceToGatheringShips();
        remainingHalite = haliteOnMap(game.gameMap);

        Log.log(String.format("Average distance from depository to halite: %s", avgDistanceToHalite));
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
        investmentManager = new InvestmentManager(navigationManager);

        initialHalite = haliteOnMap(game.gameMap);
        remainingHalite = initialHalite;

        Log.log(String.format("Initial halite: %s", initialHalite));
    }

    @Override
    protected void onGameEnd() {

    }

    @Override
    protected ArrayList<Command> getCommandQueue() {

        ArrayList<Command> commands = new ArrayList<>();

        // For each ship check some conditions and set the mode that the ship should run in
        setShipModes();
        // For each ship create a list of direction -> score pairs
        updateShipDirectionScores();

        // Check state of the game and update the bots mode accordingly
        setBotMode();
        // Use the bots mode to update the managers (navigation / investment)
        updateManagers();

        // Convert the ship direction scores into the best possible set of commands
        commands.addAll(navigationManager.resolveShipDirections(ships.values(), game.gameMap));

        // Get a command from the investment manager (create a ship / dropoff)
        commands.addAll(investmentManager.getCommand(game));
        // If we have issued a makeDropoff command then we need to make sure that the ship receiving that command isn't
        // trying to move as well
        if (investmentManager.isDropOffCommandIssued()) {
            // When I remove this move command sometimes it results in a ship crashing into the new dropoff... sigh
            commands = cleanseMoveCommand(commands, Integer.toString(dropoffCandidate.getId()));
        }

        return commands;
    }

    @Override
    protected GeneticShip createShip(Ship initialStatus) {
        return new GeneticShip(initialStatus, shipGenes);
    }

    @Override
    protected String getBotName() {
        return "testing";
    }
}
