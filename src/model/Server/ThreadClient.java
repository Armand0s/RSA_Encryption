package model.Server;

import main.main;
import model.Common.*;

import java.io.*;
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

    public RSAPublicKey RSAClientLocalKey;
    //private MessageType message;

    private boolean running = true;



    public ThreadClient(Socket socket, Server server, int id) {
        this.id = id;
        this.server = server;
        this.socket = socket;

        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            main.logger.severe("Client " + id + " : Creating IO Stream........ FAILED");
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
        try {
            byteToSend = SerializableUtils.convertToBytes(messageType);
            out.writeInt(byteToSend.length);
            out.write(byteToSend);
            out.flush();
        } catch (IOException e) {
            main.logger.severe("Client " + id + " : Sending Public Key of Server......... FAILED");
        }

        main.logger.info("Client " + id + " : Sending Public Key of Server............. OK");
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
            main.logger.severe("Client " + id + " : Receiving Public Key of Client......... FAILED (IOException)");
            return false;
        } catch (ClassNotFoundException e) {
            main.logger.severe("Client " + id + " : Sending Public Key of Server......... FAILED (ClassNotFoundException");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAPublicKey){
            RSAClientLocalKey =(RSAPublicKey) messageType.getData();
            main.logger.info("Client " + id + " : Receiving Public Key of Client........... OK");
            return true;
        }
        main.logger.severe("Client " + id + " : Sending Public Key of Server......... FAILED (Unknown Error)");
        return false;

    }


    private boolean sendFinalRSAkeysToClient() {
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.RSAKeys);
        messageType.setData(server.clientsRSAKeys);

        try {
            RSA.EncryptAndSend(SerializableUtils.convertToBytes(messageType),out,RSAClientLocalKey);
        } catch (IOException e) {
            main.logger.severe("Client " + id + " : Sending Final Keys to Client........ FAILED");
        }
        main.logger.info("Client " + id + " : Sending Final Keys to Client............. OK");
        return true;
    }

    private boolean receivePseudoFromClient() {
        MessageType messageType;
        try {
            int sizeToReceive = in.readInt();
            byte[] arraybyte = new byte[sizeToReceive];
            in.read(arraybyte);
            messageType = (MessageType) RSA.decryptObject(arraybyte,server.getOrCreateKeyForClients().getPrivateKey());
        } catch (IOException e) {
            main.logger.severe("Client " + id + " : Receiving pseudo of client....... FAILED (IOException)");
            return false;
        } catch (ClassNotFoundException e) {
            main.logger.severe("Client " + id + " : Receiving pseudo of client....... FAILED (ClassNotFoundException)");
            return false;
        }
        if (messageType.getType() == MessageType.Type.Pseudo){
            this.pseudo = (String) messageType.getData();
        }
        main.logger.info("Client " + id + " : Receiving pseudo of client............... OK (pseudo = " + pseudo + ")");
        return true;
    }

    @Override
    public void run() {

        while (running) {
            MessageType messageType;
            try {

                byte[] receivedMessage = RSA.ReceiveAndDecrypt(in,server.getOrCreateKeyForClients().getPrivateKey());
                messageType = (MessageType) SerializableUtils.convertFromBytes(receivedMessage);

                server.analyseMessage(messageType,this);

            } catch (IOException e) {
                main.logger.severe("Client " + id + " : Unable to Receive Message from Client (IOException) " + e.getMessage());

                break;
            } catch (ClassNotFoundException e) {
                main.logger.severe("Client " + id + " : Unable to Receive Message from Client, Steam Corrupted (ClassNotFoundException)");
                break;
            }
        }
    }
}
