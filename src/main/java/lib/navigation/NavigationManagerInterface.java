package lib.navigation;

import lib.hlt.Command;
import lib.hlt.GameMap;
import lib.hlt.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface NavigationManagerInterface {

    List<Position> getOccupiedPositions();

    ArrayList<Command> resolveShipDirections(Collection<? extends ShipNavigationInterface> ships, GameMap gameMap);

    void markOccupied(Position occupiedPosition);
}
