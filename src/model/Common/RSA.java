/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.Common;

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
        p = BigInteger.probablePrime(bitsLenght, random);
        q = BigInteger.probablePrime(bitsLenght, random);
        createKeys();
    }
    
    public RSA(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        createKeys();
    }

    private void createKeys() {
        n = createN();
        phi = createPhi();
        e = createE();
        d = createD();


        RSAKeys.setPublicKey(new RSAPublicKey(phi,e));
        RSAKeys.setPrivateKey(new RSAPrivateKey(p,q));
    }

    private BigInteger createN() {
        return p.multiply(q);
    }

    private BigInteger createE() {
        BigInteger tmpE = p.max(q).add(BigInteger.ONE); // take the bigger between (p and q)+1
        BigInteger resultGCD = BigInteger.ZERO;
        // while GCD != 1
        while ((resultGCD.compareTo(BigInteger.ONE) != 0)) {
            resultGCD = phi.gcd(tmpE);
            tmpE = tmpE.add(BigInteger.ONE);
        }
        return tmpE.subtract(BigInteger.ONE);
    }
    
    private BigInteger createPhi() {
            return (
                    p.subtract(BigInteger.ONE)
                            .multiply(p.subtract(BigInteger.ONE))
            );
    }

    private BigInteger createD() {
        return modInv(e,phi);
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



    private BigInteger gcd(BigInteger a,BigInteger b) {
        BigInteger r;
        while (b.compareTo(BigInteger.ZERO) == 1) { // while  b > 0
            r = a.mod(b);
            a = b;
            b = r;
        }
        return a;
    }

    public RSAKeys getRSAKeys() {
        return RSAKeys;
    }

    
    public String toString() {
        String ret = "p           : " + p.toString() + "\n"
                + "q           : " + q.toString() + "\n"
                + "n           : " + n.toString() + "\n"
                + "d           : " + d.toString() + "\n"
                + "phi         : " + phi.toString() + "\n"
                + "e           : " + e.toString() + "\n"
                + "public Key  : (" + phi.toString() + "," + e.toString()+")\n"
                + "private Key : (" + d.toString() + "," + n.toString() + ")\n"
                + "bit lenght  : " + p.bitLength() + "\n";
        return ret;
    }

    /*public synchronized String encrypt(String message, RSAPublicKey publickey) {
        return (new BigInteger(message.getBytes())).modPow(publickey.getE(), publickey.getN()).toString();
    }*/

    public synchronized BigInteger encrypt(BigInteger message, RSAPublicKey publickey) {
        return message.modPow(publickey.getE(), publickey.getN());
    }

    public synchronized String decrypt(String message) {
        return new String((new BigInteger(message)).modPow(e, n).toByteArray());
    }

}
