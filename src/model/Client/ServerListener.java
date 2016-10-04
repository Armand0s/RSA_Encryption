package model.Client;

import java.io.IOException;

/**
 * Created by Armand on 27/09/2016.
 */
public class ServerListener extends Thread{

    private Client client;

    public ServerListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {

        while(true) {
            String message;
            try {
                try {
                     message = (String) client.in.readObject();
                } catch (IOException e) {
                    System.err.println("Server closed connection");
                    message = "";
                }

                System.out.println(message);
                System.out.println("> ");
            } catch (ClassNotFoundException e) {

            }
        }

    }
}
