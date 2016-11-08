package model.Server;

import main.main;
import model.Common.*;

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
    public String pseudo;

    public RSAKeys RSAKeys;
    //private MessageType message;

    private boolean running = true;



    public ThreadClient(Socket socket, Server server, int id) {
        this.id = id;
        this.server = server;
        this.socket = socket;
        RSAKeys = new RSAKeys();

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            main.logger.severe("Unable to create IO stream for client");
            return;
        }

        sendPublicKeyOfServer();
        receivePublicKeyOfClient();
        sendFinalRSAkeysToClient();
        receivePseudoFromClient();

        this.start();

    }


    private boolean sendPublicKeyOfServer() {
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.RSAPublicKey);
        messageType.setData(server.serverKeys.getPublicKey());

        byte[] byteToSend;
        while (running) {
            try {
                byteToSend = SerializableUtils.convertToBytes(messageType);
                out.writeInt(byteToSend.length);
                out.write(byteToSend);
                out.flush();
            } catch (IOException e) {
                main.logger.severe("Unable to send Server Public Key to Client");
                break;
            }
        }
        main.logger.info("Client " + id + " : Public Key SENT");
        return true;
    }

    private boolean receivePublicKeyOfClient() {
        MessageType messageType;
        try {
            int sizeToReceive = in.readInt();
            byte[] arraybyte = new byte[sizeToReceive];
            in.read(arraybyte);
            messageType = (MessageType) SerializableUtils.convertFromBytes(arraybyte);
        } catch (IOException e) {
            main.logger.severe("Unable to get Public Key of client");
            return false;
        } catch (ClassNotFoundException e) {
            main.logger.severe("Received object not recognized from client");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAKeys){
            RSAKeys.setPublicKey((RSAPublicKey) messageType.getData());
        }
        main.logger.info("Client " + id + " : Client Public Key RECEIVED");
        return true;
    }


    private boolean sendFinalRSAkeysToClient() {
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.RSAKeys);
        messageType.setData(server.getOrCreateKeyForClients());

        byte[] byteToSend;

        try {
            byteToSend = SerializableUtils.convertToBytes(messageType);
            out.writeInt(byteToSend.length);
            out.write(byteToSend);
            out.flush();
        } catch (IOException e) {
            main.logger.severe("Unable to send Final Keys to Client");
        }

        main.logger.info("Client " + id + " : Final Keys SENT");
        return true;
    }

    private boolean receivePseudoFromClient() {
        MessageType messageType;
        try {
            int sizeToReceive = in.readInt();
            byte[] arraybyte = new byte[sizeToReceive];
            in.read(arraybyte);
            messageType = (MessageType) RSA.decryptObject(arraybyte,server.serverKeys.getPrivateKey());
        } catch (IOException e) {
            main.logger.severe("Unable to get Public Key of client");
            return false;
        } catch (ClassNotFoundException e) {
            main.logger.severe("Received object not recognized from client");
            return false;
        }
        if (messageType.getType() == MessageType.Type.Pseudo){
            this.pseudo = (String) messageType.getData();
        }
        main.logger.info("Client " + id + " : Pseudo RECEIVED");
        return true;
    }

    @Override
    public void run() {

        while (running) {
            int sizeToReceive;
            byte[] bytearray;
            MessageType messageType;
            try {

                sizeToReceive = in.readInt();
                bytearray = new byte[sizeToReceive];
                in.read(bytearray);
                messageType = (MessageType) RSA.decryptObject(bytearray,server.serverKeys.getPrivateKey());

                server.analyseMessage(messageType,this);

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
