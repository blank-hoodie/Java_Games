package Game.Hero.Moving.Fly;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;

public class Fly implements MovingType {
    @Override
    public void move(Point position, int dx, int dy) {
        // полёт теперь непрерывный: вызывается много раз, пока держишь Shift,
        // поэтому за один вызов двигаемся всего на 1 клетку
        position.x += dx;
        position.y += dy;
    }
}
