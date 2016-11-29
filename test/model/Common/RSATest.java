package model.Common;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Created by Nicolas on 22/11/2016.
 */
public class RSATest {

    @Test
    public void testGetRSAKeys() throws Exception {
        RSA rsa = new RSA(new BigInteger("5"), new BigInteger("37"));
        RSAKeys keys = rsa.getRSAKeys();
        Assert.assertTrue(keys.getPublicKey().getE().compareTo(keys.getPublicKey().getN()) < 0);
        Assert.assertEquals(new BigInteger("185"), keys.getPublicKey().getN());

        Assert.assertEquals(new BigInteger("185"), keys.getPrivateKey().getN());
        Assert.assertEquals(new BigInteger("1"), keys.getPrivateKey().getD().multiply(rsa.getE()).mod(rsa.getPhi()));

        rsa = new RSA(1024);
        keys = rsa.getRSAKeys();
        Assert.assertTrue(keys.getPublicKey().getE().compareTo(keys.getPublicKey().getN()) < 0);
        Assert.assertEquals(rsa.getP().multiply(rsa.getQ()), keys.getPublicKey().getN());

        Assert.assertEquals(rsa.getP().multiply(rsa.getQ()), keys.getPrivateKey().getN());
        Assert.assertEquals(new BigInteger("1"), keys.getPrivateKey().getD().multiply(rsa.getE()).mod(rsa.getPhi()));
    }

    @Test
    public void testDecrypt() throws Exception {
        RSA rsa = new RSA(new BigInteger("23"), new BigInteger("13"));
        RSAKeys keys = rsa.getRSAKeys();
        byte[] array = {0, 1, 2, 3 ,4 , 5, 6, 7, 8, 9, 10};
        byte[] encrypted = RSA.encrypt(array, keys.getPublicKey());
        byte[] decrypted = RSA.decrypt(encrypted, keys.getPrivateKey());
        Assert.assertArrayEquals(array, decrypted);
    }

    @Test
    public void testEncrypt() throws Exception {

    }

    @Test
    public void testDecryptObject() throws Exception {

    }

    @Test
    public void testEncryptObject() throws Exception {

    }
}