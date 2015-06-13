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
 * Created by Ur on 2015-06-13.
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

    private void initializeComponents(){
        UIManager.getSystemLookAndFeelClassName();
        initializeMainFrame();
        initializeLogConsoleTextField();
        initializeAButtonGroup();
        initializeTopPanel();
        initializeLogAllRadioButton();
        initializeLogNoneRadioButton();
        initializeLogUserRadioButton();
        initializeResetLogConsole();
        appServer = new Server();
        Thread serverThread = new Thread(appServer);
        serverThread.start();

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

    private void initializeResetLogConsole() {
        resetLogConsoleButton = new JButton();
        resetLogConsoleButton.setText("Reset konsoli");
        topPanel.add(resetLogConsoleButton);
    }

    private void initializeTopPanel() {
        topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "wybierz"));
        mainFrame.add(topPanel, BorderLayout.NORTH);
    }

    private void initializeLogConsoleTextField() {
        logConsoleTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(logConsoleTextArea);
        scrollPane.setViewportView(logConsoleTextArea);
        logConsoleTextArea.setEditable(false);
        mainFrame.add(scrollPane, BorderLayout.CENTER);
    }


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

    private void initializeAButtonGroup(){
        aButtonGroup = new ButtonGroup();
    }

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

    public void run(){
        initializeComponents();

    }


    @Override
    public void logChanged(Event e) {
        logConsoleTextArea.setText(logConsoleTextArea.getText() + "\r\n" + appServer.getLog());
    }
}
