package lib.models.genes;

public class ShipGenes {
    private int migrateHaliteAmount;
    private int returnHaliteAmount;
    private int gatherPositionHaliteAmount;
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
