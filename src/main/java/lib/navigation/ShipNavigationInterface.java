package lib.navigation;

import lib.hlt.Command;
import lib.hlt.Direction;
import lib.hlt.Position;

import java.util.List;

public interface ShipNavigationInterface {
    public List<DirectionScore> getDirectionScores();
    public Position getPosition();
    public Command move(Direction direction);
}
