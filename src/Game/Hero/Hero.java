package Game.Hero;

import Game.Hero.Direction.Direction;
import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk.Walk;
import Game.Hero.Moving.Jump.Jump;
import Game.Hero.Moving.Fly.Fly;

public class Hero {
    public Point position = new Point();
    private Direction facing = Direction.DOWN; // куда смотрит герой

    // у героя теперь сразу все три способа передвижения
    private final MovingType walk = new Walk();
    private final MovingType jump = new Jump();
    private final MovingType fly = new Fly();

    public Direction getFacing() {
        return facing;
    }

    // повернуться, не двигаясь
    public void turn(Direction direction) {
        facing = direction;
    }

    // повернуться и сделать шаг
    public void walk(Direction direction) {
        facing = direction;
        walk.move(position, direction.dx, direction.dy);
    }

    // прыжок туда, куда смотрим
    public void jump() {
        jump.move(position, facing.dx, facing.dy);
    }

    // один "шаг" полёта туда, куда смотрим
    public void fly() {
        fly.move(position, facing.dx, facing.dy);
    }

    public void printPosition() {
        System.out.println("Позиция: x=" + position.x + ", y=" + position.y
                + ", смотрит: " + facing);
    }
}
