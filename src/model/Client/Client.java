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

    public RSAKeys RSAKeys;
    private RSAPublicKey RSAPublicKeyOfServer;
    private RSAKeys RSAKeysLocal;

    public Client(String ipserver,int port, String pseudo) {
        this.ipserver = ipserver;
        this.port = port;
        this.pseudo = pseudo;
        RSAKeysLocal = new RSA(1024).getRSAKeys();
        RSAKeys = new RSAKeys();
        System.out.println(RSAKeysLocal);
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

        boolean resSendPseudo;
        resSendPseudo = sendPseudoToServer();

        if (resInit && resReceivePublicKeyOfServer && resSendKey && resReceiveFinalKeys && resSendPseudo)
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
        try {
            int sizeToReceive = in.readInt();
            byte[] arraybyte = new byte[sizeToReceive];
            in.read(arraybyte);
            messageType = (MessageType) SerializableUtils.convertFromBytes(arraybyte);

        } catch (IOException e) {
            System.err.println("Unable to get Key from client");
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Received object not recognized from client");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAPublicKey){
            RSAPublicKeyOfServer = (RSAPublicKey) messageType.getData();
            System.out.println("Public Key of Server received  OK");
            return true;

        }
        return false;
    }


    private boolean sendRSAPublickeyToServer() {
        // Public key sent to the server
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.RSAPublicKey);
        messageType.setData(RSAKeysLocal.getPublicKey());

        byte[] byteToSend;
        try {
            byteToSend = SerializableUtils.convertToBytes(messageType);
            out.writeInt(byteToSend.length);
            out.write(byteToSend);
            out.flush();
        } catch (IOException e) {
            System.err.println("Can't send the public key");
            return false;
        }
        System.out.println("Local public key sent OK");
        return true;
    }


    private boolean receiveFinalKeysFromServer() {
        MessageType messageType;
        try {
            int sizeToReceive = in.readInt();
            byte[] byteToReceiveEncrypted = new byte[sizeToReceive];
            byte[] byteToReceiveDecrypted;
            in.read(byteToReceiveEncrypted);
            //messageType = (MessageType) RSA.decryptObject(byteToReceive,RSAKeysLocal.getPrivateKey());
            byteToReceiveDecrypted = RSA.decrypt(byteToReceiveEncrypted,RSAKeysLocal.getPrivateKey());
            messageType = (MessageType) SerializableUtils.convertFromBytes(byteToReceiveDecrypted);
        } catch (IOException e) {
            System.err.println("Unable to get Key from client");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Received object not recognized from client");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAKeys){
            RSAKeys = (RSAKeys) messageType.getData();
        }
        System.out.println("Final keys received OK");
        return true;
    }

    private boolean sendPseudoToServer() {
        byte[] messageTypeByteArrayEncrypted;

        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.Pseudo);
        messageType.setData(pseudo);
        try {
            messageTypeByteArrayEncrypted = RSA.encryptObject(messageType,RSAKeys.getPublicKey());
            out.writeInt(messageTypeByteArrayEncrypted.length);
            out.write(messageTypeByteArrayEncrypted);
            out.flush();
        } catch (IOException e) {
            System.err.println("Unable to send Pseudo to server");
            return false;
        }

    return true;

    }

    private boolean sendMessage(String message) {
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.Message);
        messageType.setData(message);
        try {
            byte[] buffer = SerializableUtils.convertToBytes(messageType);
            out.writeInt(buffer.length);
            out.write(buffer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
