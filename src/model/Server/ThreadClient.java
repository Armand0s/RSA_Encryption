package model.Server;

import main.main;
import model.Client.MessageType;
import model.Common.Keys;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Armand Souchon
 */
public class ThreadClient extends Thread{

    public Socket socket;
    public ObjectInputStream in;
    public ObjectOutputStream out;

    public Server server;
    public int id;
    private String pseudo;

    private Keys keys;
    private MessageType message;

    private boolean running = true;



    public ThreadClient(Socket socket, Server server, int id) {
        this.id = id;
        this.server = server;
        this.socket = socket;

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            main.logger.severe("Unable to create IO stream for client");
            return;
        }

    }

    private boolean establishRSAkey() {
        return false;
    }

    @Override
    public void run() {

        while (running) {
            try {
                message = (MessageType) in.readObject();
            } catch (IOException e) {
                main.logger.severe("Unable to get Object from client");
                break;
            } catch (ClassNotFoundException e) {
                main.logger.severe("Received object not recognized");
                break;
            }




        }




    }
}
