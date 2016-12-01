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
        RSA rsa = new RSA(new BigInteger("5915587277"), new BigInteger("1500450271"));
        RSAKeys keys = rsa.getRSAKeys();
        Assert.assertTrue(keys.getPublicKey().getE().compareTo(keys.getPublicKey().getN()) < 0);
        Assert.assertEquals(new BigInteger("8876044532898802067"), keys.getPublicKey().getN());

        Assert.assertEquals(new BigInteger("8876044532898802067"), keys.getPrivateKey().getN());
        Assert.assertEquals(new BigInteger("1"), (keys.getPrivateKey().getD().multiply(rsa.getE())).mod(rsa.getPhi()));

        rsa = new RSA(1024);
        keys = rsa.getRSAKeys();
        Assert.assertTrue(keys.getPublicKey().getE().compareTo(keys.getPublicKey().getN()) < 0);
        Assert.assertEquals(rsa.getP().multiply(rsa.getQ()), keys.getPublicKey().getN());
        Assert.assertEquals(rsa.getP().subtract(BigInteger.ONE).multiply(rsa.getQ().subtract(BigInteger.ONE)), rsa.getPhi());

        Assert.assertEquals(rsa.getP().multiply(rsa.getQ()), keys.getPrivateKey().getN());
        Assert.assertEquals(new BigInteger("1"), keys.getPrivateKey().getD().multiply(rsa.getE()).mod(rsa.getPhi()));
    }

    @Test
    public void testDecrypt() throws Exception {
        /*for(int i = 0; i < 100000; i++)
        {
            RSA rsa = new RSA(17);
            RSAKeys keys = rsa.getRSAKeys();
            byte[] raw = {1, -128};
            BigInteger bi = new BigInteger(raw);
            System.out.println(bi.toString());
            byte[] encrypted = RSA.encrypt(raw, keys.getPublicKey());
            byte[] decrypted = RSA.decrypt(encrypted, keys.getPrivateKey());
            Assert.assertArrayEquals(raw, decrypted);
        }*/

        RSA rsa = new RSA(new BigInteger("11"), new BigInteger("17"));
        RSAKeys keys = rsa.getRSAKeys();
        System.out.println(rsa.toString());
        byte[] raw = new byte[]{1, 0};
        byte[] encrypted = RSA.encrypt(raw, keys.getPublicKey());
        for (int i = 0; i < encrypted.length; i++) {
            System.out.println(encrypted[i]);
        }
        byte[] decrypted = RSA.decrypt(encrypted, keys.getPrivateKey());
        for (int i = 0; i < decrypted.length; i++) {
            System.out.println(decrypted[i]);
        }
        Assert.assertArrayEquals(raw, decrypted);
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