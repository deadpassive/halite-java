package lib.models.genes;

public class BotGenes {
    private double createShipTurnRemainingThreshold;
    private double createShipHaliteRemainingThreshold;
    private double forcedReturnTurnRemainingThreshold;

    @Override
    public String toString() {
        return "BotGenes{" +
                "createShipTurnRemainingThreshold=" + createShipTurnRemainingThreshold +
                ", createShipHaliteRemainingThreshold=" + createShipHaliteRemainingThreshold +
                ", forcedReturnTurnRemainingThreshold=" + forcedReturnTurnRemainingThreshold +
                '}';
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
