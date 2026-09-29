package Game.Hero.Moving.Walk;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;

public class Walk implements MovingType {
    @Override
    public void move(Point position, int dx, int dy) {
        position.x += dx;
        position.y += dy;
    }
}
