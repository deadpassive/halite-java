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

import lib.hlt.Command;
import lib.hlt.Game;
import lib.models.ships.StatefulShip;

public abstract class AbstractShipState {

    public abstract void enter(StatefulShip ship);

    public abstract void evaluateState(Game game, StatefulShip ship);

    public abstract void execute(Game game, StatefulShip ship);

    public abstract void exit(StatefulShip ship);
}
