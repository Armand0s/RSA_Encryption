package model.Client;

import model.Common.RSAKeys;
import model.Common.MessageType;
import model.Common.RSA;

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

    private RSAKeys RSAKeys;
    private RSAKeys tmpRSAKeys;

    public Client(String ipserver,int port, String pseudo) {
        this.ipserver = ipserver;
        this.port = port;
        this.pseudo = pseudo;
        RSAKeys = new RSA(1024).getRSAKeys();
        run();
    }

    private boolean run() {

        boolean resInit;
        resInit = initClient();

        boolean resSendKey;
        resSendKey = sendRSAPublickeyToServer();


        return resInit && resSendKey;
    }




    private boolean initClient() {
        try {
            socket = new Socket(ipserver, port);
        } catch (IOException e) {
            System.err.println("Unable to connect to server IP:"+ipserver+"/"+port);
            return false;
        }
        System.out.println("Connected to server IP:" + ipserver + "/" + port);

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Unable to connect socket streams");
            return false;
        }

        new ServerListener(this).start();
        return true;
    }


    private boolean sendRSAPublickeyToServer() {
        // Public key sent to the server
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.SendKey);
        messageType.setData(RSAKeys.getPublicKey());
        try {
            out.writeObject(messageType);
            out.flush();
        } catch (IOException e) {
            System.err.println("Can't send the public key");
            return false;
        }
        return true;
    }

}
