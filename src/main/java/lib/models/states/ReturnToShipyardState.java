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

import lib.hlt.Direction;
import lib.hlt.Game;
import lib.hlt.Log;
import lib.models.ships.StatefulShip;
import lib.navigation.DirectionScore;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReturnToShipyardState extends AbstractShipState {

    @Override
    public void enter(StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " entering state ReturnToShipyard");
    }

    @Override
    public void evaluateState(Game game, StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " evaluating current state ReturnToShipyard");
        if (ship.getPosition().equals(game.me.shipyard.position) && ship.getHalite() == 0) {
            // TODO: determine best state to go in to
            Log.log("Ship " + ship.getId() + " is at shipyard and empty - switching states");
            ship.changeState(new WanderAndCollectState());
        } else {
            Log.log("Ship " + ship.getId() + " is staying in the state ReturnToShipyard");
        }
    }

    @Override
    public void execute(Game game, StatefulShip ship) {
        // TODO: handle collisions and clever navigation
        Log.log("Ship " + ship.getId() + " executing state ReturnToShipyard");
        List<DirectionScore> directionScores = new ArrayList<>();
        directionScores.addAll(Direction.ALL_CARDINALS.stream()
                .map(d -> new DirectionScore(1000 - game.gameMap.calculateDistance(ship.getPosition().directionalOffset(d), game.me.shipyard.position), d))
                .collect(Collectors.toList())
        );
        directionScores.add(new DirectionScore(1000 - game.gameMap.calculateDistance(ship.getPosition(), game.me.shipyard.position), Direction.STILL));
        ship.setDirectionScores(directionScores);
    }

    @Override
    public void exit(StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " leaving state ReturnToShipyard");
    }
}
