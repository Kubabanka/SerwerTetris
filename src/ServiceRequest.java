import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServiceRequest implements Runnable {

    private Socket socket;
    private Server.LogLevel logLevel;
    private Server appServer;
    public ServiceRequest(Socket connection, Server.LogLevel logLevel, Server appServer) {
        this.socket = connection;
        this.logLevel = logLevel;
        this.appServer = appServer;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String inCommand = in.readLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            if(!inCommand.isEmpty()) {
                if(logLevel == Server.LogLevel.LogAll)
                    appServer.setLog(inCommand);
            }
        }
        catch (Exception ex)
        {
            if(logLevel == Server.LogLevel.LogAll)
                appServer.setLog("Error read/write");
        }
   
        try {
            socket.close();
        }catch(IOException ioe) {
            if(logLevel == Server.LogLevel.LogAll)
                appServer.setLog("Error closing client connection");
        }
    }
}