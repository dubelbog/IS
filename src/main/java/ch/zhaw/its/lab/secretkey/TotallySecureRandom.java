package ch.zhaw.its.lab.secretkey;

import java.security.SecureRandom;

public class TotallySecureRandom extends SecureRandom {
    @Override
    public void nextBytes(byte[] bytes) {
        long now = System.currentTimeMillis();
        System.out.println("now: " + now);
        System.out.println("bytes.length: " + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (now & 0xff);
            System.out.println(bytes[i]);
            now >>= 8;
            System.out.println("now new: " + now);
        }
    }
}
