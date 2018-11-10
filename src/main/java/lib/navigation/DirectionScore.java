package lib.navigation;

import lib.hlt.Direction;

public class DirectionScore {
    private final int score;
    private final Direction direction;

    public DirectionScore(int score, Direction direction) {
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

    public int getScore() {
        return score;
    }

    public Direction getDirection() {
        return direction;
    }
}
