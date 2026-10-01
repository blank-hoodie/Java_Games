package Game;

import Game.Hero.Hero;
import Game.Hero.Direction.Direction;
import Game.Level.Level;
import Game.Level.Tile;

import java.io.File;

// Правила игры: уровни, жизни, столкновения.
// GamePanel только рисует и передаёт сюда нажатия клавиш.
public class Game {

    private static final int MAX_LIVES = 3;
    private static final String LEVELS_FOLDER = "levels";

    private final Hero hero = new Hero();
    private Level level;
    private int levelNumber = 1;
    private int lives = MAX_LIVES;
    private boolean flying = false;
    private boolean won = false;

    // последняя безопасная клетка, на которой герой стоял на земле.
    // Сюда он возвращается после потери жизни.
    private int safeX;
    private int safeY;

    private String message = "";

    public Game() {
        loadLevel(1);
    }

    // ---------- уровни ----------

    private String levelPath(int number) {
        return LEVELS_FOLDER + "/level" + number + ".png";
    }

    private void loadLevel(int number) {
        levelNumber = number;
        level = Level.load(levelPath(number));
        restartLevel();
        message = "Уровень " + number;
    }

    private void restartLevel() {
        lives = MAX_LIVES;
        flying = false;
        hero.setPosition(level.getStartX(), level.getStartY());
        safeX = level.getStartX();
        safeY = level.getStartY();
    }

    private void nextLevel() {
        if (new File(levelPath(levelNumber + 1)).exists()) {
            loadLevel(levelNumber + 1);
        } else {
            won = true;
            flying = false;
            message = "Победа! Все уровни пройдены";
        }
    }

    // ---------- действия героя ----------

    public void walk(Direction direction) {
        if (won) return;

        // в полёте WASD только поворачивают
        if (flying) {
            hero.turn(direction);
            return;
        }

        int nx = hero.position.x + direction.dx;
        int ny = hero.position.y + direction.dy;
        if (level.getTile(nx, ny) == Tile.WALL) {
            hero.turn(direction); // в стену не идём, только поворачиваемся
            return;
        }

        hero.walk(direction);
        checkLanding();
    }

    public void jump() {
        if (won || flying) return;

        Direction f = hero.getFacing();
        int x = hero.position.x;
        int y = hero.position.y;
        Tile middle = level.getTile(x + f.dx, y + f.dy);
        Tile landing = level.getTile(x + f.dx * 2, y + f.dy * 2);

        // через стену и в стену прыгнуть нельзя
        if (middle == Tile.WALL || landing == Tile.WALL) {
            return;
        }
        // яму перепрыгиваем, а о шипы спотыкаемся
        if (middle == Tile.SPIKES) {
            loseLife("Наткнулся на шипы!");
            return;
        }

        hero.jump();
        checkLanding();
    }

    public void startFlying() {
        if (won) return;
        flying = true;
    }

    public void stopFlying() {
        if (!flying) return;
        flying = false;
        checkLanding(); // приземлились — проверяем, куда
    }

    // один шаг полёта, вызывается из игрового цикла
    public void flyStep() {
        if (!flying || won) return;

        Direction f = hero.getFacing();
        int nx = hero.position.x + f.dx;
        int ny = hero.position.y + f.dy;
        if (level.getTile(nx, ny) == Tile.WALL) {
            loseLife("Врезался в стену!");
            return;
        }
        hero.fly(); // над ямами и шипами пролетаем спокойно
    }

    // ---------- проверки ----------

    // вызывается, когда герой оказался на земле
    private void checkLanding() {
        Tile tile = level.getTile(hero.position.x, hero.position.y);
        switch (tile) {
            case PIT -> loseLife("Упал в яму!");
            case SPIKES -> loseLife("Наступил на шипы!");
            case EXIT -> nextLevel();
            default -> {
                safeX = hero.position.x;
                safeY = hero.position.y;
            }
        }
    }

    private void loseLife(String reason) {
        lives--;
        flying = false;
        if (lives <= 0) {
            restartLevel();
            message = reason + " Жизни кончились, уровень заново";
        } else {
            hero.setPosition(safeX, safeY);
            message = reason + " Осталось жизней: " + lives;
        }
    }

    // ---------- геттеры для отрисовки ----------

    public Hero getHero() { return hero; }
    public Level getLevel() { return level; }
    public int getLevelNumber() { return levelNumber; }
    public int getLives() { return lives; }
    public boolean isFlying() { return flying; }
    public String getMessage() { return message; }
}
