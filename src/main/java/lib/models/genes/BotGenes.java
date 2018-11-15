package lib.models.genes;

import javax.persistence.*;

@Entity
@Table(name = "public.bot_genes")
public class BotGenes {

    @Id
    @GeneratedValue
    private int id;

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
