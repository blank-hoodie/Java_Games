package Game.Enemy;

import Game.Hero.Direction.Direction;
import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk.Walk;
import Game.Hero.Point.Point;

// Враг-патрульный: идёт прямо, у препятствия разворачивается.
// Решение "можно ли идти дальше" принимает Game, потому что только он знает уровень.
public class Enemy {
    public Point position = new Point();
    private Direction direction;
    private final MovingType moving = new Walk(); // враг ходит так же, как герой

    public Enemy(int x, int y, Direction direction) {
        position.x = x;
        position.y = y;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void turnAround() {
        direction = direction.opposite();
    }

    public void step() {
        moving.move(position, direction.dx, direction.dy);
    }
}
