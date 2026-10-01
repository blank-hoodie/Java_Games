package Game.Gui;

import Game.Hero.Hero;
import Game.Hero.Moving.Walk.Walk;
import Game.Hero.Moving.Jump.Jump;
import Game.Hero.Moving.Fly.Fly;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private static final int CELL_SIZE = 20;   // размер одной "клетки" в пикселях
    private static final int GRID_WIDTH = 30;   // ширина поля в клетках
    private static final int GRID_HEIGHT = 20;  // высота поля в клетках

    private final Hero hero = new Hero();
    private String currentMode = "Ходьба";

    public GamePanel() {
        setPreferredSize(new java.awt.Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        setBackground(Color.WHITE);
        setFocusable(true); // обязательно, иначе панель не будет получать нажатия клавиш

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKey(e.getKeyCode());
                repaint(); // перерисовать после каждого действия
            }
        });

        // игровой цикл: обновляем экран ~60 раз в секунду,
        // даже если никто ничего не нажимал
        Timer timer = new Timer(16, e -> repaint());
        timer.start();
    }

    private void handleKey(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W -> hero.move(0, -1); // вверх (в Swing ось y растёт вниз)
            case KeyEvent.VK_S -> hero.move(0, 1);
            case KeyEvent.VK_A -> hero.move(-1, 0);
            case KeyEvent.VK_D -> hero.move(1, 0);
            case KeyEvent.VK_1 -> { hero.setMoving(new Walk()); currentMode = "Ходьба"; }
            case KeyEvent.VK_2 -> { hero.setMoving(new Jump()); currentMode = "Прыжок"; }
            case KeyEvent.VK_3 -> { hero.setMoving(new Fly());  currentMode = "Полёт"; }
            default -> { /* неизвестная клавиша — игнорируем */ }
        }
        clampPosition();
    }

    // не даём герою уйти за пределы поля
    private void clampPosition() {
        int maxX = GRID_WIDTH - 1;
        int maxY = GRID_HEIGHT - 1;
        if (hero.position.x < 0) hero.position.x = 0;
        if (hero.position.y < 0) hero.position.y = 0;
        if (hero.position.x > maxX) hero.position.x = maxX;
        if (hero.position.y > maxY) hero.position.y = maxY;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // сначала очищает фон

        // герой — простой красный квадрат в клетке (x, y)
        g.setColor(Color.RED);
        g.fillRect(hero.position.x * CELL_SIZE, hero.position.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // текст с текущим режимом и координатами
        g.setColor(Color.BLACK);
        g.drawString("Режим: " + currentMode + "   x=" + hero.position.x + " y=" + hero.position.y, 10, 15);
    }
}
