package Game;

import Game.Enemy.Enemy;
import Game.Hero.Hero;
import Game.Hero.Direction.Direction;
import Game.Level.Level;
import Game.Level.Tile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Правила игры: уровни, жизни, столкновения, враги.
// GamePanel только рисует и передаёт сюда нажатия клавиш.
public class Game {

    private static final int MAX_LIVES = 3;
    private static final String LEVELS_FOLDER = "levels";

    // скорость в кадрах (60 кадров = 1 секунда)
    private static final int FLY_EVERY_TICKS = 5;      // полёт: 12 клеток в секунду
    private static final int ENEMY_EVERY_TICKS = 15;   // враги: 4 клетки в секунду
    private static final int INVULNERABLE_TICKS = 60;  // неуязвимость после удара: 1 секунда

    private final Hero hero = new Hero();
    private Level level;
    private List<Enemy> enemies = new ArrayList<>();
    private int levelNumber = 1;
    private int lives = MAX_LIVES;
    private boolean flying = false;
    private boolean won = false;

    private int tick = 0;
    private int invulnerableTicks = 0;

    // последняя безопасная клетка, на которой герой стоял на земле
    private int safeX;
    private int safeY;

    private String message = "";

    public Game() {
        loadLevel(1);
    }

    // ---------- игровой цикл ----------

    // вызывается каждый кадр (~60 раз в секунду)
    public void update() {
        if (won) return;
        tick++;

        if (invulnerableTicks > 0) {
            invulnerableTicks--;
        }
        if (tick % FLY_EVERY_TICKS == 0) {
            flyStep();
        }
        if (tick % ENEMY_EVERY_TICKS == 0) {
            for (Enemy enemy : enemies) {
                moveEnemy(enemy);
            }
            checkEnemyHit(); // враг мог сам прийти на клетку героя
        }
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
        invulnerableTicks = 0;
        hero.setPosition(level.getStartX(), level.getStartY());
        safeX = level.getStartX();
        safeY = level.getStartY();
        enemies = level.createEnemies(); // враги возвращаются на свои места
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

        if (flying) {
            hero.turn(direction);
            return;
        }

        int nx = hero.position.x + direction.dx;
        int ny = hero.position.y + direction.dy;
        if (level.getTile(nx, ny) == Tile.WALL) {
            hero.turn(direction);
            return;
        }

        hero.walk(direction);
        checkLanding();
        checkEnemyHit();
    }

    public void jump() {
        if (won || flying) return;

        Direction f = hero.getFacing();
        int midX = hero.position.x + f.dx;
        int midY = hero.position.y + f.dy;
        Tile middle = level.getTile(midX, midY);
        Tile landing = level.getTile(hero.position.x + f.dx * 2, hero.position.y + f.dy * 2);

        if (middle == Tile.WALL || landing == Tile.WALL) {
            return;
        }
        if (middle == Tile.SPIKES) {
            loseLife("Наткнулся на шипы!");
            return;
        }
        // перепрыгнуть врага нельзя — врезаемся в него
        if (invulnerableTicks == 0 && enemyAt(midX, midY)) {
            loseLife("Врезался во врага!");
            return;
        }

        hero.jump();
        checkLanding();
        checkEnemyHit();
    }

    public void startFlying() {
        if (won) return;
        flying = true;
    }

    public void stopFlying() {
        if (!flying) return;
        flying = false;
        checkLanding();
    }

    // один шаг полёта, вызывается из update()
    private void flyStep() {
        if (!flying) return;

        Direction f = hero.getFacing();
        int nx = hero.position.x + f.dx;
        int ny = hero.position.y + f.dy;
        if (level.getTile(nx, ny) == Tile.WALL) {
            loseLife("Врезался в стену!");
            return;
        }
        hero.fly();
        checkEnemyHit(); // враги задевают и в полёте
    }

    // ---------- враги ----------

    // враг может стоять только на полу, старте или выходе
    private boolean enemyCanEnter(int x, int y) {
        Tile tile = level.getTile(x, y);
        return tile != Tile.WALL && tile != Tile.PIT && tile != Tile.SPIKES;
    }

    private void moveEnemy(Enemy enemy) {
        Direction d = enemy.getDirection();
        if (!enemyCanEnter(enemy.position.x + d.dx, enemy.position.y + d.dy)) {
            enemy.turnAround();
            d = enemy.getDirection();
            // если и сзади препятствие — враг зажат и стоит на месте
            if (!enemyCanEnter(enemy.position.x + d.dx, enemy.position.y + d.dy)) {
                return;
            }
        }
        enemy.step();
    }

    private boolean enemyAt(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.position.x == x && enemy.position.y == y) {
                return true;
            }
        }
        return false;
    }

    private void checkEnemyHit() {
        if (won || invulnerableTicks > 0) return;
        if (enemyAt(hero.position.x, hero.position.y)) {
            loseLife("Тебя задел враг!");
        }
    }

    // ---------- проверки ----------

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
        invulnerableTicks = INVULNERABLE_TICKS;
    }

    // ---------- геттеры для отрисовки ----------

    public Hero getHero() { return hero; }
    public Level getLevel() { return level; }
    public List<Enemy> getEnemies() { return enemies; }
    public int getLevelNumber() { return levelNumber; }
    public int getLives() { return lives; }
    public boolean isFlying() { return flying; }
    public boolean isInvulnerable() { return invulnerableTicks > 0; }
    public String getMessage() { return message; }
}
