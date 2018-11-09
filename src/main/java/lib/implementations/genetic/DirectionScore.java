package lib.implementations.genetic;

import lib.hlt.Direction;
import lib.hlt.Position;

public class DirectionScore {
    private final Double score;
    private final Direction direction;

    public DirectionScore(Double score, Direction direction) {
        this.score = score;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "DirectionScore{" +
                "score=" + score +
                ", direction=" + direction +
                '}';
    }

    public Double getScore() {
        return score;
    }

    public Direction getDirection() {
        return direction;
    }
}
