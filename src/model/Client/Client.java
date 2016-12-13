package model.Client;

import main.main;
import model.Common.*;
import ui.View;
import ui.ViewAbstract;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

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
    private ServerListener serverListener;
    private View vue;

    public Client(String ipserver, int port, String pseudo, View vue) {
        this.ipserver = ipserver;
        this.port = port;
        this.pseudo = pseudo;
        RSAKeysLocal = new RSA(1024).getRSAKeys();
        RSAKeys = new RSAKeys();
        this.vue = vue;
        //System.out.println(RSAKeysLocal);
        run();
    }

    private void run() {

        Scanner scan = new Scanner(System.in);
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
        {
            serverListener = new ServerListener(this);
            serverListener.start();
            while (true) {
                String str = scan.nextLine();
                sendMessage(str);
            }
        }
    }

    public void stop() {
        serverListener.interrupt();
        try {
            serverListener.join();
            socket.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean initClient() {
        try {
            socket = new Socket(ipserver, port);
        } catch (IOException e) {
            System.err.println("Connecting to server....... FAILED "+ipserver+"/"+port);
            return false;
        }
        System.out.println("Connecting to server..... OK " + ipserver + "/" + port);

        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("Creating socket stream..... FAILED");
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
            System.err.println("Receiving public key of server........ FAILED (IOException)");
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Receiving public key of server........ FAILED (ClassNotFoundException)");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAPublicKey){
            RSAPublicKeyOfServer = (RSAPublicKey) messageType.getData();
            System.out.println("Receiving public key of server........ OK");
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
            System.err.println("Sending local key to server........... FAILED");
            return false;
        }
        System.out.println("Sending local key to server........... OK");
        return true;
    }


    private boolean receiveFinalKeysFromServer() {
        MessageType messageType;
        try {
            byte[] byteToReceiveDecrypted = RSA.ReceiveAndDecrypt(in,RSAKeysLocal.getPrivateKey());
            messageType = (MessageType) SerializableUtils.convertFromBytes(byteToReceiveDecrypted);
        } catch (IOException e) {
            System.err.println("Receiving final keys............. FAILED (IOException)");
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Receiving final keys............. FAILED (ClassNotFoundException)");
            return false;
        }
        if (messageType.getType() == MessageType.Type.RSAKeys){
            RSAKeys = (RSAKeys) messageType.getData();
        }
        System.out.println("Receiving final keys............. OK");
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
            System.err.println("Sending pseudo to Server.............. FAILED");
            return false;
        }
        System.out.println("Sending pseudo to Server.............. OK");

    return true;

    }

    private boolean sendMessage(String message) {
        MessageType messageType = new MessageType();
        messageType.setType(MessageType.Type.Message);
        messageType.setData(message);
        try {
            RSAPublicKey rsaPrivateKey = RSAKeys.getPublicKey();
            byte[] sendTmp = SerializableUtils.convertToBytes(messageType);
            RSA.EncryptAndSend(sendTmp,out,rsaPrivateKey);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void receiveMessage(String message)
    {
        vue.addTextToLog(message);
    }
}
