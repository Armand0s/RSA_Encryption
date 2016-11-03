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
    private String pseudo;

    public RSAKeys RSAKeys;
    private MessageType message;

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
        try {
            int sizeToReceive = in.readInt();
            byte[] arraybyte = new byte[sizeToReceive];
            in.read(arraybyte);
            message = (MessageType) SerializableUtils.convertFromBytes(arraybyte);
        } catch (IOException e) {
            main.logger.severe("Unable to get Public Key of client");
            return false;
        } catch (ClassNotFoundException e) {
            main.logger.severe("Received object not recognized from client");
            return false;
        }
        if (message.getType() == MessageType.Type.RSAKeys){
            RSAKeys.setPublicKey((RSAPublicKey) message.getData());
        }
        System.out.println(message.getType());
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
