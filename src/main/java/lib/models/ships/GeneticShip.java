package lib.models.ships;

import lib.hlt.*;
import lib.models.genes.ShipGenes;
import lib.models.modes.ShipMode;
import lib.navigation.DirectionScore;
import lib.navigation.NavigationUtils;
import lib.navigation.ShipNavigationInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneticShip extends AbstractShip implements ShipNavigationInterface {

    private final ShipGenes shipGenes;

    private ShipMode shipMode = ShipMode.GATHERING;

    private int distanceFromDeposit;

    private List<DirectionScore> directionScores;

    public GeneticShip(Ship initialStatus, ShipGenes shipGenes) {
        super(initialStatus);
        this.shipGenes = shipGenes;
    }

    public void updateDirectionScores(Game game) {
        directionScores = new ArrayList<>();
        switch (shipMode) {
            case FORCED_GATHERING:
                directionScores.addAll(Direction.ALL_CARDINALS.stream()
                        .map(d -> new DirectionScore(
                                0,
                                d)
                        ).collect(Collectors.toList())
                );
                // Whilst forced gathering its essential that the ship stays still else it dies so max it out
                directionScores.add(new DirectionScore(2000, Direction.STILL));
                break;

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
                        .map(d -> {
                            List<Position> positionsInRay = NavigationUtils.getMapCellsInRay(this.getPosition(), d, shipGenes.getRayLength());

                            // At most 1000, sometimes this can exceed 1000 without the min because lots of halite can drop in the same cell when ships crash
                            return new DirectionScore(
                                    Math.min(NavigationUtils.totalHaliteAtPositions(game.gameMap, positionsInRay, this.getPosition()) / positionsInRay.size(), 1000)
                                    , d
                            );
                        }
                        ).collect(Collectors.toList())
                );
                // Whilst migrating staying still is not desirable
                directionScores.add(new DirectionScore(0, Direction.STILL));
                break;

            case RETURNING:
                List<Position> depositories = game.me.dropoffs.values().stream().map(d -> d.position).collect(Collectors.toList());
                depositories.add(game.me.shipyard.position);

                Position cloesestDepository = NavigationUtils.closestPosition(this.getPosition(), depositories, game.gameMap);
                directionScores.addAll(Direction.ALL_CARDINALS.stream()
                        .map(d -> new DirectionScore(1000 - game.gameMap.calculateDistance(this.getPosition().directionalOffset(d), cloesestDepository), d))
                        .collect(Collectors.toList())
                );
                directionScores.add(new DirectionScore(1000 - game.gameMap.calculateDistance(this.getPosition(), cloesestDepository), Direction.STILL));
                break;
        }
    }

    public void updateDistanceFromDeposit(Game game) {
        Position shipPosition = this.getPosition();
        distanceFromDeposit = game.gameMap.calculateDistance(shipPosition, NavigationUtils.closestDepository(shipPosition, game));
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

    public int getDistanceFromDeposit() {
        return distanceFromDeposit;
    }

    @Override
    public String toString() {
        return "GeneticShip{" +
                "shipGenes=" + shipGenes +
                ", shipMode=" + shipMode +
                ", directionScores=" + directionScores +
                '}';
    }
}
