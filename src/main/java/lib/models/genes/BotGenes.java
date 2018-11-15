package lib.models.genes;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dev.bot_genes")
public class BotGenes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "bot_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private UUID gameId;

    @Column(name = "create_ship_turn_remaining_threshold", nullable = false)
    private double createShipTurnRemainingThreshold;

    @Column(name = "create_ship_halite_remaining_threshold", nullable = false)
    private double createShipHaliteRemainingThreshold;

    @Column(name = "create_dropoff_averageD_distance_to_halite", nullable = false)
    private double createDropoffAverageDistanceToHalite;

    @Column(name = "forced_return_turn_remaining_threshold", nullable = false)
    private double forcedReturnTurnRemainingThreshold;

    @Column(name = "create_dropoff_turn_remaining_threshold", nullable = false)
    private double createDropoffTurnRemainingThreshold;

    @Column(name = "create_dropoff_halite_remaining_threshold", nullable = false)
    private double createDropoffHaliteRemainingThreshold;

    public BotGenes() {
    }

    @Override
    public String toString() {
        return "BotGenes{" +
                "createShipTurnRemainingThreshold=" + createShipTurnRemainingThreshold +
                ", createShipHaliteRemainingThreshold=" + createShipHaliteRemainingThreshold +
                ", createDropoffAverageDistanceToHalite=" + createDropoffAverageDistanceToHalite +
                ", forcedReturnTurnRemainingThreshold=" + forcedReturnTurnRemainingThreshold +
                ", createDropoffTurnRemainingThreshold=" + createDropoffTurnRemainingThreshold +
                ", createDropoffHaliteRemainingThreshold=" + createDropoffHaliteRemainingThreshold +
                '}';
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreateShipTurnRemainingThreshold(double createShipTurnRemainingThreshold) {
        this.createShipTurnRemainingThreshold = createShipTurnRemainingThreshold;
    }

    public void setCreateShipHaliteRemainingThreshold(double createShipHaliteRemainingThreshold) {
        this.createShipHaliteRemainingThreshold = createShipHaliteRemainingThreshold;
    }

    public void setCreateDropoffAverageDistanceToHalite(double createDropoffAverageDistanceToHalite) {
        this.createDropoffAverageDistanceToHalite = createDropoffAverageDistanceToHalite;
    }

    public void setForcedReturnTurnRemainingThreshold(double forcedReturnTurnRemainingThreshold) {
        this.forcedReturnTurnRemainingThreshold = forcedReturnTurnRemainingThreshold;
    }

    public void setCreateDropoffTurnRemainingThreshold(double createDropoffTurnRemainingThreshold) {
        this.createDropoffTurnRemainingThreshold = createDropoffTurnRemainingThreshold;
    }

    public void setCreateDropoffHaliteRemainingThreshold(double createDropoffHaliteRemainingThreshold) {
        this.createDropoffHaliteRemainingThreshold = createDropoffHaliteRemainingThreshold;
    }

    public double getCreateDropoffTurnRemainingThreshold() {
        return createDropoffTurnRemainingThreshold;
    }

    public double getCreateDropoffHaliteRemainingThreshold() {
        return createDropoffHaliteRemainingThreshold;
    }

    public double getCreateDropoffAverageDistanceToHalite() {
        return createDropoffAverageDistanceToHalite;
    }

    public double getForcedReturnTurnRemainingThreshold() {
        return forcedReturnTurnRemainingThreshold;
    }

    public double getCreateShipTurnRemainingThreshold() {
        return createShipTurnRemainingThreshold;
    }

    public double getCreateShipHaliteRemainingThreshold() {
        return createShipHaliteRemainingThreshold;
    }
}
