import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Ba�ka on 2015-06-13.
 */

public class Server {

    private static Server server;
    private ServerSocket serverSocket;

    /**
     * This executor service has 10 threads.
     * So it means your server can process max 10 concurrent requests.
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.runServer();
    }

    private void runServer() {
        int serverPort = 8085;
        try {
            System.out.println("Starting Server");
            serverSocket = new ServerSocket(serverPort);

            while(true) {
                System.out.println("Waiting for request");
                try {
                    Socket s = serverSocket.accept();
                    System.out.println("Processing request");
                    executorService.submit(new ServiceRequest(s));
                } catch(IOException ioe) {
                    System.out.println("Error accepting connection");
                    ioe.printStackTrace();
                }
            }
        }catch(IOException e) {
            System.out.println("Error starting Server on "+serverPort);
            e.printStackTrace();
        }
    }

    //Call the method when you want to stop your server
    private void stopServer() {
        //Stop the executor service.
        executorService.shutdownNow();
        try {
            //Stop accepting requests.
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error in server shutdown");
            e.printStackTrace();
        }
        System.exit(0);
    }

    class ServiceRequest implements Runnable {

        private Socket socket;

        public ServiceRequest(Socket connection) {
            this.socket = connection;
        }

        public void run() {

            //Do your logic here. You have the `socket` available to read/write data.

            //Make sure to close
            try {
                socket.close();
            }catch(IOException ioe) {
                System.out.println("Error closing client connection");
            }
        }
    }
}