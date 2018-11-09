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
package lib.models.ships;

import lib.hlt.Command;
import lib.hlt.Direction;
import lib.hlt.Position;
import lib.hlt.Ship;

public class AbstractShip {

    private Ship shipStatus;

    AbstractShip(Ship initialStatus) {
        this.shipStatus = initialStatus;
    }

    public final Position getPosition() {
        return shipStatus.position;
    }

    public final int getHalite() {
        return shipStatus.halite;
    }

    public final void update(Ship shipStatus) {
        this.shipStatus = shipStatus;
    }

    public final int getId() {
        return shipStatus.id.id;
    }

    public final boolean isFull() {
        return shipStatus.isFull();
    }

    public Command move(Direction direction) {
        return shipStatus.move(direction);
    }

    public Command stayStill() {
        return shipStatus.stayStill();
    }


}
