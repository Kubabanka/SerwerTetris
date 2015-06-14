import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServiceRequest implements Runnable {

    private Socket socket;
    private Server.LogLevel logLevel;
    private Server appServer;
    private Data aData = new Data();
    public ServiceRequest(Socket connection, Server.LogLevel logLevel, Server appServer) {
        this.socket = connection;
        this.logLevel = logLevel;
        this.appServer = appServer;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String received = in.readLine();
            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            if(!received.isEmpty()) {
                if(logLevel == Server.LogLevel.LogAll)
                    appServer.setLog(received);
                String[] splitted = received.split(" ",2);
                String request = splitted[0];
                switch (request)
                {
                    case ("HighScoresRequest"):
                        out.printf("%s","HighScoresReply " + aData.HighScoreToString());
                        break;
                    case ("NewScoreRequest"):
                        String [] var = splitted[1].split(" ");
                        out.printf("%s", "NewScoreReply " + aData.ChangeHighScore(Integer.parseInt(var[1]),var[0]));
                        break;
                    case ("LevelRequest"):
                        out.printf("%s", "Level " + aData.levels[Integer.parseInt(splitted[1])]);
                        break;
                    case ("DefaultSettingsRequest"):
                        out.printf("%s", "DefaultSettingsConfig " + aData.ConfigToString());
                        break;

                }


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

    public static void main(String[] args) {
        Data aData = new Data();
        String received = "NewScoreRequest Banka 900000";
        String[] splitted = received.split(" ",2);
        String request = splitted[0];
        switch (request)
        {
            case ("HighScoresRequest"):
                System.out.printf("%s", "HighScoresReply " + aData.HighScoreToString());
                break;
            case ("NewScoreRequest"):
                String [] var = splitted[1].split(" ");
                System.out.printf("%s", "NewScoreReply " + aData.ChangeHighScore(Integer.parseInt(var[1]),var[0]));
                break;
            case ("LevelRequest"):
                System.out.printf("%s", "Level " + aData.levels[Integer.parseInt(splitted[1])]);
                break;
            case ("DefaultSettingsRequest"):
                System.out.printf("%s", "DefaultSettingsConfig " + aData.ConfigToString());
                break;

        }
    }
}