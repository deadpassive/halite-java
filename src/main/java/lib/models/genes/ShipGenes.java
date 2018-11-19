package lib.models.genes;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dev.ship_genes")
public class ShipGenes {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ship_id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "bot_id", nullable = false)
    private UUID botId;

    @Column(name = "migrate_halite_amount", nullable = false)
    private int migrateHaliteAmount;

    @Column(name = "return_halite_amount", nullable = false)
    private int returnHaliteAmount;

    @Column(name = "gather_position_halite_amount", nullable = false)
    private int gatherPositionHaliteAmount;

    @Column(name = "ray_length", nullable = false)
    private int rayLength;

    @Override
    public String toString() {
        return "ShipGenes{" +
                "id=" + id +
                ", botId=" + botId +
                ", migrateHaliteAmount=" + migrateHaliteAmount +
                ", returnHaliteAmount=" + returnHaliteAmount +
                ", gatherPositionHaliteAmount=" + gatherPositionHaliteAmount +
                ", rayLength=" + rayLength +
                '}';
    }

    public UUID getBotId() {
        return botId;
    }

    public void setBotId(UUID botId) {
        this.botId = botId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMigrateHaliteAmount(int migrateHaliteAmount) {
        this.migrateHaliteAmount = migrateHaliteAmount;
    }

    public void setReturnHaliteAmount(int returnHaliteAmount) {
        this.returnHaliteAmount = returnHaliteAmount;
    }

    public void setGatherPositionHaliteAmount(int gatherPositionHaliteAmount) {
        this.gatherPositionHaliteAmount = gatherPositionHaliteAmount;
    }

    public void setRayLength(int rayLength) {
        this.rayLength = rayLength;
    }

    public int getRayLength() {
        return rayLength;
    }

    public int getMigrateHaliteAmount() {
        return migrateHaliteAmount;
    }

    public int getReturnHaliteAmount() {
        return returnHaliteAmount;
    }

    public int getGatherPositionHaliteAmount() {
        return gatherPositionHaliteAmount;
    }
}
