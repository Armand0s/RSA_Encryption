package controller;

import main.main;
import model.Common.RSAKeys;
import model.Common.RSA;

import java.io.*;

/**
 * Created by Armand on 28/09/2016.
 */
public class KeySaver {

    private RSAKeys RSAKeys;
    private File keyFile;

    public KeySaver() {
    }

    public  boolean checkFile() {
        keyFile = new File(System.getProperty("user.dir") + "data/keyserver.txt");
        if (keyFile.exists() && keyFile.isFile()) {
            RSAKeys = new RSAKeys();
            if (!readKeysFromFile())
                return false;
        } else if (keyFile.exists() && keyFile.isDirectory()) {
            main.logger.severe("The file where RSAKeys are saved is now a directory ! Unable to continue");
            main.logger.severe("Delete or move the directory path : " + System.getProperty("user.dir") + "data/keyserver.txt");
            return false;
        } else {
            if (!createNewKeyFile())
                return false;
        }
        return true;
    }


    private boolean readKeysFromFile() {

        return false;
    }

    private boolean createNewKeyFile() {
        try {
            keyFile.createNewFile();
        } catch (IOException e) {
            main.logger.severe("Unable to create a new keyFileat path :" + System.getProperty("user.dir") + "data/keyserver.txt");
            return false;
        }
        RSA rsa = new RSA(2048);
        RSAKeys.setPrivateKey(rsa.getRSAKeys().getPrivateKey());
        RSAKeys.setPublicKey(rsa.getRSAKeys().getPublicKey());
        try {
            try {
                PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
                writer.println(RSAKeys.getPrivateKey().getD().toString());
                writer.println(RSAKeys.getPrivateKey().getN().toString());
                writer.println(RSAKeys.getPublicKey().getE().toString());
                writer.println(RSAKeys.getPublicKey().getN().toString());
                writer.close();
            } catch (UnsupportedEncodingException e1) {
                main.logger.severe("Unable to encode character in keyFile file");
            }
        } catch (FileNotFoundException e) {} // already checked

        return true;
    }
}
