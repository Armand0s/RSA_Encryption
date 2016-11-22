package model.Common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author Armand Souchon
 */
public class RSAPrivateKey implements Serializable{

    private BigInteger n;
    private BigInteger d;

    ////////// format (n,d) //////////

    public RSAPrivateKey(BigInteger n, BigInteger d) {
        this.n = n;
        this.d = d;
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
        return  "n : " + n + "\nd : " + d;
    }
}
