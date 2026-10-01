package Game.Hero;

import Game.Hero.Direction.Direction;
import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk.Walk;
import Game.Hero.Moving.Jump.Jump;
import Game.Hero.Moving.Fly.Fly;

public class Hero {
    public Point position = new Point();
    private Direction facing = Direction.DOWN;

    private final MovingType walk = new Walk();
    private final MovingType jump = new Jump();
    private final MovingType fly = new Fly();

    public Direction getFacing() {
        return facing;
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }

    public void turn(Direction direction) {
        facing = direction;
    }

    public void walk(Direction direction) {
        facing = direction;
        walk.move(position, direction.dx, direction.dy);
    }

    public void jump() {
        jump.move(position, facing.dx, facing.dy);
    }

    public void fly() {
        fly.move(position, facing.dx, facing.dy);
    }
}
