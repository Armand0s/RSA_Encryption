/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.Client.Client;
import model.Server.Server;

/**
 *
 * @author Nicolas
 */
public class Model {
    private boolean isConnecting;
    
    public Model(Server server, Client client){
        isConnecting = false;
    }
    
    public boolean IsConnecting(){
        return isConnecting;
    }
    
    public void connect(String ip){
        
        isConnecting = true;
    }
}
