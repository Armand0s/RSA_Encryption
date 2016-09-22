package model;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Armand on 22/09/2016.
 */
public class ConnectedClient {

    private int idClient;
    private String pseudo;

    public Server server;
    private Socket socket;

    private InputStream input;
    private OutputStream output;

    public ConnectedClient(int idClient, String pseudo, Server server) {
        this.pseudo = pseudo;
        this.idClient = idClient;
        this.server = server;
    }

}
