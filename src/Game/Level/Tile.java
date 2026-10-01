package Game.Level;

import java.awt.Color;

// Тип клетки уровня. Цвет — это цвет пикселя на картинке уровня.
public enum Tile {
    FLOOR(new Color(255, 255, 255)),  // белый  — пол
    WALL(new Color(0, 0, 0)),         // чёрный — стена
    SPIKES(new Color(255, 0, 0)),     // красный — шипы
    PIT(new Color(0, 0, 255)),        // синий  — яма
    START(new Color(0, 255, 0)),      // зелёный — старт героя
    EXIT(new Color(255, 255, 0));     // жёлтый — выход

    public final Color color;

    Tile(Color color) {
        this.color = color;
    }

    // найти тип клетки по цвету пикселя; null — если цвет неизвестен
    public static Tile fromRgb(int rgb) {
        for (Tile tile : values()) {
            if ((tile.color.getRGB() & 0xFFFFFF) == (rgb & 0xFFFFFF)) {
                return tile;
            }
        }
        return null;
    }
}
