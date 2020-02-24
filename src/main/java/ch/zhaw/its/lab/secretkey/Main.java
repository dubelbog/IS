package ch.zhaw.its.lab.secretkey;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {

    public static void main(String[] args) throws IOException, NoSuchPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
         entropyAnalyzer();

        File file = new File("cipher2");

        byte[] inFile = readContentIntoByteArray(file);

        FileEncrypterITS e = new FileEncrypterITS(inFile);

        String iv = getBlockOf16Bytes(inFile, 0);
        System.out.println(iv);
        ArrayList<String> test = new ArrayList<>();

        for (int i = 16; i < inFile.length/16; i += 16) {
            test.add(getBlockOf16Bytes(inFile, i));
        }


        iv = new BigInteger(iv, 16).toString(2);
   //     String first_16 = new BigInteger(test.get(0), 16).toString(2);

//        String res = "";
//        System.out.println(iv.length() == first_16.length());
//        for (int i = 0; i < first_16.length(); i++)
//        {
//            if (iv.charAt(i) == first_16.charAt(i)){
//                res += "0";
//            } else {
//                res += "1";
//            }
//
//        }
 //       System.out.println(res);


//
       adaptKey("0" + iv, e, file);
        e.encrypt();
        e.encrypt();

    }

    private static String getBlockOf16Bytes(byte[] inFile, int index) {
        String result = "";
        for (int i = index; i<index + 16; i++) {
            int val = inFile[i];
            if (val < 0) {
                val = val + 255;
                val++;
            }

            String hexStr = Integer.toString(val,16);
            if(hexStr.length() == 1) {
                hexStr = "0" + hexStr;
            }

            result += hexStr;
        }
        return result;
    }

    private static void adaptKey(String key, FileEncrypterITS e, File file) throws IOException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {

        for (int k = 0; k < 16; k++) {
            for (int i = 0; i < 16; i++) {
                String num1 = getHex(k);
                String num2 = getHex(i);

                key = num1 + num2 + key.substring(2);
                byte[] rawKey = decodeKey(key);
                e.decrypt(rawKey);

                double entropy = getFileEntropy(file);
                if (isNaturalLanguage(entropy)) {
                    break;
                }
            }

        }
    }

    private static String getHex(int i) {
        String num = Integer.toString(i);
        switch (i) {
            case 10:
                num = "a";
                break;
            case 11:
                num = "b";
                break;
            case 12:
                num = "c";
                break;
            case 13:
                num = "d";
                break;
            case 14:
                num = "e";
                break;
            case 15:
                num = "f";
                break;
            default:
                break;
        }

        return num;
    }

    private static byte[] decodeKey(String rawKey) throws IOException {
        return DatatypeConverter.parseHexBinary(rawKey);
    }


    private static void entropyAnalyzer() {
        ArrayList<String> paths = new ArrayList<>();

        paths.add("mystery");
        paths.add("/Users/bdubel/Documents/ZHAW/FS_2020/IST/Labs/lab_01/src/main/java/ch/zhaw/its/lab/secretkey/FileEncrypter.java");
        paths.add("lorem_ipsum.txt");
        paths.add("internet_text.txt");
        paths.add("random_1.txt");
        paths.add("random_short.txt");
        paths.add("cipher");
        paths.add("cipher1");


        for (int i = 0; i < paths.size(); i++) {

            File file = new File(paths.get(i));
            double entropy = getFileEntropy(file);

            System.out.println("File: " + file.getName());
            System.out.println(entropy);
            System.out.println("Natural Language: " + isNaturalLanguage(entropy));
            System.out.println("--------------------------------------------");
        }

    }

    private static double getFileEntropy(File file) {
        byte[] bFile = readContentIntoByteArray(file);
        double size = bFile.length;

        HashMap<Byte, Integer> elements = new HashMap<>();
        elements.put(bFile[0], 1);

        for (int k = 1; k < bFile.length; k++) {
            byte bin = bFile[k];
            if (elements.containsKey(bin)) {
                elements.put(bin, elements.get(bin) + 1);
            } else {
                elements.put(bin, 1);
            }
        }
        return countEntropy(elements, size);
    }

    private static boolean isNaturalLanguage(double entropy) {
        return entropy <= 5;
    }


    private static double countEntropy(HashMap<Byte, Integer> elements, double size) {
        double entropy = 0.0;

        for (Byte bin : elements.keySet()) {
            double probability = elements.get(bin) / size;
            entropy += probability * getLogToBase2(1.0 / probability);
        }

        return entropy;
    }

    private static byte[] readContentIntoByteArray(File file) {
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


    private static void excerise12and13() {
        // Aufgabe 11
        double n = 8.0;
        System.out.println("Entropy per Byte: only null bytes: ");
        double entropyNullBytes = (getLogToBase2(1.0) / n) * n;
        System.out.println(entropyNullBytes);

        // Aufgabe 12
        System.out.println("Entropy per Byte: random uniformly: ");
        double x = 2;
        double entropyUniform = (getLogToBase2(x)) * n;
        System.out.println(entropyUniform);
    }

    private static double getLogToBase2(double x) {
        return Math.log(x) / Math.log(2);
    }
}
