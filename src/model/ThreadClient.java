package model;

/**
 * Created by Armand on 22/09/2016.
 */
public class ThreadClient extends Thread{

    private Server server;
    private ConnectedClient connectedClient;
    private int idClient;



    public ThreadClient(ConnectedClient connectedClient,int idClient) {
        this.idClient = idClient;
        this.connectedClient = connectedClient;
        this.server = connectedClient.server;
    }

    @Override
    public void run() {

    }
}
