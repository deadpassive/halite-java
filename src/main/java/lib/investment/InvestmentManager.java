package lib.investment;

import lib.hlt.Command;
import lib.hlt.Constants;
import lib.hlt.Game;
import lib.hlt.Log;
import lib.models.ships.AbstractShip;
import lib.navigation.NavigationManagerInterface;

import java.util.ArrayList;
import java.util.List;


public class InvestmentManager {

    // Used to ensure that investments align with navigation (ie don't spawn a ship in the path of another ship)
    private NavigationManagerInterface navigationManager;

    private Investment nextInvestment;

    private AbstractShip dropoffCandidate;

    private boolean dropOffCommandIssued = false;

    public InvestmentManager(NavigationManagerInterface navigationManager) {
        this.navigationManager = navigationManager;
    }

    public List<Command> getCommand(Game game) {
        List<Command> command = new ArrayList<>();
        switch (nextInvestment){
            case NONE:
                Log.log("Not investing this turn");
                break;

            case SHIP:
                Log.log("Investing in a ship if possible");
                Command spawnShipCommand = spawnShip(game);
                if (spawnShipCommand != null) {
                    command.add(spawnShipCommand);
                }
                break;

            case DROPOFF:
                Log.log("Investing in a ship if possible");
                Command spawnDropoffCommand = spawnDropoff(game);
                if (spawnDropoffCommand != null) {
                    command.add(spawnDropoffCommand);
                    dropOffCommandIssued = true;
                }
        }
        return command;
    }

    private Command spawnDropoff(Game game) {
        // If there is enough halite for a dropoff
        if (game.me.halite >= Constants.DROPOFF_COST &&
                // and the candidate dropoff ship isnt on an occupied position
                !navigationManager.getOccupiedPositions().contains(dropoffCandidate.getPosition())) {
            return dropoffCandidate.makeDropoff();
        }
        return null;
    }

    private Command spawnShip(Game game) {
        // If the bot has enough halite for a ship
        if (game.me.halite >= Constants.SHIP_COST &&
                // and the shipyard doesnt / wont have a ship on it
                !navigationManager.getOccupiedPositions().contains(game.gameMap.at(game.me.shipyard).position)) {

            navigationManager.markOccupied(game.me.shipyard.position);
            return game.me.shipyard.spawn();
        }
        return null;
    }

    public void setNextInvestment(Investment nextInvestment) {
        this.nextInvestment = nextInvestment;
    }

    public void setDropoffCandidate(AbstractShip dropoffCandidate) {
        this.dropoffCandidate = dropoffCandidate;
    }

    public AbstractShip getDropoffCandidate() {
        return dropoffCandidate;
    }

    public boolean isDropOffCommandIssued() {
        return dropOffCommandIssued;
    }

    public void setDropOffCommandIssued(boolean dropOffCommandIssued) {
        this.dropOffCommandIssued = dropOffCommandIssued;
    }

    public void onTurnStart() {
        setDropOffCommandIssued(false);
    }
}
