package model.Common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Armand Souchon
 */
public class RSAPublicKey implements Serializable{

    private BigInteger n;
    private BigInteger e;
    ///////// format (n,e) ////////

    public RSAPublicKey(BigInteger n, BigInteger e) {
        this.n = n;
        this.e = e;
    }

    public BigInteger getN() {
        return n;
    }

    public BigInteger getE() {
        return e;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }


    @Override
    public String toString() {
        return  "n   = " + n + "\n" +
                "e   = " + e;
    }
}
