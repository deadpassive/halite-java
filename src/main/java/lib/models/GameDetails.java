package lib.models;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dev.games")
public class GameDetails {

    // Unique identifier for this game (from this bots perspective)
    @Id
    @Column(name = "bot_id", updatable = false, nullable = false)
    private UUID gameId;

    // How many players are in the game
    @Column(name = "players", nullable = false)
    private int players;
    // How big the map is (height is always equal to width)
    @Column(name = "map_size", nullable = false)
    private int mapSize;

    // Total amount of halite on the map at the start of the game
    @Column(name = "initial_halite", nullable = false)
    private int initialHalite;
    // Average amount of halite in each map cell
    @Column(name = "initial_halite_density", nullable = false)
    private double initialHaliteDensity;

    @Column(name = "halite_stockpiled", nullable = false)
    private int haliteStockpiled;

    @Override
    public String toString() {
        return "GameDetails{" +
                "gameId=" + gameId +
                ", players=" + players +
                ", mapSize=" + mapSize +
                ", initialHalite=" + initialHalite +
                ", initialHaliteDensity=" + initialHaliteDensity +
                ", haliteStockpiled=" + haliteStockpiled +
                '}';
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public int getInitialHalite() {
        return initialHalite;
    }

    public void setInitialHalite(int initialHalite) {
        this.initialHalite = initialHalite;
    }

    public double getInitialHaliteDensity() {
        return initialHaliteDensity;
    }

    public void setInitialHaliteDensity(double initialHaliteDensity) {
        this.initialHaliteDensity = initialHaliteDensity;
    }

    public int getHaliteStockpiled() {
        return haliteStockpiled;
    }

    public void setHaliteStockpiled(int haliteStockpiled) {
        this.haliteStockpiled = haliteStockpiled;
    }

    public static final class GameDetailsBuilder {
        // Unique identifier for this game (from this bots perspective)
        private UUID gameId;
        // How many players are in the game
        private int players;
        // How big the map is (height is always equal to width)
        private int mapSize;
        // Total amount of halite on the map at the start of the game
        private int initialHalite;
        // Average amount of halite in each map cell
        private double initialHaliteDensity;
        private int haliteStockpiled;

        private GameDetailsBuilder() {
        }

        public static GameDetailsBuilder aGameDetails() {
            return new GameDetailsBuilder();
        }

        public GameDetailsBuilder withGameId(UUID gameId) {
            this.gameId = gameId;
            return this;
        }

        public GameDetailsBuilder withPlayers(int players) {
            this.players = players;
            return this;
        }

        public GameDetailsBuilder withMapSize(int mapSize) {
            this.mapSize = mapSize;
            return this;
        }

        public GameDetailsBuilder withInitialHalite(int initialHalite) {
            this.initialHalite = initialHalite;
            return this;
        }

        public GameDetailsBuilder withInitialHaliteDensity(double initialHaliteDensity) {
            this.initialHaliteDensity = initialHaliteDensity;
            return this;
        }

        public GameDetailsBuilder withHaliteStockpiled(int haliteStockpiled) {
            this.haliteStockpiled = haliteStockpiled;
            return this;
        }

        public GameDetails build() {
            GameDetails gameDetails = new GameDetails();
            gameDetails.setGameId(gameId);
            gameDetails.setPlayers(players);
            gameDetails.setMapSize(mapSize);
            gameDetails.setInitialHalite(initialHalite);
            gameDetails.setInitialHaliteDensity(initialHaliteDensity);
            gameDetails.setHaliteStockpiled(haliteStockpiled);
            return gameDetails;
        }
    }
}
