package lib.navigation;

import lib.hlt.Command;
import lib.hlt.Direction;
import lib.hlt.Position;

import java.util.List;

public interface ShipNavigationInterface {
    List<DirectionScore> getDirectionScores();
    Position getPosition();
    Command move(Direction direction);
    int getId();
}
