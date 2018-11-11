package lib.models.genes;

public class BotGenes {
    private double createShipTurnRemainingThreshold;
    private double createShipHaliteRemainingThreshold;
    private double createDropoffAverageDistanceToHalite;
    private double forcedReturnTurnRemainingThreshold;

    @Override
    public String toString() {
        return "BotGenes{" +
                "createShipTurnRemainingThreshold=" + createShipTurnRemainingThreshold +
                ", createShipHaliteRemainingThreshold=" + createShipHaliteRemainingThreshold +
                ", createDropoffAverageDistanceToHalite=" + createDropoffAverageDistanceToHalite +
                ", forcedReturnTurnRemainingThreshold=" + forcedReturnTurnRemainingThreshold +
                '}';
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
