/*
 * Copyright (c) Dotted Eyes Ltd 2018.
 * All Rights Reserved.
 *
 * This Software is the confidential information of
 * Dotted Eyes Ltd. 67-71 Northwood St,
 * Birmingham, B3 1TX, United Kingdom.
 *
 * The software may be used only in accordance with the terms
 * of the licence agreement made with Dotted Eyes Ltd.
 *
 */
package lib.models.states;

import lib.hlt.*;
import lib.models.ships.AbstractShip;
import lib.models.ships.StatefulShip;
import lib.navigation.DirectionScore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Mimic the default action where the ship wanders randomly collecting halite.
 */
public class WanderAndCollectState extends AbstractShipState {

    Random randomGenerator = new Random();

    @Override
    public void enter(StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " entering state WanderAndCollect");

    }

    @Override
    public void evaluateState(Game game, StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " evaluating current state WanderAndCollect");
        // TODO: figure out the best value for this
        if (ship.getHalite() > Constants.MAX_HALITE * 0.9) {
            Log.log("Ship " + ship.getId() + " has enough halite (" + ship.getHalite() + ") - switching states");
            ship.changeState(new ReturnToShipyardState());
        } else {
            Log.log("Ship " + ship.getId() + " does not have enough halite (" + ship.getHalite() + ") is staying in the state WanderAndCollect");
        }
    }

    @Override
    public void execute(Game game, StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " executing state WanderAndCollect");
        List<DirectionScore> directionScores = new ArrayList<>();
        // Add all the cardinal directions with scores based on how much halite they have
        directionScores.addAll(Direction.ALL_CARDINALS.stream()
                .map(d -> new DirectionScore(
                        game.gameMap.at(ship.getPosition().directionalOffset(d)).halite,
                        d)
                ).collect(Collectors.toList())
        );

        if (game.gameMap.at(ship.getPosition()).halite >= Constants.MAX_HALITE / 10) {
            Log.log("Ship " + ship.getId() + " would like to stay at current position to collect halite");
            // Add stay still as having the highest score because that's what we really want to do
            directionScores.add(new DirectionScore(1000, Direction.STILL));
        } else if (cantAffordToMove(game, ship)) {
            directionScores.add(new DirectionScore(10000, Direction.STILL));
        } else {
            // Add stay still as a last resort
            directionScores.add(new DirectionScore(0, Direction.STILL));
        }

        ship.setDirectionScores(directionScores);
    }

    @Override
    public void exit(StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " leaving state WanderAndCollect");
    }

    /**
     * Check that the ship has enough halite to move off the current cell that its on.
     * @param ship The {@link AbstractShip} to check for
     * @return true if the ship cant move, false if it can
     */
    private boolean cantAffordToMove(Game game, AbstractShip ship) {
        return ship.getHalite() < ((double) game.gameMap.at(ship.getPosition()).halite * 0.1);
    }
}
