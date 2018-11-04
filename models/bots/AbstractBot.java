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

import hlt.Ship;
import models.ships.AbstractShip;
import models.ships.SimpleShip;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractBot {

    protected Map<Integer, AbstractShip> ships = new HashMap<>();

    protected void updateShips(Collection<Ship> shipStatuses) {
        Map<Integer, AbstractShip> newShips = new HashMap<>();

        for (Ship shipStatus : shipStatuses) {
            AbstractShip ship = ships.get(shipStatus.id.id);
            if (ship != null) {
                ships.get(shipStatus.id.id).update(shipStatus);
            } else {
                ship = new SimpleShip(shipStatus);
            }
            newShips.put(ship.getId(), ship);
        }

        ships = newShips;
    }

}
