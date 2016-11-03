package model.Server;

import main.main;
import model.Common.MessageType;
import model.Common.RSAKeys;
import model.Common.RSAPublicKey;
import model.Common.SerializableUtils;

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

    private RSAKeys RSAKeys;
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
            main.logger.severe("Error client " + id + " Unable to serialize/send public Key of server");
            return false;
        }

        return true;
    }

    private boolean receivePublicKeyOfClient() {
        boolean keyReceived = false;
        while (!keyReceived) {
            try {
                message = (MessageType) in.readObject();
            } catch (IOException e) {
                main.logger.severe("Unable to get Key from client");
                return false;
            } catch (ClassNotFoundException e) {
                main.logger.severe("Received object not recognized from client");
                return false;
            }
            if (message.getType() == MessageType.Type.RSAKeys){
                RSAKeys.setPublicKey((RSAPublicKey) message.getData());
                keyReceived = true;
            }
        }
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
