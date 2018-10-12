package ch.zhaw.its.lab.secretkey;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Ransom {
    public static void main(String[] args) throws NoSuchPaddingException, InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException,
            InvalidKeyException {
        if (args.length != 2) {
            System.err.println("usage: RansomRansom plaintext ciphertext");
            System.exit(1);
        }

        FileEncrypter e = new FileEncrypter(args[0], args[1]);
        e.encrypt();

        File input = new File(args[0]);
        if (input.delete()) {
            System.out.println("MUAHAHAHAHAHAHA! The original file \"" + args[0] + "\" is gone, ");
            System.out.println("the key is also gone. The encryption algorithm is AES.");
            System.out.println("Now you must pay $$$ to get the files back! Resistance is futile!");
        } else {
            System.err.println("Deletion failed");
        }
    }
}
