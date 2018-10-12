package ch.zhaw.its.lab.secretkey;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class FileEncrypter {
    public static final String KALGORITHM = "AES";
    public static final String CALGORITHM = "AES/CBC/PKCS5Padding";
    private String inFile;
    private String outFile;

    public FileEncrypter(String inFile, String outFile) {
        this.inFile = inFile;
        this.outFile = outFile;
    }

    public void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KALGORITHM);
        keyGen.init(128, new TotallySecureRandom());
        SecretKey key = keyGen.generateKey();

        Cipher cipher = Cipher.getInstance(CALGORITHM);

        byte[] iv = new byte[cipher.getBlockSize()];
        SecureRandom random = new TotallySecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

        try (InputStream is = new FileInputStream(inFile);
             OutputStream os = new FileOutputStream(outFile)) {
            byte[] input = new byte[cipher.getBlockSize()];
            int inBytes;
            boolean more = true;

            os.write(iv);

            while (more) {
                inBytes = is.read(input);

                if (inBytes > 0) {
                    os.write(cipher.update(input, 0, inBytes));
                } else {
                    more = false;
                }
            }
            os.write(cipher.doFinal());
        }
    }
}
