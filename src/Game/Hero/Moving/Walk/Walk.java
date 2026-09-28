package Game.Hero.Moving.Walk;

import Game.Hero.Point;

public class Walk implements MovingType {
    @Override 
    public void move(Point position, int dx, int dy, int dz) {
        position.x += dx;
        position.y += dy;
        position.z = 0;
    }
}