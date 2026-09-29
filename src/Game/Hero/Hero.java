package Game.Hero;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk.Walk;

public class Hero {
    public Point position = new Point();
    private MovingType moving = new Walk(); // по умолчанию герой ходит

    public void setMoving(MovingType moving) {
        this.moving = moving;
    }

    public void move(int dx, int dy) {
        moving.move(position, dx, dy);
    }

    public void printPosition() {
        System.out.println("Позиция: x=" + position.x + ", y=" + position.y);
    }
}
