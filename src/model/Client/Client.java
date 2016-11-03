package model.Client;

import main.main;
import model.Common.*;

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
    private RSAPublicKey RSAPublicKeyOfServer;
    private RSAKeys RSAKeysLocal;

    public Client(String ipserver,int port, String pseudo) {
        this.ipserver = ipserver;
        this.port = port;
        this.pseudo = pseudo;
        RSAKeysLocal = new RSA(1024).getRSAKeys();
        RSAKeys = new RSAKeys();
        run();
    }

    private void run() {

        boolean resInit;
        resInit = initClient();

        boolean resReceivePublicKeyOfServer;
        resReceivePublicKeyOfServer = receiveRSAPublicKeyOfServer();

        boolean resSendKey;
        resSendKey = sendRSAPublickeyToServer();

        boolean resReceiveFinalKeys;
        resReceiveFinalKeys = receiveFinalKeysFromServer();

        if (resInit && resReceivePublicKeyOfServer && resSendKey && resReceiveFinalKeys)
            new ServerListener(this).start();

    }




    private boolean initClient() {
        try {
            socket = new Socket(ipserver, port);
        } catch (IOException e) {
            System.err.println("Unable to connect to server IP:"+ipserver+"/"+port);
            return false;
        }
        System.out.println("Connected to server " + ipserver + "/" + port);

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Unable to connect socket streams");
            return false;
        }

        return true;
    }

    private boolean receiveRSAPublicKeyOfServer() {
        MessageType messageType;
        boolean publickeyReceived = false;
        while (!publickeyReceived) {
            try {
                /*
                byte[] arraybyte = SerializableUtils.readUnknownByteArrayLenght(in);
                messageType = (MessageType) SerializableUtils.convertFromBytes(arraybyte);
                */
                int sizeToReceive = in.readInt();
                byte[] arraybyte = new byte[sizeToReceive];
                in.read(arraybyte);
                messageType = (MessageType) SerializableUtils.convertFromBytes(arraybyte);
                System.out.println("Public Key of Server received  OK");

            } catch (IOException e) {
                System.err.println("Unable to get Key from client");
                return false;
            } catch (ClassNotFoundException e) {
                System.err.println("Received object not recognized from client");
                return false;
            }
            if (messageType.getType() == MessageType.Type.RSAPublicKey){
                RSAPublicKeyOfServer = (RSAPublicKey) messageType.getData();
                publickeyReceived = true;
            }
        }
        return true;
    }


    private boolean sendRSAPublickeyToServer() {
        // Public key sent to the server
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.RSAPublicKey);
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


    private boolean receiveFinalKeysFromServer() {
        MessageType messageType;
        boolean keysReceived = false;
        while (!keysReceived) {
            try {

                messageType = (MessageType) in.readObject();



            } catch (IOException e) {
                System.err.println("Unable to get Key from client");
                return false;
            } catch (ClassNotFoundException e) {
                System.err.println("Received object not recognized from client");
                return false;
            }
            if (messageType.getType() == MessageType.Type.RSAKeys){
                RSAKeys = (RSAKeys) messageType.getData();
                keysReceived = true;
            }
        }
        return true;
    }

    // DONE
    private boolean sendPseudoToServer() throws IOException{
        byte[] messageTypeByteArray;
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.Pseudo);
        messageType.setData(pseudo);
        try {
            messageTypeByteArray = SerializableUtils.convertToBytes(messageType);

            out.write(messageTypeByteArray);
            out.flush();
        } catch (IOException e) {
            System.err.println("Unable to send Pseudo to server");
            return false;
        }

    return true;

    }

}
