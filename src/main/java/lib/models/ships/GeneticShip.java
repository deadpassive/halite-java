package lib.models.ships;

import lib.hlt.*;
import lib.models.genes.ShipGenes;
import lib.navigation.DirectionScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticShip extends AbstractShip {

    private final ShipGenes shipGenes;

    private ShipMode shipMode = ShipMode.GATHERING;

    private List<DirectionScore> directionScores;

    public GeneticShip(Ship initialStatus, ShipGenes shipGenes) {
        super(initialStatus);
        this.shipGenes = shipGenes;
    }

    public void updateDirectionScores(Game game) {
        directionScores = new ArrayList<>();
        switch (shipMode) {
            case GATHERING:
                directionScores.addAll(Direction.ALL_CARDINALS.stream()
                        .map(d -> new DirectionScore(
                                game.gameMap.at(this.getPosition().directionalOffset(d)).halite,
                                d)
                        ).collect(Collectors.toList())
                );
                // Whilst gathering staying still is the most desirable desirable
                directionScores.add(new DirectionScore(1000, Direction.STILL));
                break;

            case MIGRATING:
                directionScores.addAll(Direction.ALL_CARDINALS.stream()
                        .map(d -> new DirectionScore(
                                game.gameMap.at(this.getPosition().directionalOffset(d)).halite,
                                d)
                        ).collect(Collectors.toList())
                );
                // Whilst migrating staying still is not desirable
                directionScores.add(new DirectionScore(0, Direction.STILL));
                break;

            case RETURNING:
                directionScores.addAll(Direction.ALL_CARDINALS.stream()
                        .map(d -> new DirectionScore(1000 - game.gameMap.calculateDistance(this.getPosition().directionalOffset(d), game.me.shipyard.position), d))
                        .collect(Collectors.toList())
                );
                directionScores.add(new DirectionScore(1000 - game.gameMap.calculateDistance(this.getPosition(), game.me.shipyard.position), Direction.STILL));
                break;
        }
        Log.log(String.format("Ship %s - %s", this.getId(), directionScores));

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
