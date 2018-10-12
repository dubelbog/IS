package ch.zhaw.its.lab.secretkey;

import java.security.SecureRandom;

public class TotallySecureRandom extends SecureRandom {
    /** Computes the next bytes of random numbers.
     *
     * @param bytes the array in which to store the random bytes
     */
    @Override
    public void nextBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }

        long now = System.nanoTime();
        int i = 0;

        while (now != 0 && i < bytes.length) {
            bytes[i] = (byte) (now & 0xff);
            i++;
            now >>= 8;
        }
    }

    @Override
    public String getAlgorithm() {
        return "TSR"; // Totally Secure Random
    }
}
