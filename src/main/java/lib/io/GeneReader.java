package lib.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import lib.hlt.Game;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import org.apache.commons.io.IOUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.Table;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class GeneReader {

    private Random random = new Random();

    private final static String OPTIMIZED_SHIP_GENES_FILE_PATH = "optimized-ship-genes.csv";
    private final static String OPTIMIZED_BOT_GENES_FILE_PATH = "optimized-bot-genes.csv";

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
                // TODO switch to readOptimizedBotGenes
                return randomBotGenes();
            case HAND_TUNED:
                return readHandTunedBotGenes();
            case RANDOM:
                // TODO generate random genes
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
        shipGenes.setReturnHaliteAmount(random.nextInt(200) + 800);
        // Random number between 1 and 15
        shipGenes.setRayLength(random.nextInt(13) + 3);
        // Random number between 1 and 300
        shipGenes.setGatherPositionHaliteAmount(random.nextInt(250) + 1);

        return shipGenes;
    }

    private BotGenes randomBotGenes() {
        BotGenes botGenes = new BotGenes();
        botGenes.setForcedReturnTurnRemainingThreshold(0.0 + random.nextFloat() * (0.08 - 0.0));    // 0.1
        botGenes.setCreateDropoffHaliteRemainingThreshold(0.3 + random.nextFloat() * (0.5 - 0.3)); // 0.4
        botGenes.setCreateDropoffTurnRemainingThreshold(0.5 + random.nextFloat() * (0.7 - 0.5));   // 0.6
        botGenes.setCreateShipHaliteRemainingThreshold(0.3 + random.nextFloat() * (0.7 - 0.3));    // 0.4
        botGenes.setCreateShipTurnRemainingThreshold(0.4 + random.nextFloat() * (0.8 - 0.4));      // 0.6
        // Random number between 10 and 30
        botGenes.setCreateDropoffAverageDistanceToHalite(random.nextInt(20) + 10);

        return botGenes;
    }

    /**
     * Reads {@link BotGenes}s from a csv file and selects the {@link ShipGenes} that best suit the map.
     * @return Best {@link BotGenes} for the map
     * @throws IOException if there is a problem loading the ship genes csv
     */
    private BotGenes readOptimizedBotGenes() throws IOException {
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMIZED_BOT_GENES_FILE_PATH);

        Table table = Table.read().csv(genes, OPTIMIZED_BOT_GENES_FILE_PATH);

        // Filter incompatible rows (for maps with the wrong size / number of players)
        table = table.dropWhere(table.numberColumn("map_size").isNotEqualTo(game.gameMap.height));
        table = table.dropWhere(table.numberColumn("palyers").isNotEqualTo(game.players.size()));

        // Sort rows by how similar the halite density is
        DoubleColumn haliteDensityDelta = DoubleColumn.create(
                "halite_density_delta",
                Arrays.stream(table.doubleColumn("halite_density_bin").asDoubleArray())
                        .map(v -> Math.abs(v - initialHaliteDensity))
                        .toArray()
        );
        table.addColumns(haliteDensityDelta);
        table.sortDescendingOn("halite_density_delta");

        System.out.println(table);
        BotGenes botGenes = new BotGenes();

        botGenes.setCreateDropoffAverageDistanceToHalite(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setCreateShipTurnRemainingThreshold(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setCreateShipHaliteRemainingThreshold(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setCreateDropoffTurnRemainingThreshold(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setCreateDropoffHaliteRemainingThreshold(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));
        botGenes.setForcedReturnTurnRemainingThreshold(table.doubleColumn("create_dropoff_averaged_distance_to_halite").get(0));

        return botGenes;
    }

    /**
     * Reads {@link ShipGenes}s from a csv file and selects the {@link ShipGenes} that best suit the map.
     * @return Best {@link ShipGenes} for the map
     * @throws IOException if there is a problem loading the ship genes csv
     */
    private ShipGenes readOptimizedShipGenes() throws IOException {
        InputStream genes = getClass().getClassLoader().getResourceAsStream(OPTIMIZED_SHIP_GENES_FILE_PATH);

        Table table = Table.read().csv(genes, OPTIMIZED_SHIP_GENES_FILE_PATH);

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

        shipGenes.setGatherPositionHaliteAmount((int) Math.round(table.doubleColumn("collect_threshold").get(0)));
        shipGenes.setRayLength((int) Math.round(table.doubleColumn("ray_length").get(0)));
        shipGenes.setReturnHaliteAmount((int) Math.round(table.doubleColumn("deposit_threshold").get(0)));

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
