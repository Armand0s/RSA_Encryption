/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.Client.Client;
import model.Server.Server;
import ui.View;

/**
 *
 * @author Nicolas
 */
public class Model {
    private boolean isConnecting;
    Server server;
    Client client;
    
    public Model(){
        isConnecting = false;
    }
    
    public boolean IsConnecting(){
        return isConnecting;
    }
    
    public void connect(String ip, int port, String name, View view){
        setClient(new Client(ip, port, name, view));
        isConnecting = true;
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Server getServer()
    {
        return server;
    }

    public Client getClient()
    {
        return client;
    }
}
