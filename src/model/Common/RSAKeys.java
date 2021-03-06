package model.Common;

import java.io.Serializable;

/**
 * @author Armand Souchon
 */
public class RSAKeys implements Serializable{

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    public RSAKeys(RSAPrivateKey privateKey, RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public RSAKeys() {
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "RSAKeys{\n" +
                "privateKey\n" + privateKey +
                "\npublicKey\n" + publicKey;
    }
}
