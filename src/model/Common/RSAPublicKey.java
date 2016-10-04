package model.Common;

import java.math.BigInteger;

/**
 * Created by Armand on 22/09/2016.
 */
public class RSAPublicKey {

    private BigInteger e;
    private BigInteger n;

    ///////// format (e,n) ////////

    public RSAPublicKey(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return  "e   = " + e.toString() +
                "n = " + n.toString() + "\n";
    }
}
