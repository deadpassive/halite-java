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
import lib.models.ships.StatefulShip;

import java.util.Random;

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
    public Command execute(Game game, StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " executing state WanderAndCollect");
        if (game.gameMap.at(ship.getPosition()).halite < Constants.MAX_HALITE / 10 || ship.isFull()) {
            final Direction randomDirection = Direction.ALL_CARDINALS.get(randomGenerator.nextInt(4));
            Log.log("Ship " + ship.getId() + " moving in randomly selected direction " + randomDirection);
            return ship.move(randomDirection);
        } else {
            Log.log("Ship " + ship.getId() + " staying at current position to collect halite");
            return ship.stayStill();
        }
    }

    @Override
    public void exit(StatefulShip ship) {
        Log.log("Ship " + ship.getId() + " leaving state WanderAndCollect");
    }
}
