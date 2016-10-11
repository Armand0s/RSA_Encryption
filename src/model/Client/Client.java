package model.Client;

import model.Common.Keys;

import java.io.*;
import java.net.Socket;

/**
 * @author Armand Souchon
 */
public class Client {

    private String ipserver;
    private int port;
    private String pseudo;

    private Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    private Keys keys;
    private Keys tmpKeys;

    public Client(String ipserver,int port, String pseudo) {
        this.ipserver = ipserver;
        this.port = port;
        this.pseudo = pseudo;
        run();
    }

    private boolean run() {
        try {
            socket = new Socket(ipserver, port);
        } catch (IOException e) {
            System.err.println("Unable to connect to server IP:"+ipserver+"/"+port);
            return false;
        }
        System.out.println("Connected to server IP:"+ipserver+"/"+port);

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Unable to connect socket streams");
            return false;
        }

        new ServerListener(this).start();

        /////// add RSA exchange here ///////


        return true;
    }
}
