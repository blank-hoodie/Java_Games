package Game.Hero.Moving.Jump;

import Game.Hero.Point;

public class Jump implements MovingType {
    @Override
    public void move(Point position, int dx, int dy, int dz) {
        position.x = 0;
        position.y = 0;
        position.z += dz;
    }
}