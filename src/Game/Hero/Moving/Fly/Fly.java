package Game.Hero.Moving.Fly;

import Game.Hero.Point;

public class Fly  int dx, int dy, int dz MovingType {
    @Override
    public void move(Point position, int dx, int dy, int dz) {
        position.x += dx;
        position.y += dy;
        position.z += dz;
    }
}