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
import hlt.Ship;
import models.ships.AbstractShip;
import models.ships.JonsShip;

import java.util.ArrayList;

public class JonsBot extends AbstractBot<JonsShip> {

    private static JonsBot INSTANCE;

    public static void main(String[] args) {
        SimpleBot.getInstance().run(args);
    }

    private JonsBot() {}

    public static JonsBot getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JonsBot();
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

        for (AbstractShip ship : ships.values()) {
            // TODO: update each ships's "mission"
        }
        return null;
    }

    @Override
    protected JonsShip createShip(Ship initialStatus) {
        return null;
    }
}
