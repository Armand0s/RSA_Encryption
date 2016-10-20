package model.Common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Armand Souchon
 */
public class RSAPrivateKey implements Serializable{

    private BigInteger d;
    private BigInteger n;

    ////////// format (d,n) //////////

    public RSAPrivateKey(BigInteger d, BigInteger n) {
        this.d = d;
        this.n = n;
    }

    public BigInteger getD() {
        return d;
    }

    public void setD(BigInteger d) {
        this.d = d;
    }

    public BigInteger getN() {
        return n;
    }

    public void setN(BigInteger n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return  "d : " + d.toString() + "\nn : " + n.toString();
    }
}
