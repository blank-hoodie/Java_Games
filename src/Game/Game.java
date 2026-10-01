package Game;

import Game.Hero.Hero;
import Game.Hero.Direction.Direction;

import java.util.Scanner;

// консольная версия игры
public class Game {
    public void start() {
        Hero hero = new Hero();
        Scanner scanner = new Scanner(System.in);

        System.out.println("w/a/s/d - идти, j - прыжок, f - шаг полёта, q - выход");

        while (true) {
            hero.printPosition();
            System.out.print("Команда: ");
            String command = scanner.nextLine();

            switch (command) {
                case "q":
                    System.out.println("Игра окончена");
                    return;
                case "w":
                    hero.walk(Direction.UP);
                    break;
                case "s":
                    hero.walk(Direction.DOWN);
                    break;
                case "a":
                    hero.walk(Direction.LEFT);
                    break;
                case "d":
                    hero.walk(Direction.RIGHT);
                    break;
                case "j":
                    hero.jump();
                    break;
                case "f":
                    hero.fly();
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
    }
}
