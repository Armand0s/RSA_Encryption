package model.Server;

import main.main;
import model.Common.RSA;
import model.Common.RSAKeys;
import sun.awt.Mutex;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * @author Armand Souchon
 */
public class Server {

    public static final int RSA_BYTE_LENGTH = 1024;

    private Logger logfile = main.logger;

    private ServerSocket serverSocket;
    private int port;

    private int numNewClient = 1;
    private ArrayList<ThreadClient> clients;
    private RSAKeys clientsRSAKeys;

    private boolean running = true;

    private Mutex mutexMap;

    public RSAKeys serverKeys;


    public Server(int port) {
        this.port = port;
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException ioException) {
            logfile.severe("Unable to create the server, exiting");
        }
        mutexMap = new Mutex();

        serverKeys = new RSA(RSA_BYTE_LENGTH).getRSAKeys();

        run();
    }

    private void run() {
        logfile.info("Server created. IP :" +this.serverSocket.getInetAddress().getHostAddress()+":" + this.serverSocket.getLocalPort());
        logfile.info("Waiting a client......");
        while(running) {

            // Create the socket for the next client
            Socket newClientSocket;
            // Waiting for client
            try {
                newClientSocket = serverSocket.accept();
                logfile.info("New client connected");
            } catch (IOException ioException) {
                newClientSocket = null;
                logfile.severe("Unable to accept client on server, exiting");
            }
            // A new client is arrived, add this client to the list
            ThreadClient newClient = new ThreadClient(newClientSocket, this, ++numNewClient);
            mutexMap.lock();
                clients.add(newClient);
            mutexMap.unlock();
            // out when running raised : server will be closed
        }

        // close server and each client thread
        try {
            serverSocket.close();
            for(int i=0;i<clients.size();i++) {
                ThreadClient workinglient = clients.get(i);
                workinglient.in.close();
                workinglient.out.close();
                workinglient.socket.close();
            }
        } catch (IOException e) {
            logfile.severe("Unable to close the server");
        }
    }

    private synchronized void disconnectClient(int id) {
        // scan the array list until we found the Id
        for(int i = 0; i < clients.size(); ++i) {
            ThreadClient ct = clients.get(i);
            if(ct.id == id) {
                clients.remove(i);
                return;
            }
        }
    }

    private void stop() {
        running = false;
    }

    public boolean broadcast(String message) {
        for (int i=0;i<clients.size();i++) {
            if (sendMessage(message,clients.get(i))) {
                return false;
            }
        }
        return true;
    }
    // TODO
    private boolean sendMessage(String message,ThreadClient client) {
        return false;
    }

    public RSAKeys getOrCreateKeyForClients() {
        if (clients.size() == 1) { // room was empty, create new key
            clientsRSAKeys = new RSA(1024).getRSAKeys();
        }

        return clientsRSAKeys;
    }


}
