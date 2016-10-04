package model.Server;

import model.Common.Keys;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Armand on 22/09/2016.
 */
public class ThreadClient extends Thread{

    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public Server server;
    public int id;
    private String pseudo;

    private Keys keys;



    public ThreadClient(Socket socket, Server server, int id) {
        this.id = id;
        this.server = server;
        this.socket = socket;


    }

    private boolean establishRSAkey() {
        return false;
    }

    @Override
    public void run() {


    }
}
