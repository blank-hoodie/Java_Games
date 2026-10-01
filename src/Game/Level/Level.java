package Game.Level;

import Game.Enemy.Enemy;
import Game.Hero.Direction.Direction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Level {
    // цвета врагов на картинке (это не клетки: под врагом будет пол)
    private static final int ENEMY_HORIZONTAL = 0xFF00FF; // фиолетовый — ходит влево-вправо
    private static final int ENEMY_VERTICAL = 0x00FFFF;   // голубой — ходит вверх-вниз

    private final Tile[][] tiles; // tiles[y][x]
    private final int width;
    private final int height;
    private int startX = -1;
    private int startY = -1;

    // где и в какую сторону враги стоят в начале уровня
    private final List<Enemy> enemySpawns = new ArrayList<>();

    private Level(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
    }

    // загрузить уровень из картинки: 1 пиксель = 1 клетка
    public static Level load(String path) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось открыть уровень: " + path, e);
        }
        if (image == null) {
            throw new RuntimeException("Файл не является картинкой: " + path);
        }

        Level level = new Level(image.getWidth(), image.getHeight());

        for (int y = 0; y < level.height; y++) {
            for (int x = 0; x < level.width; x++) {
                int argb = image.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;
                int rgb = argb & 0xFFFFFF;

                // сначала проверяем, не враг ли это
                if (alpha != 0 && rgb == ENEMY_HORIZONTAL) {
                    level.enemySpawns.add(new Enemy(x, y, Direction.RIGHT));
                    level.tiles[y][x] = Tile.FLOOR;
                    continue;
                }
                if (alpha != 0 && rgb == ENEMY_VERTICAL) {
                    level.enemySpawns.add(new Enemy(x, y, Direction.DOWN));
                    level.tiles[y][x] = Tile.FLOOR;
                    continue;
                }

                Tile tile = Tile.fromRgb(argb);
                if (alpha == 0 || tile == null) {
                    if (alpha != 0) {
                        System.out.println("Внимание: неизвестный цвет в " + path
                                + " в точке (" + x + ", " + y + "), считаю полом");
                    }
                    tile = Tile.FLOOR;
                }
                if (tile == Tile.START) {
                    level.startX = x;
                    level.startY = y;
                }
                level.tiles[y][x] = tile;
            }
        }

        if (level.startX == -1) {
            throw new RuntimeException("На уровне " + path + " нет старта (зелёного пикселя)");
        }
        return level;
    }

    // новые враги в начальных позициях (при каждом перезапуске уровня)
    public List<Enemy> createEnemies() {
        List<Enemy> enemies = new ArrayList<>();
        for (Enemy spawn : enemySpawns) {
            enemies.add(new Enemy(spawn.position.x, spawn.position.y, spawn.getDirection()));
        }
        return enemies;
    }

    // всё, что за пределами карты, считается стеной
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return Tile.WALL;
        }
        return tiles[y][x];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
}
