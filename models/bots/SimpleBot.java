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
package models.bots;

import hlt.Command;
import hlt.Constants;
import hlt.Direction;
import hlt.Ship;
import models.ships.SimpleShip;

import java.util.ArrayList;
import java.util.Random;


public class SimpleBot extends AbstractBot<SimpleShip> {

    private static SimpleBot INSTANCE;

    public static void main(String[] args) {
        SimpleBot.getInstance().run(args);
    }

    private Random randomGenerator;

    private SimpleBot() {
        final long rngSeed;
        rngSeed = System.nanoTime();
        randomGenerator = new Random(rngSeed);
    }

    public static SimpleBot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleBot();
        }
        return INSTANCE;
    }

    @Override
    protected ArrayList<Command> getCommandQueue() {

        final ArrayList<Command> commandQueue = new ArrayList<>();

        for (final SimpleShip ship : ships.values()) {
            if (game.gameMap.at(ship.getPosition()).halite < Constants.MAX_HALITE / 10 || ship.isFull()) {
                final Direction randomDirection = Direction.ALL_CARDINALS.get(randomGenerator.nextInt(4));
                commandQueue.add(ship.move(randomDirection));
            } else {
                commandQueue.add(ship.stayStill());
            }
        }

        if (
                game.turnNumber <= 200 &&
                        game.me.halite >= Constants.SHIP_COST &&
                        !game.gameMap.at(game.me.shipyard).isOccupied())
        {
            commandQueue.add(game.me.shipyard.spawn());
        }

        return commandQueue;
    }

    @Override
    protected void onTurnStart() {

    }

    @Override
    protected void onTurnEnd() {

    }

    @Override
    protected void onGameStart() {

    }

    @Override
    protected void onGameEnd() {

    }

    @Override
    protected SimpleShip createShip(Ship initialStatus) {
        return new SimpleShip(initialStatus);
    }
}
