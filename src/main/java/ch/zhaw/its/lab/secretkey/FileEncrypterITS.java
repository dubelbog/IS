package ch.zhaw.its.lab.secretkey;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileEncrypterITS {
    public static final String KALGORITHM = "AES";
    public static final String CALGORITHM = KALGORITHM + "/CBC/PKCS5Padding";
    private byte[] inFile;
    private SecretKey key;

    public FileEncrypterITS(byte[] inFile) {
        this.inFile = inFile;
    }

    public void crypt(InputStream is, OutputStream os, Cipher cipher) throws IOException, BadPaddingException, IllegalBlockSizeException {
        boolean more = true;
        byte[] input = new byte[cipher.getBlockSize()];

        while (more) {
            int inBytes = is.read(input);

            if (inBytes > 0) {
                os.write(cipher.update(input, 0, inBytes));
            } else {
                more = false;
            }
        }
        os.write(cipher.doFinal());
    }

    public void encrypt() throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator keyGen = KeyGenerator.getInstance(KALGORITHM);
        keyGen.init(128, new TotallySecureRandom());
        SecretKey key = keyGen.generateKey();


        Cipher cipher = Cipher.getInstance(CALGORITHM);
        byte[] rawIv = new byte[cipher.getBlockSize()];
        new TotallySecureRandom().nextBytes(rawIv);
        IvParameterSpec iv = new IvParameterSpec(rawIv);
        this.key = key;
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

    }

    public SecretKey getKey() {
        return this.key;
    }


    public IvParameterSpec readIv(InputStream is, Cipher cipher) throws IOException {
        byte[] rawIv = new byte[cipher.getBlockSize()];
        int inBytes = is.read(rawIv);

        if (inBytes != cipher.getBlockSize()) {
            throw new IOException("can't read IV from file");
        }

        return new IvParameterSpec(rawIv);
    }

    public void decrypt(byte[] rawKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException, InvalidKeyException {

        SecretKey key = new SecretKeySpec(rawKey, 0, rawKey.length, KALGORITHM);
        Cipher cipher = Cipher.getInstance(CALGORITHM);

        try (InputStream is = new ByteArrayInputStream(inFile);
             OutputStream os = new FileOutputStream("decryption2")) {
            IvParameterSpec ivParameterSpec = readIv(is, cipher);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            crypt(is, os, cipher);
        } catch (BadPaddingException e) {
            String keyString = String.format("%x", new BigInteger(1, key.getEncoded()));
            System.out.println("try again, the key " + keyString + " does not work.");
        }

    }
}
