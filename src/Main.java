import javax.swing.*;

/**
 * G³ówna klasa programu.
 * Tworzy okno programu.
 */
public class Main {
    /**
     * Tworzy nowe okno.
     */
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SwingUtilities.invokeLater(mainWindow);
    }
}
//invokeLater - do tworzenia GUI
