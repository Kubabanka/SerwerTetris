import javax.swing.event.EventListenerList;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EventListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    public enum LogLevel{LogAll, LogUser, LogNone}
    private String log = "";
    private EventListenerList logChangedListeners = new EventListenerList();
    private LogLevel logLevel;
    private ServerSocket serverSocket;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    public Server()
    {
        logLevel = LogLevel.LogAll;
    }

    private void runServer() {
        int serverPort = 8087;
        try {
            setLog("Starting Server");
            serverSocket = new ServerSocket(serverPort);

            while(true) {
                if(logLevel == LogLevel.LogAll)
                    setLog("Waiting for request");
                try {
                    Socket s = serverSocket.accept();
                    if(logLevel != LogLevel.LogNone)
                        setLog("Client connected:\t" + s.toString());
                    executorService.submit(new ServiceRequest(s,logLevel,this));
                } catch(IOException ioe) {
                    if(logLevel == LogLevel.LogAll)
                        setLog("Error accepting connection");
                    ioe.printStackTrace();
                }
            }
        }catch(IOException e) {
            setLog("Error starting Server on " + serverPort);
            e.printStackTrace();
        }
    }

    public void stopServer() {
        executorService.shutdownNow();
        try {
            setLog("Shutting down server");
            serverSocket.close();
        } catch (IOException e) {
            setLog("Error in server shutdown");
            e.printStackTrace();
        }
        System.exit(0);
    }

    @Override
    /**
     * Uruchamia serwer.
     */
    public void run() {
        runServer();
    }

    /**
     * Ustawia, jaki typ logów bêdzie zapisywane w konsoli (filtruje)
     * @param logLevel wszystko, nic lub u¿ytkownik (informacja o po³¹czeniu u¿ytkowników)
     */
    public void setLogLevel(LogLevel logLevel)
    {
        this.logLevel = logLevel;
    }

    /**
     * Pobiera informacjê o aktualnie wybranej filtracji.
     */
    public LogLevel getLogLevel()
    {
        return logLevel;
    }

    /**
     * Funkcja s³u¿¹ca do dodawania s³uchacza zdarzenia.
     * @param listener S³uchacz zdarzenia.
     */
    public void addLogChangedListener(LogChangedListener listener)
    {
        logChangedListeners.add(LogChangedListener.class, listener);
    }

    /**
     * Funkcja s³u¿¹ca do usuwania s³uchacza zdarzenia.
     * @param listener S³uchacz zdarzenia.
     */
    public void removeLogChangedListener(LogChangedListener listener)
    {
        logChangedListeners.remove(LogChangedListener.class, listener);
    }

    /**
     * Funkcja powiadamiaj¹ca obiekty s³uchaczy, ¿e zmieni³ siê typ filtracji
     * @param e Zdarzenie.
     */
    protected void fireLogChanged(Event e)
    {
        Object[] listeners = logChangedListeners.getListenerList();
        int numListeners = listeners.length;
        for (int i = 0; i<numListeners; i+=2)
        {
            if (listeners[i]==LogChangedListener.class)
            {
                ((LogChangedListener)listeners[i+1]).logChanged(e);
            }
        }
    }

    /**
     * Funkcja pobieraj¹ca zawartoœæ logu.
     * @return Zawartoœæ logu.
     */
    public String getLog()
    {
        return log;
    }

    /**
     * Funkcja ustawiaj¹ca zawartoœæ logu.
     * Sk³ada siê na ni¹ data otrzymania logu oraz jego treœæ.
     * @param l Zawartoœæ logu.
     */
    public void setLog(String l)
    {
        log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\t" + l;
        fireLogChanged(null);
    }
}