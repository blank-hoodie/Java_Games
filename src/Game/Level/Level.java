package Game.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Level {
    private final Tile[][] tiles; // tiles[y][x]
    private final int width;
    private final int height;
    private int startX = -1;
    private int startY = -1;

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

                Tile tile = Tile.fromRgb(argb);
                if (alpha == 0 || tile == null) {
                    // прозрачные и неизвестные цвета считаем полом
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
