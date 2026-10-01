package Game.Gui;

import Game.Hero.Hero;
import Game.Hero.Direction.Direction;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private static final int CELL_SIZE = 20;
    private static final int GRID_WIDTH = 30;
    private static final int GRID_HEIGHT = 20;

    // полёт: 1 клетка каждые FLY_EVERY_TICKS кадров (5 кадров ≈ 12 клеток в секунду)
    private static final int FLY_EVERY_TICKS = 5;

    private final Hero hero = new Hero();

    private boolean flying = false;    // держим ли сейчас Shift
    private boolean spaceHeld = false; // держим ли сейчас пробел
    private int tick = 0;              // счётчик кадров

    public GamePanel() {
        setPreferredSize(new Dimension(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE));
        setBackground(Color.WHITE);
        setFocusable(true);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                onKeyPressed(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                onKeyReleased(e.getKeyCode());
            }
        });

        // если окно потеряло фокус (например, переключился на другую программу),
        // "отпускаем" все клавиши, иначе герой может улететь сам по себе
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                flying = false;
                spaceHeld = false;
            }
        });

        // игровой цикл: ~60 раз в секунду
        Timer timer = new Timer(16, e -> update());
        timer.start();
    }

    private void onKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W -> moveOrTurn(Direction.UP);
            case KeyEvent.VK_S -> moveOrTurn(Direction.DOWN);
            case KeyEvent.VK_A -> moveOrTurn(Direction.LEFT);
            case KeyEvent.VK_D -> moveOrTurn(Direction.RIGHT);
            case KeyEvent.VK_SPACE -> {
                // если держать клавишу, система шлёт keyPressed много раз подряд.
                // Флаг spaceHeld пропускает повторы: прыжок только один раз на нажатие.
                if (!spaceHeld) {
                    spaceHeld = true;
                    hero.jump();
                    clampPosition();
                }
            }
            case KeyEvent.VK_SHIFT -> flying = true;
            default -> { }
        }
    }

    private void onKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_SPACE -> spaceHeld = false;
            case KeyEvent.VK_SHIFT -> flying = false;
            default -> { }
        }
    }

    // во время полёта WASD только поворачивают героя, а на земле — ещё и делают шаг
    private void moveOrTurn(Direction direction) {
        if (flying) {
            hero.turn(direction);
        } else {
            hero.walk(direction);
            clampPosition();
        }
    }

    // вызывается таймером каждый кадр
    private void update() {
        tick++;
        if (flying && tick % FLY_EVERY_TICKS == 0) {
            hero.fly();
            clampPosition();
        }
        repaint();
    }

    private void clampPosition() {
        if (hero.position.x < 0) hero.position.x = 0;
        if (hero.position.y < 0) hero.position.y = 0;
        if (hero.position.x > GRID_WIDTH - 1) hero.position.x = GRID_WIDTH - 1;
        if (hero.position.y > GRID_HEIGHT - 1) hero.position.y = GRID_HEIGHT - 1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int px = hero.position.x * CELL_SIZE;
        int py = hero.position.y * CELL_SIZE;

        // тело героя: синее в полёте, красное на земле
        g.setColor(flying ? Color.BLUE : Color.RED);
        g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

        // "глаз" — маленький чёрный квадрат у той стороны, куда смотрит герой
        Direction f = hero.getFacing();
        int eye = 6;
        int center = (CELL_SIZE - eye) / 2;
        int offset = CELL_SIZE / 2 - eye / 2; // насколько сдвинуть глаз от центра к краю
        int eyeX = px + center + f.dx * offset;
        int eyeY = py + center + f.dy * offset;
        g.setColor(Color.BLACK);
        g.fillRect(eyeX, eyeY, eye, eye);

        g.drawString("x=" + hero.position.x + " y=" + hero.position.y
                + "  смотрит: " + f + (flying ? "  [ПОЛЁТ]" : ""), 10, 15);
    }
}
