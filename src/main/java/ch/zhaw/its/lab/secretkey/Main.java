package ch.zhaw.its.lab.secretkey;


import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;



public class Main {

    public static void main(String[] args) throws IOException, NoSuchPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {

        // Entropy
        Entropy.entropyAnalyzer();

        // Key
       KeyFinder.findKey();

    }


    private static void excerise12and13() {
        // Aufgabe 11
        double n = 8.0;
        System.out.println("Entropy per Byte: only null bytes: ");
        double entropyNullBytes = (Utils.getLogToBase2(1.0) / n) * n;
        System.out.println(entropyNullBytes);

        // Aufgabe 12
        System.out.println("Entropy per Byte: random uniformly: ");
        double x = 2;
        double entropyUniform = (Utils.getLogToBase2(x)) * n;
        System.out.println(entropyUniform);
    }


}
