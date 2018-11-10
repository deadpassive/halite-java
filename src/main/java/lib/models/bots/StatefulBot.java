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

import lib.hlt.Command;
import lib.hlt.Constants;
import lib.hlt.Ship;
import lib.models.ships.StatefulShip;

import java.util.ArrayList;

public class StatefulBot extends AbstractBot<StatefulShip> {

    private static StatefulBot INSTANCE;

    public static StatefulBot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StatefulBot();
        }

        return INSTANCE;
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
    protected ArrayList<Command> getCommandQueue() {
        final ArrayList<Command> commandQueue = new ArrayList<>();

        for (final StatefulShip ship : ships.values()) {
            commandQueue.add(ship.getCommand(game));
        }

        // TODO: manage bot-level updates better - use states for this too?
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
    protected StatefulShip createShip(Ship initialStatus) {
        return new StatefulShip(initialStatus);
    }
}
