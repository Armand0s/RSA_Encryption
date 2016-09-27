package model;

import java.math.BigInteger;

/**
 * Created by Armand on 22/09/2016.
 */
public class RSAPrivateKey {

    private BigInteger p;
    private BigInteger q;

    ////////// format (p,q) //////////

    public RSAPrivateKey(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getQ() {
        return q;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public void setQ(BigInteger q) {
        this.q = q;
    }

    public String toString() {
        String ret =
                "p = " + p.toString() + "\n" +
                "q = " + q.toString();
        return ret;
    }
}
