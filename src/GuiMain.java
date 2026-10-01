import Game.Gui.GameWindow;

import javax.swing.SwingUtilities;

public class GuiMain {
    public static void main(String[] args) {
        // весь код, работающий с окнами Swing, принято запускать
        // через invokeLater — так Swing делает это безопасно
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
