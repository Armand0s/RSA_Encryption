package model;

import java.math.BigInteger;

/**
 * Created by Armand on 22/09/2016.
 */
public class RSAPublicKey {

    private BigInteger phi;
    private BigInteger e;

    public RSAPublicKey(BigInteger phi,BigInteger e) {
        this.phi = phi;
        this.e = e;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getPhi() {
        return phi;
    }

    public void setE(BigInteger e) {
        this.e = e;
    }

    public void setPhi(BigInteger phi) {
        this.phi = phi;
    }

    @Override
    public String toString() {
        String ret =
                "phi = " + phi.toString() + "\n" +
                "e   = " + e.toString();
        return ret;
    }
}
