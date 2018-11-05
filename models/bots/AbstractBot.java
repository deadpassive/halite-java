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

import java.util.*;

public abstract class AbstractBot {

    Map<Integer, AbstractShip> ships = new HashMap<>();
    Game game;

    private void updateShips(Collection<Ship> shipStatuses) {
        Map<Integer, AbstractShip> newShips = new HashMap<>();

        for (Ship shipStatus : shipStatuses) {
            AbstractShip ship = ships.get(shipStatus.id.id);
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
        this.game = new Game();

        onGameStart();

        // At this point "game" variable is populated with initial map data.
        // This is a good place to do computationally expensive start-up pre-processing.
        // As soon as you call "ready" function below, the 2 second per turn timer will start.
        game.ready("MyJavaBot");

        Log.log("Successfully created bot! My Player ID is " + game.myId);

        do {
            game.updateFrame();

            updateShips(game.me.ships.values());

            onTurnStart();

            game.endTurn(getCommandQueue());

            onTurnEnd();

        } while (game.turnNumber != Constants.MAX_TURNS);

        onGameEnd();
    }

    protected abstract void onTurnStart();

    protected abstract void onTurnEnd();

    protected abstract void onGameStart();

    protected abstract void onGameEnd();

    protected abstract ArrayList<Command> getCommandQueue();

    protected abstract AbstractShip createShip(Ship initialStatus);
}
