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
package lib.models.bots;

import lib.hlt.*;
import lib.models.ships.AbstractShip;

import java.util.*;

public abstract class AbstractBot<ShipType extends AbstractShip> {

    Map<Integer, ShipType> ships = new HashMap<>();
    Game game;
    Random randomGenerator;

    private void updateShips(Collection<Ship> shipStatuses) {
        Map<Integer, ShipType> newShips = new HashMap<>();

        for (Ship shipStatus : shipStatuses) {
            ShipType ship = ships.get(shipStatus.id.id);
            if (ship != null) {
                ships.get(shipStatus.id.id).update(shipStatus);
            } else {
                ship = createShip(shipStatus);
            }
            newShips.put(ship.getId(), ship);
        }

        ships = newShips;
    }

    public void run(final String[] args) {
        final long rngSeed;
        if (args.length > 1) {
            rngSeed = Integer.parseInt(args[1]);
        } else {
            rngSeed = System.nanoTime();
        }
        randomGenerator = new Random(rngSeed);

        this.game = new Game();

        onGameStart();

        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.
        game.ready(getBotName());

        Log.log("Successfully created bot! My Player ID is " + game.myId + ". Bot rng seed is " + rngSeed + ".");

        do {
            game.updateFrame();

            updateShips(game.me.ships.values());

            onTurnStart();

            game.endTurn(getCommandQueue());

            onTurnEnd();

            if (game.turnNumber == Constants.MAX_TURNS -1) {
                // On the penultimate turn call onGameEnd() (after the final move the processes are
                // forced to terminate by halite.exe)
                onGameEnd();
            }
        } while (game.turnNumber != Constants.MAX_TURNS );
    }

    protected abstract String getBotName();

    protected abstract void onTurnStart();

    protected abstract void onTurnEnd();

    protected abstract void onGameStart();

    protected abstract void onGameEnd();

    protected abstract ArrayList<Command> getCommandQueue();

    protected abstract ShipType createShip(Ship initialStatus);
}