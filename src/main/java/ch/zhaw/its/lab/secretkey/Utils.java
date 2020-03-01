package ch.zhaw.its.lab.secretkey;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Utils {

    public static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    public static double getLogToBase2(double x) {
        return Math.log(x) / Math.log(2);
    }

    public static byte[] decodeKey(String rawKey) throws IOException {
        return DatatypeConverter.parseHexBinary(rawKey);
    }
}
