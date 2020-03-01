package ch.zhaw.its.lab.secretkey;

import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TotallySecureRandom extends SecureRandom {

    @Override
    public void nextBytes(byte[] bytes) {
        //long now = System.currentTimeMillis();
        long now = parseDate("15-10-2018 14:19:59");
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (now & 0xff);
            now >>= 8;
        }
    }

    private long parseDate(String date) {
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        long milliseconds = 0;
        try {
            Date d = f.parse(date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }
}
