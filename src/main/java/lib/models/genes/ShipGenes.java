package lib.models.genes;

import javax.persistence.*;

@Entity
@Table(name = "public.ship_genes")
public class ShipGenes {

    @Id
    @GeneratedValue
    private int id;

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
                "migrateHaliteAmount=" + migrateHaliteAmount +
                ", returnHaliteAmount=" + returnHaliteAmount +
                ", gatherPositionHaliteAmount=" + gatherPositionHaliteAmount +
                ", rayLength=" + rayLength +
                '}';
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
