/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Common;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author Armand Souchon
 */
public class RSA {

    public RSAKeys RSAKeys;

    // Secure random to get a prime number
    private final SecureRandom random = new SecureRandom();

    // p, q big prime numbers
    private BigInteger p; // p : first prime number
    private BigInteger q; // q : second prime number
    private BigInteger n; // p*q :
    private BigInteger e; // pgcd((p + quelque chose),n) = 1
    private BigInteger d; // (e*d) % phi = 1
    private BigInteger phi; // totient : (p-1)*(q-1)
    

    
    public RSA(int bitsLenght){
        RSAKeys = new RSAKeys();
        // get prime numbers
        do {
            p = BigInteger.probablePrime(bitsLenght, random);
            q = BigInteger.probablePrime(bitsLenght, random);
        } while (!((p.gcd(q)).equals(new BigInteger("1"))));
        createKeys();
    }
    
    public RSA(BigInteger p, BigInteger q) {
        RSAKeys = new RSAKeys();
        this.p = p;
        this.q = q;
        if (!((p.gcd(q)).equals(new BigInteger("1")))) {
            System.out.println("Caution, p and q are not prime each other !!!");
        }
        createKeys();
    }

    private void createKeys() {
        n = createN();
        phi = createPhi();
        e = createE();
        d = createD();


        RSAKeys.setPublicKey(new RSAPublicKey(n,e));
        RSAKeys.setPrivateKey(new RSAPrivateKey(n,d));
    }

    private BigInteger createN() {
        return p.multiply(q);
    }

    private BigInteger createE() {
        BigInteger tmpE = (p.compareTo(q) < 0) ? q.add(new BigInteger("1")) : p.add(new BigInteger("1")) ; // max (p,q) +1
        BigInteger resultGCD = BigInteger.ZERO;
        // while GCD != 1
        while (!(resultGCD.equals(new BigInteger("1")))) {
            resultGCD = tmpE.gcd(phi);

            tmpE = tmpE.add(BigInteger.ONE);
        }
        return tmpE.subtract(BigInteger.ONE);
    }
    
    private BigInteger createPhi() {
            return ((p.subtract(new BigInteger("1"))).multiply(q.subtract(new BigInteger("1"))));
    }

    private BigInteger createD() {

        /*BigInteger minPQ ;
        minPQ = (p.compareTo(q) < 0) ? p : q;
        BigInteger dTmp = ((p.add(q)).divide(new BigInteger("2"))).add(minPQ);
        BigInteger counter = new BigInteger("1");
        BigInteger resultPGDC;
        do {
            resultPGDC = ((minPQ.add(counter)).multiply(e)).mod(phi);
            counter = counter.add(new BigInteger("1"));
        } while (resultPGDC.equals(BigInteger.ONE));
        return minPQ.add(counter).subtract(new BigInteger("1"));*/
        return modInv(e,phi);
        //return e.modInverse(phi);
    }

    private BigInteger modInv(BigInteger _e,BigInteger _phi) {
        BigInteger[] u = new BigInteger[3];
        BigInteger[] v = new BigInteger[3];
        BigInteger q, temp1, temp2, temp3;

        u[0] = new BigInteger("1");
        u[1] = new BigInteger("0");
        u[2] = _phi;

        v[0] = new BigInteger("0");
        v[1] = new BigInteger("1");
        v[2] = _e;

        while(!v[2].equals(new BigInteger("0"))) {
            q = u[2].divide(v[2]);
            temp1 = u[0].subtract(q.multiply(v[0]));
            temp2 = u[1].subtract(q.multiply(v[1]));
            temp3 = u[2].subtract(q.multiply(v[2]));
            u[0] = v[0];
            u[1] = v[1];
            u[2] = v[2];
            v[0] = temp1;
            v[1] = temp2;
            v[2] = temp3;
        }
        if (u[1].compareTo(BigInteger.ZERO) != 1) // u[1] < 1
            return _phi.add(u[1]);
        return u[1];
    }


    public RSAKeys getRSAKeys() {
        return RSAKeys;
    }

    
    public String toString() {
        String ret = "p               : " + p + "\n"
                + "q               : " + q + "\n"
                + "n               : " + n + "\n"
                + "d               : " + d + "\n"
                + "phi             : " + phi + "\n"
                + "e               : " + e + "\n"
                + "public Key      : (" + phi + "," + e +")\n"
                + "private Key     : (" + d + "," + n + ")\n"
                + "bit lenght      : " + p.bitLength() + "\n";
        return ret;
    }

    public static synchronized byte[] decrypt(byte[] byteArray, RSAPrivateKey privateKey) {
        return new BigInteger(byteArray).modPow(privateKey.getD(), privateKey.getN()).toByteArray();
    }

    public static synchronized byte[] encrypt(byte[] byteArray, RSAPublicKey publicKey) {
        return new BigInteger(byteArray).modPow(publicKey.getE(), publicKey.getN()).toByteArray();
    }

    public static synchronized Object decryptObject(byte[] byteArray, RSAPrivateKey privateKey) throws IOException, ClassNotFoundException{
        return SerializableUtils.convertFromBytes(decrypt(byteArray,privateKey));
    }

    public static synchronized byte[] encryptObject(Object object, RSAPublicKey publicKey) throws IOException{
        return encrypt(SerializableUtils.convertToBytes(object),publicKey);
    }


}
