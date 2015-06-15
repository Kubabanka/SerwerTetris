import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Odpowiada za interfejs graficzny (GUI) aplikacji serwera.
 */
public class MainWindow implements Runnable, LogChangedListener {
    private JFrame mainFrame;
    private JTextArea logConsoleTextArea;
    private ButtonGroup aButtonGroup;
    private JRadioButton logAllRadioButton;
    private JRadioButton logUserRadioButton;
    private JRadioButton logNoneRadioButton;
    private JPanel topPanel;
    private JButton resetLogConsoleButton;
    private Server appServer;

    /**
     * Inicjalizuje graficzne komponenty u¿yte w programie.
     */
    private void initializeComponents(){
        /**
         * Wygl¹d buttonów bêdzie dostosowywa³ siê do systemu operacyjnego.
         */
        UIManager.getSystemLookAndFeelClassName();
        /**
         * Inicjalizuje g³ówn¹ ramkê okna.
         */
        initializeMainFrame();
        /**
         * Inicjaluzje pole tekstowe konsoli logów.
         */
        initializeLogConsoleTextField();
        /**
         * Inicjalizuje obiekt s³u¿¹cy do grupowania przycisków.
         */
        initializeAButtonGroup();
        /**
         * Inicjalizuje panel na samej górze okna.
         */
        initializeTopPanel();
        /**
         * Inicjalizuje jeden z przycisków s³u¿¹cych do ustawienia, które logi bêd¹ zapisywane.
         * All - wszysto
         */
        initializeLogAllRadioButton();
        /**
         * Inicjalizuje jeden z przycisków s³u¿¹cych do ustawienia, które logi bêd¹ zapisywane.
         * None - nic
         */
        initializeLogNoneRadioButton();
        /**
         * Inicjalizuje jeden z przycisków s³u¿¹cych do ustawienia, które logi bêd¹ zapisywane.
         * User - u¿ytkownicy
         */
        initializeLogUserRadioButton();
        /**
         * Inicjalizuje przycisk restartuj¹cy widok w konsoli.
         */
        initializeResetLogConsole();

        appServer = new Server();
        Thread serverThread = new Thread(appServer);
        serverThread.start();

        // Gdy zamykamy okno, zamykany jest równie¿ serwer.
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                appServer.stopServer();
                System.exit(0);
            }
        });
        appServer.addLogChangedListener(this);
        resetLogConsoleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logConsoleTextArea.setText("");
            }
        });
    }

    /**
     * Inicjalizuje przycisk Reset konsoli.
     */
    private void initializeResetLogConsole() {
        resetLogConsoleButton = new JButton();
        resetLogConsoleButton.setText("Reset konsoli");
        topPanel.add(resetLogConsoleButton);
    }

    /**
     * Inicjalizuje górny panel.
     */
    private void initializeTopPanel() {
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "wybierz"));
        mainFrame.add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Inicjalizuje pole na logi.
     */
    private void initializeLogConsoleTextField() {
        logConsoleTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(logConsoleTextArea);
        scrollPane.setViewportView(logConsoleTextArea);
        logConsoleTextArea.setEditable(false);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
    }


    /**
     * Inicjalizuje g³ówn¹ ramkê.
     */
    private void initializeMainFrame() {
        Dimension screnDimension = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame = new JFrame();
        mainFrame.setSize(800, 480);
        mainFrame.setLayout(new BorderLayout());
        int x = (int) (screnDimension.getWidth() - mainFrame.getWidth()) / 2;
        int y = (int) (screnDimension.getHeight() - mainFrame.getHeight()) / 2;
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocation(x, y);
        mainFrame.setMinimumSize(new Dimension(400, 240));
        mainFrame.setVisible(true);
    }

    /**
     * Inicjalizuje buttony.
     */
    private void initializeAButtonGroup(){
        aButtonGroup = new ButtonGroup();
    }

    /**
     * Inicjalizuje buttony wyboru opcji "wszystko".
     */
    private void initializeLogAllRadioButton(){
        logAllRadioButton = new JRadioButton("wszystko",true);
        aButtonGroup.add(logAllRadioButton);
        topPanel.add(logAllRadioButton);
        logAllRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                changeLogLevel();
            }
        });
    }
    /**
     * Inicjalizuje buttony wyboru opcji "uzytkownikow".
     */
    private void initializeLogUserRadioButton(){
        logUserRadioButton = new JRadioButton("uzytkownikow ",false);
        aButtonGroup.add(logUserRadioButton);
        topPanel.add(logUserRadioButton);
        logUserRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                changeLogLevel();
            }
        });
    }
    /**
     * Inicjalizuje buttony wyboru opcji "nic".
     */
    private void initializeLogNoneRadioButton(){
        logNoneRadioButton = new JRadioButton("nic",false);
        aButtonGroup.add(logNoneRadioButton);
        topPanel.add(logNoneRadioButton);
        logNoneRadioButton.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                changeLogLevel();
            }
        });
    }

    /**
     * Zmiana poziomu logów.
     */
    private void changeLogLevel()
    {
        if(logAllRadioButton.isSelected()) {
            appServer.setLogLevel(Server.LogLevel.LogAll);
        }
        else if(logUserRadioButton.isSelected()) {
            appServer.setLogLevel(Server.LogLevel.LogUser);
        }
        else if(logNoneRadioButton.isSelected()) {
            appServer.setLogLevel(Server.LogLevel.LogNone);
        }
    }

    /**
     * Uruchamia program.
     */
    public void run(){
        initializeComponents();

    }

    /**
     * Zmiana logu.
     */
    @Override
    public void logChanged(Event e) {
        logConsoleTextArea.setText(logConsoleTextArea.getText() + "\r\n" + appServer.getLog());
    }
}
