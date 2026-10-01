package Game.Gui;

import Game.Game;
import Game.Hero.Hero;
import Game.Hero.Direction.Direction;
import Game.Level.Level;
import Game.Level.Tile;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private static final int CELL_SIZE = 20;
    private static final int HUD_HEIGHT = 24;     // полоска сверху для текста
    private static final int FLY_EVERY_TICKS = 5;

    private final Game game = new Game();

    private boolean spaceHeld = false;
    private int tick = 0;
    private Level shownLevel; // уровень, под который подогнан размер окна

    public GamePanel() {
        setBackground(Color.WHITE);
        setFocusable(true);
        fitToLevel();

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

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                game.stopFlying();
                spaceHeld = false;
            }
        });

        Timer timer = new Timer(16, e -> update());
        timer.start();
    }

    private void onKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_W -> game.walk(Direction.UP);
            case KeyEvent.VK_S -> game.walk(Direction.DOWN);
            case KeyEvent.VK_A -> game.walk(Direction.LEFT);
            case KeyEvent.VK_D -> game.walk(Direction.RIGHT);
            case KeyEvent.VK_SPACE -> {
                if (!spaceHeld) {
                    spaceHeld = true;
                    game.jump();
                }
            }
            case KeyEvent.VK_SHIFT -> game.startFlying();
            default -> { }
        }
    }

    private void onKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_SPACE -> spaceHeld = false;
            case KeyEvent.VK_SHIFT -> game.stopFlying();
            default -> { }
        }
    }

    private void update() {
        tick++;
        if (tick % FLY_EVERY_TICKS == 0) {
            game.flyStep();
        }
        // уровни могут быть разного размера — подгоняем окно при смене уровня
        if (game.getLevel() != shownLevel) {
            fitToLevel();
        }
        repaint();
    }

    private void fitToLevel() {
        shownLevel = game.getLevel();
        setPreferredSize(new Dimension(
                shownLevel.getWidth() * CELL_SIZE,
                shownLevel.getHeight() * CELL_SIZE + HUD_HEIGHT));
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.pack();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLevel(g);
        drawHero(g);
        drawHud(g);
    }

    private void drawLevel(Graphics g) {
        Level level = game.getLevel();
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                Tile tile = level.getTile(x, y);
                g.setColor(tile.color);
                g.fillRect(x * CELL_SIZE, y * CELL_SIZE + HUD_HEIGHT, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawHero(Graphics g) {
        Hero hero = game.getHero();
        int px = hero.position.x * CELL_SIZE;
        int py = hero.position.y * CELL_SIZE + HUD_HEIGHT;

        // оранжевый на земле, фиолетовый в полёте
        // (красный/синий/зелёный/жёлтый уже заняты клетками уровня)
        g.setColor(game.isFlying() ? new Color(180, 0, 220) : new Color(255, 140, 0));
        g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

        Direction f = hero.getFacing();
        int eye = 6;
        int center = (CELL_SIZE - eye) / 2;
        int offset = CELL_SIZE / 2 - eye / 2;
        g.setColor(Color.BLACK);
        g.fillRect(px + center + f.dx * offset, py + center + f.dy * offset, eye, eye);
    }

    private void drawHud(Graphics g) {
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, getWidth(), HUD_HEIGHT);
        g.setColor(Color.WHITE);
        g.drawString("Уровень " + game.getLevelNumber()
                + "   Жизни: " + game.getLives()
                + "   " + game.getMessage(), 8, 16);
    }
}
