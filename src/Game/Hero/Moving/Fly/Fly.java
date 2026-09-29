package Game.Hero.Moving.Fly;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;

public class Fly implements MovingType {
    @Override
    public void move(Point position, int dx, int dy) {
        position.x += dx * 3;
        position.y += dy * 3;
    }
}
