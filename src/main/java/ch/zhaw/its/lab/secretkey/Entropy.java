package ch.zhaw.its.lab.secretkey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Entropy {

    public static void entropyAnalyzer() {

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

    public static double getFileEntropy(File file) {
        byte[] bFile = Utils.readContentIntoByteArray(file);
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

    public static boolean isNaturalLanguage(double entropy) {
        return entropy <= 5;
    }


    private static double countEntropy(HashMap<Byte, Integer> elements, double size) {
        double entropy = 0.0;

        for (Byte bin : elements.keySet()) {
            double probability = elements.get(bin) / size;
            entropy += probability * Utils.getLogToBase2(1.0 / probability);
        }

        return entropy;
    }


}
