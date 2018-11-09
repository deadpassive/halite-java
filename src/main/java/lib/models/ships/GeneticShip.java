package lib.models.ships;

import lib.hlt.Direction;
import lib.hlt.Ship;

import java.util.ArrayList;
import java.util.List;

public class GeneticShip extends AbstractShip {

    private final ShipGenes shipGenes;

    private ShipMode shipMode = ShipMode.GATHERING;

    private List<DirectionScore> directionScores;

    public GeneticShip(Ship initialStatus, ShipGenes shipGenes) {
        super(initialStatus);
        this.shipGenes = shipGenes;
    }

    public void updateDirectionScores() {
        directionScores = new ArrayList<>();
        switch (shipMode) {
            case GATHERING:
                directionScores.add(new DirectionScore(0.0, Direction.NORTH));
                directionScores.add(new DirectionScore(0.0, Direction.EAST));
                directionScores.add(new DirectionScore(0.0, Direction.SOUTH));
                directionScores.add(new DirectionScore(0.0, Direction.WEST));
                directionScores.add(new DirectionScore(1.0, Direction.STILL));
            case MIGRATING:
                directionScores.add(new DirectionScore(1.0, Direction.NORTH));
                directionScores.add(new DirectionScore(0.0, Direction.EAST));
                directionScores.add(new DirectionScore(0.1, Direction.SOUTH));
                directionScores.add(new DirectionScore(0.0, Direction.WEST));
                directionScores.add(new DirectionScore(0.5, Direction.STILL));
            case DEPOSITING:
                // TODO get the directions towards the nearest depot and increase their score
                directionScores.add(new DirectionScore(1.0, Direction.NORTH));
                directionScores.add(new DirectionScore(1.0, Direction.EAST));
                directionScores.add(new DirectionScore(1.0, Direction.SOUTH));
                directionScores.add(new DirectionScore(1.0, Direction.WEST));
                directionScores.add(new DirectionScore(0.5, Direction.STILL));
        }
    }

    public ShipMode getShipMode() {
        return shipMode;
    }

    public void setShipMode(ShipMode shipMode) {
        this.shipMode = shipMode;
    }

    public List<DirectionScore> getDirectionScores() {
        return directionScores;
    }

    public ShipGenes getShipGenes() {
        return shipGenes;
    }
}
