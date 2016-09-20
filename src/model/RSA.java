/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 *
 * @author p1408610
 */
public class RSA {
    
    private BigInteger publicKey;
    
    private final SecureRandom random = new SecureRandom();

    private BigInteger p; 
    private BigInteger q;
    
    private BigInteger n; // p*q
    private BigInteger phi; // (p-1)*(q-1)
    
    private BigInteger e; // pgcd(max(p,q) + quelque chose) = 1
    
    public RSA(int bitsLenght){
        p = BigInteger.probablePrime(bitsLenght, random);
        q = BigInteger.probablePrime(bitsLenght, random);
        
        getPhiandN();
        createPublicKey();
    }
    
    public RSA(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        
        getPhiandN();
        createPublicKey();
        
    }
    
    private void createPublicKey() {
        e = BigInteger.ZERO;
        BigInteger iterator = new BigInteger("1");
        if(p.compareTo(q) == 0) // equal
            e = p;
        if(p.compareTo(q) == 1) // upper
            e = p;
        if(p.compareTo(q) == -1) // lower
            e = q;
        do {
            
            e = pgcd(phi, e.add(iterator));
            iterator = iterator.add(BigInteger.ONE);
        } while (e.compareTo(BigInteger.ONE) != 0);
        
        e = phi.add(iterator.subtract(BigInteger.ONE));
    }
    
    private void getPhiandN() {
       phi = (p.subtract(new BigInteger("1")).multiply(p.subtract(new BigInteger("1"))));
       n = p.multiply(q);
    }
    /**
     * 
     * @param a
     * @param b
     * @return 
     */
    private BigInteger pgcd(BigInteger a,BigInteger b) {
        BigInteger r;
        while (b.compareTo(BigInteger.ZERO) == 1) { // while  b > 0
            r = a.mod(b);
            a = b;
            b = r;
        }
        return a;
    }
    
    public String toString() {
        String ret = "p : " + p.toString() + "\n"
                + "q : " + q.toString() + "\n"
                + "n : " + n.toString() + "\n"
                + "phi : " + phi.toString() + "\n"
                + "e : " + e.toString() + "\n"
                + "public Key : (" + e.toString() + "," + n.toString()+"\n"
                + "bit lenght : " + p.bitLength() + "\n";
        return ret;
    }

}
