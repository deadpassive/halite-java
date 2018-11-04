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

import hlt.*;
import models.ships.AbstractShip;
import models.ships.SimpleShip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleBot extends AbstractBot {


    private static SimpleBot INSTANCE;

    private SimpleBot() {}

    public static SimpleBot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SimpleBot();
        }
        return INSTANCE;
    }

    public void run(final String[] args) {
        final long rngSeed;
        if (args.length > 1) {
            rngSeed = Integer.parseInt(args[1]);
        } else {
            rngSeed = System.nanoTime();
        }
        final Random rng = new Random(rngSeed);

        Game game = new Game();
        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.
        game.ready("MyJavaBot");

        // Create our starting ships
        // TODO: do we need this?
//        ships = game.me.ships.entrySet().stream().map(entry -> new SimpleShip(entry.getValue())).collect(Collectors.toList());

        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");

        for (;;) {
            game.updateFrame();
            final Player me = game.me;
            final GameMap gameMap = game.gameMap;

            updateShips(me.ships.values());

            final ArrayList<Command> commandQueue = new ArrayList<>();

            for (final AbstractShip ship : ships.values()) {
                if (gameMap.at(ship.getPosition()).halite < Constants.MAX_HALITE / 10 || ship.isFull()) {
                    final Direction randomDirection = Direction.ALL_CARDINALS.get(rng.nextInt(4));
                    commandQueue.add(ship.move(randomDirection));
                } else {
                    commandQueue.add(ship.stayStill());
                }
            }

            if (
                    game.turnNumber <= 200 &&
                            me.halite >= Constants.SHIP_COST &&
                            !gameMap.at(me.shipyard).isOccupied())
            {
                commandQueue.add(me.shipyard.spawn());
            }

            game.endTurn(commandQueue);
        }
    }

}
