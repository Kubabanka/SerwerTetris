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
        int serverPort = 8085;
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
    public void run() {
        runServer();
    }

    public void setLogLevel(LogLevel logLevel)
    {
        this.logLevel = logLevel;
    }
    public LogLevel getLogLevel()
    {
        return logLevel;
    }
    public void addLogChangedListener(LogChangedListener listener)
    {
        logChangedListeners.add(LogChangedListener.class, listener);
    }
    public void removeLogChangedListener(LogChangedListener listener)
    {
        logChangedListeners.remove(LogChangedListener.class, listener);
    }
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
    public String getLog()
    {
        return log;
    }
    public void setLog(String l)
    {
        log = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\t" + l;
        fireLogChanged(null);
    }
}