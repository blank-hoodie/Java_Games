package Game.Hero.Moving.Jump;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;

public class Jump implements MovingType {
    @Override
    public void move(Point position, int dx, int dy, int dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }
}