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
package models.ships;

import hlt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JonsShip extends AbstractShip {

    // TODO: this should be handled in a state machine
    private String missionType;
    private String missionPhase;
    private Position target;
    private Random random = new Random();


    JonsShip(Ship initialStatus) {
        super(initialStatus);
    }

    private Command moveCollect(Game game, Position moveTo) {
        if (game.gameMap.at(getPosition()).halite < Constants.MAX_HALITE / 10 || isFull()) {
            // Choose the next location
            // TODO: handle navigation properly
            return moveTowards(game, moveTo);
        } else {
            return stayStill();
        }
    }

    private Command moveTowards(Game game, Position moveTo) {
        Direction direction = game.gameMap.naiveNavigate(this.shipStatus, moveTo);
        return move(direction);
    }

    public Command update(Game game) {
        if (missionType == null) {
            initMission(game);
        }

        if (missionPhase.equals("OUTWARD")) {
            if (isFull()) {
                // switch state and move back towards shipyard
                missionPhase = "RETURN";
                return moveTowards(game, game.me.shipyard.position);
            }
        }


        // Choose the next location
        if (missionPhase.equals("OUTWARD")) {
            // Move towards the target
            return moveCollect(game, target);
        } else {
            // Move towards the shipyard
            return moveCollect(game, game.me.shipyard.position);
        }


//        if (missionType.equals("COLLECTION_RUN")) {
//            if (missionPhase.equals("OUTWARD")) {
//                // Move towards the target, collecting on the way
//                if(this.getPosition().equals(target)) {
//                    // We have reached our destination - collect until full or nothing left then return
//                } else {
//                    // We are on our way out
//                }
//            }
//        }
    }

    private void initMission(Game game) {
        Log.log("Generating mission for ship " + this.getId());
        missionType = "COLLECTION_RUN";
        missionPhase = "OUTWARD";
        List<Position> randomTargets = new ArrayList<>();
        do {
            // TODO: think how best to generate targets
            int xPos = random.nextInt(game.gameMap.width);
            int yPos = random.nextInt(game.gameMap.height);
            randomTargets.add(new Position(xPos, yPos));
        } while (randomTargets.size() < 5);

        this.target = randomTargets.get(0);
    }
}
