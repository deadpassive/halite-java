package lib.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.hlt.Game;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import org.apache.commons.io.IOUtils;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class GeneReader {

    private Random random = new Random();

    private final static String OPTIMIZED_GENES_FILE_PATH = "optimized-genes.csv";

    private final static String HAND_TUNED_SHIP_GENES_FILE_PATH = "hand-tuned-ship-genes.json";
    private final static String HAND_TUNED_BOT_GENEs_FILE_PATH = "hand-tuned-bot-genes.json";

    private final Game game;
    private final double initialHaliteDensity;

    public GeneReader(Game game, Double initialHaliteDensity) {
        this.game = game;
        this.initialHaliteDensity = initialHaliteDensity;
    }

    public BotGenes readBotGenes(GeneSource source) throws IOException {
        switch (source){
            case OPTIMIZED:
                return readOptimizedBotGenes();
            case HAND_TUNED:
                return readHandTunedBotGenes();
            case RANDOM:
                return randomBotGenes();
        }
        return null;
    }

    public ShipGenes readShipGenes(GeneSource source) throws IOException {
        switch (source){
            case OPTIMIZED:
                return readOptimizedShipGenes();
            case HAND_TUNED:
                return readHandTunedShipGenes();
            case RANDOM:
                return randomShipGenes();
        }
        return null;
    }

    private ShipGenes randomShipGenes() {
        ShipGenes shipGenes = new ShipGenes();
        // Random number between 700 and 1000
        shipGenes.setReturnHaliteAmount(random.nextInt(1000));
        // Random number between 5 and 15
        shipGenes.setRayLength(random.nextInt(25));
        // Random number between 0 and 250
        shipGenes.setGatherPositionHaliteAmount(random.nextInt(300));

        return shipGenes;
    }

    private BotGenes randomBotGenes() {
        BotGenes botGenes = new BotGenes();
        botGenes.setForcedReturnTurnRemainingThreshold(random.nextFloat());  // 0.1
        botGenes.setCreateDropoffHaliteRemainingThreshold(random.nextFloat()); // 0.4
        botGenes.setCreateDropoffTurnRemainingThreshold(random.nextFloat());   // 0.6
        botGenes.setCreateShipHaliteRemainingThreshold(random.nextFloat());    // 0.4
        botGenes.setCreateShipTurnRemainingThreshold(random.nextFloat());      // 0.6
        // Random number between 10 and 30
        botGenes.setCreateDropoffAverageDistanceToHalite(random.nextInt(40));

        return botGenes;
    }

    /**
     * Reads {@link BotGenes}s from a csv file and selects the {@link ShipGenes} that best suit the map.
     * @return Best {@link BotGenes} for the map
     * @throws IOException if there is a problem loading the ship genes csv
     */
    private BotGenes readOptimizedBotGenes() throws IOException {
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMIZED_GENES_FILE_PATH);

        Table table = Table.read().csv(genes, OPTIMIZED_GENES_FILE_PATH);

        // Filter incompatible rows (for maps with the wrong size / number of players)
        table = table.dropWhere(table.numberColumn("map_size").isNotEqualTo(game.gameMap.height));
        table = table.dropWhere(table.numberColumn("players").isNotEqualTo(game.players.size()));

        // Sort rows by how similar the halite density is
        DoubleColumn haliteDensityDelta = DoubleColumn.create(
                "halite_density_delta",
                Arrays.stream(table.doubleColumn("halite_density_bin").asDoubleArray())
                        .map(v -> Math.abs(v - initialHaliteDensity))
                        .toArray()
        );
        table.addColumns(haliteDensityDelta);
        table.sortDescendingOn("halite_density_delta");

        BotGenes botGenes = new BotGenes();
        botGenes.setCreateDropoffAverageDistanceToHalite(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setCreateShipTurnRemainingThreshold(table.doubleColumn("create_ship_turn_remaining_threshold").get(0));
        botGenes.setCreateShipHaliteRemainingThreshold(table.doubleColumn("create_ship_halite_remaining_threshold").get(0));
        botGenes.setCreateDropoffTurnRemainingThreshold(table.doubleColumn("create_dropoff_turn_remaining_threshold").get(0));
        botGenes.setCreateDropoffHaliteRemainingThreshold(table.doubleColumn("create_dropoff_halite_remaining_threshold").get(0));
        botGenes.setForcedReturnTurnRemainingThreshold(table.doubleColumn("forced_return_turn_remaining_threshold").get(0));

        return botGenes;
    }

    /**
     * Reads {@link ShipGenes}s from a csv file and selects the {@link ShipGenes} that best suit the map.
     * @return Best {@link ShipGenes} for the map
     * @throws IOException if there is a problem loading the ship genes csv
     */
    private ShipGenes readOptimizedShipGenes() throws IOException {
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMIZED_GENES_FILE_PATH);

        Table table = Table.read().csv(genes, OPTIMIZED_GENES_FILE_PATH);

        // Filter incompatible rows (for maps with the wrong size / number of players)
        table = table.dropWhere(table.numberColumn("map_size").isNotEqualTo(game.gameMap.height));
        table = table.dropWhere(table.numberColumn("players").isNotEqualTo(game.players.size()));

        // Sort rows by how similar the halite density is
        DoubleColumn haliteDensityDelta = DoubleColumn.create(
                "halite_density_delta",
                Arrays.stream(table.doubleColumn("halite_density_bin").asDoubleArray())
                        .map(v -> Math.abs(v - initialHaliteDensity))
                        .toArray()
        );
        table.addColumns(haliteDensityDelta);

        table = table.sortAscendingOn("halite_density_delta");

        ShipGenes shipGenes = new ShipGenes();

        shipGenes.setGatherPositionHaliteAmount((int) Math.round(table.doubleColumn("gather_position_halite_amount").get(0)));
        shipGenes.setRayLength((int) Math.round(table.doubleColumn("ray_length").get(0)));
        shipGenes.setReturnHaliteAmount((int) Math.round(table.doubleColumn("return_halite_amount").get(0)));

        return shipGenes;
    }

    /**
     * Loads {@link ShipGenes} from json file.
     * @return {@link ShipGenes} loaded from the json file at HAND_TUNED_SHIP_GENES_FILE_PATH
     * @throws IOException if there is a problem loading the ship genes
     */
    private ShipGenes readHandTunedShipGenes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream genes = getClass().getClassLoader().getResourceAsStream(HAND_TUNED_SHIP_GENES_FILE_PATH);
        return mapper.readValue(IOUtils.toString(genes, "utf8"), ShipGenes.class);
    }

    /**
     * Loads {@link BotGenes} from json file.
     * @return {@link BotGenes} loaded from the json file at HAND_TUNED_BOT_GENES_FILE_PATH
     * @throws IOException if there is a problem loading the bot genes
     */
    private BotGenes readHandTunedBotGenes() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream genes = getClass().getClassLoader().getResourceAsStream(HAND_TUNED_BOT_GENEs_FILE_PATH);
        return mapper.readValue(IOUtils.toString(genes, "utf8"), BotGenes.class);
    }

}
