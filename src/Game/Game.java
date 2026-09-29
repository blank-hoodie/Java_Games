package Game;

import Game.Hero.Hero;
import Game.Hero.Moving.Walk.Walk;
import Game.Hero.Moving.Jump.Jump;
import Game.Hero.Moving.Fly.Fly;

import java.util.Scanner;

public class Game {
    public void start() {
        Hero hero = new Hero();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Способ движения: 1 - ходить, 2 - прыгать, 3 - летать");
        System.out.println("Направление: w/a/s/d");
        System.out.println("q - выход");

        while (true) {
            hero.printPosition();
            System.out.print("Команда: ");
            String command = scanner.nextLine();

            switch (command) {
                case "q":
                    System.out.println("Игра окончена");
                    return;
                case "1":
                    hero.setMoving(new Walk());
                    System.out.println("Герой теперь ходит");
                    break;
                case "2":
                    hero.setMoving(new Jump());
                    System.out.println("Герой теперь прыгает");
                    break;
                case "3":
                    hero.setMoving(new Fly());
                    System.out.println("Герой теперь летает");
                    break;
                case "w":
                    hero.move(0, 1);
                    break;
                case "s":
                    hero.move(0, -1);
                    break;
                case "a":
                    hero.move(-1, 0);
                    break;
                case "d":
                    hero.move(1, 0);
                    break;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
    }
}
