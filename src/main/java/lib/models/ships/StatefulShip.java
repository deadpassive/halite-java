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

import lib.hlt.*;
import lib.models.states.AbstractShipState;
import lib.models.states.WanderAndCollectState;
import lib.navigation.DirectionScore;
import lib.navigation.ShipNavigationInterface;

import java.util.List;

public class StatefulShip extends AbstractShip implements ShipNavigationInterface {
    private AbstractShipState currentState;
    private List<DirectionScore> directionScores;

    public StatefulShip(Ship initialStatus) {
        super(initialStatus);

        this.currentState = new WanderAndCollectState();
    }

    public void changeState(AbstractShipState newState) {
        // TODO: call changeState within each of the states. Might need another method (e.g. evaluateState) to determine
        // if the state should be changed
        if (currentState != null) {
            currentState.exit(this);
        }

        currentState = newState;

        if (currentState != null) {
            currentState.enter(this);
        }
    }

    public void updateState(Game game) {
        Log.log("Ship " + getId() + " evaluating state");
        currentState.evaluateState(game, this);
        // Ask the current state to evaluate the current situation and switch states if necessary
        currentState.execute(game, this);
    }

    @Override
    public List<DirectionScore> getDirectionScores() {
        return directionScores;
    }

    public void setDirectionScores(List<DirectionScore> directionScores) {
        this.directionScores = directionScores;
    }

    @Override
    public String toString() {
        return "StatefulShip{" +
                "currentState=" + currentState +
                '}';
    }
}
