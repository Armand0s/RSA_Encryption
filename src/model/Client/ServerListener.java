package model.Client;

import model.Common.MessageType;
import model.Common.RSA;
import model.Common.SerializableUtils;

import java.io.IOException;

/**
 * @author Armand Souchon
 */
public class ServerListener extends Thread{

    private Client client;

    public ServerListener(Client client) {
        this.client = client;
    }

    @Override
    public void run() {

        while(true) {
            byte[] byteMessage;
            MessageType messageType;
            try {
                int sizeToReceive = client.in.readInt();
                byteMessage = new byte[sizeToReceive];
                client.in.read(byteMessage);

                messageType = (MessageType) RSA.decryptObject(byteMessage,client.RSAKeys.getPrivateKey());

            } catch (IOException e) {
                System.err.println("Server closed connection");
                messageType = null;
                break;
            } catch (ClassNotFoundException e) {
                System.err.println("Unknown object received by server");
                messageType = null;
                break;
            }
            if (messageType.getType() == MessageType.Type.Message) {
                System.out.println((String) messageType.getData());
                System.out.println("> ");
            }

        }

    }
}
