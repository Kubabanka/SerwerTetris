import javax.swing.*;

/**
 * G³ówna klasa programu.
 * Tworzy okno programu.
 */
//invokeLater - do tworzenia GUI
public class Main {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SwingUtilities.invokeLater(mainWindow);
    }
}
