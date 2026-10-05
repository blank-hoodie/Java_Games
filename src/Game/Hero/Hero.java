package Game.Hero;

import Game.Hero.Point.Point;
import Game.Hero.Moving.MovingType;
import Game.Hero.Moving.Walk.Walk;
import Game.Items.Bow;

public class Hero {
    public Point position = new Point();
    private MovingType moving = new Walk(); // по умолчанию герой ходит
    private Bow bow = new Bow(5);           // лук с пятью стрелами

    public void setMoving(MovingType moving) {
        this.moving = moving;
    }

    public void move(int dx, int dy) {
        moving.move(position, dx, dy);
    }

    // выстрел из лука
    public void shoot() {
        if (bow.shoot()) {
            System.out.println("Выстрел! Осталось стрел: " + bow.getArrows());
        } else {
            System.out.println("Стрелы кончились!");
        }
    }

    public void printPosition() {
        System.out.println("Позиция: x=" + position.x + ", y=" + position.y
                + ", стрел: " + bow.getArrows());
    }
}
