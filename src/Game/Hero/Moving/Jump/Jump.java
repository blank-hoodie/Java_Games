package Game.Hero.Moving.Jump;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;

public class Jump implements MovingType {
    @Override
    public void move(Point position, int dx, int dy) {
        position.x += dx * 2;
        position.y += dy * 2;
    }
}
