import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServiceRequest implements Runnable {

    /**
     * Model gniazda s�u��cy do obs�ugi ruchu internetowego.
     */
    private Socket socket;

    /**
     * Kt�re dane b�d� przechowywane w logu (wszystko, nic, u�ytkownik)
     */
    private Server.LogLevel logLevel;

    /**
     * Okre�lenie jednego z punkt�w ko�cowych (do czego ma si� pod��czy� klient).
     */
    private Server appServer;

    /**
     * Zbi�r danych.
     */
    private Data aData = new Data();

    /**
     * Obs�uguje ��danie.
     * @param connection po��czenie
     * @param logLevel okre�lenie danych przechowywanych w logu
     * @param appServer okre�la, do czego ma po��czy� si� klient
     */
    public ServiceRequest(Socket connection, Server.LogLevel logLevel, Server appServer) {
        this.socket = connection;
        this.logLevel = logLevel;
        this.appServer = appServer;
    }

    /**
     * Uruchamia serwer i zajmuje si� komunikacj� pomi�dzy punktami ko�cowymi.
     */
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
                        out.printf("%s", "Level "+splitted[1]+" " + aData.levels[Integer.parseInt(splitted[1])-1]);
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

}