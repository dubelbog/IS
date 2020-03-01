package ch.zhaw.its.lab.secretkey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class KeyFinder {

    public static void findKey() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        File file = new File("mystery");
        byte[] inFile = Utils.readContentIntoByteArray(file);

        FileEncrypterITS e = new FileEncrypterITS(inFile);
        e.encrypt();
        SecretKey secretKey = e.getKey();
        String key = String.format("%x", new BigInteger(1, secretKey.getEncoded()));
        tryKeysBruteForce(key, e);
    }

    private static void tryKeysBruteForce(String key, FileEncrypterITS e) throws IOException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException {

        for (int a = 9; a > 2; a--) {
            for (int b = 5; b < 16; b++) {
                for (int c = 0; c < 16; c++) {
                    for (int d = 11; d < 16; d++) {
                        for (int e1 = 0; e1 < 16; e1++) {
                            for (int f = 0; f < 16; f++){
                                String num1 = getHex(a);
                                String num2 = getHex(b);
                                String num3 = getHex(c);
                                String num4 = getHex(d);
                                String num5 = getHex(e1);
                                String num6 = getHex(f);

                                key = num1 + num2 + num3 + num4 + num5 + num6 + key.substring(6);
                                byte[] rawKey = Utils.decodeKey(key);
                                e.decrypt(rawKey);

                                File decrypted = new File("decryption2");
                                double entropy = Entropy.getFileEntropy(decrypted);
                                if (Entropy.isNaturalLanguage(entropy)) {
                                    System.out.println("!!!!!!!!! SUCCESS !!!!!!!!!");
                                    System.out.println("The key is: " + key);
                                    return;
                                }
                            }
                        }
                    }
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
}
