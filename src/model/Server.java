package model;

import main.main;
import sun.awt.Mutex;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Armand on 22/09/2016.
 */
public class Server {

    private Logger logfile = main.logger;
    private int port;
    private ServerSocket serverSocket;

    private int nbClient = 0;
    private int numNewClient = 1;

    private Map<Integer,ConnectedClient> clients;
    private Mutex mutexMap;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            logfile.severe("Unable to create the server, exiting");
        }
        mutexMap = new Mutex();

        waitClient();
    }

    private void waitClient() {
        logfile.info("Server created. IP :" +this.serverSocket.getLocalSocketAddress()+":" + this.serverSocket.getLocalPort());
        logfile.info("Waiting a client......");
        while(true) {

            // Create a new client and wait until a new one connect
            ConnectedClient client = new ConnectedClient(this.numNewClient,"",this);
            try {
                serverSocket.accept();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                logfile.severe("Unable to accept client on server, exiting");
            }

            // A new client is connected, add this client to the map
            mutexMap.lock();
                clients.put(numNewClient,client);



        }
    }
}
