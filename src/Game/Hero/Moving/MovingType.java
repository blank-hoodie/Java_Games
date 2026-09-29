package Game.Hero.Moving;

import Game.Hero.Point.Point;

public interface MovingType {
    void move(Point position, int dx, int dy, int dz);
}
