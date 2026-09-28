package Game.Hero;

import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk;

public class Hero {
    Point position = new Point();
    MovingType moving = new Walk();

    public void move(int dx, int dy) {
        moving.move(position, dx, dy);
    }
}