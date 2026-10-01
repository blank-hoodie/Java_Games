package Game.Gui;

import javax.swing.JFrame;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Моя игра");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        add(panel);

        pack();                    // подогнать размер окна под панель
        setLocationRelativeTo(null); // открыть по центру экрана
        setResizable(false);
        setVisible(true);

        panel.requestFocusInWindow(); // чтобы сразу ловить нажатия клавиш
    }
}
