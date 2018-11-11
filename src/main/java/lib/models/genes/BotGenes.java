package lib.models.genes;

public class BotGenes {
    private double createShipTurnRemainingThreshold;
    private double createShipHaliteRemainingThreshold;
    private double createDropoffAverageDistanceToHalite;
    private double forcedReturnTurnRemainingThreshold;
    private double createDropoffTurnRemainingThreshold;
    private double createDropoffHaliteRemainingThreshold;

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
