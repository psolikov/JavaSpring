package ru.spbau.solikov.spring.test1;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

/**
 * Class for testing speed of MD5 hashing implemented multi and single threaded.
 */
public class Main {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    /**
     * Returns string hex representation of byte array
     *
     * @param bytes array to be printed
     * @return string representation in hex
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        SingleThreadCheckSum singleThreadCheckSum = new SingleThreadCheckSum(Paths.get(args[0]));
        long finish = System.currentTimeMillis();
        System.out.println(finish - start);
        System.out.println(bytesToHex(singleThreadCheckSum.getMD5Hash()));

        start = System.currentTimeMillis();
        MultiThreadCheckSum multiThreadCheckSum = new MultiThreadCheckSum(Paths.get(args[0]));
        finish = System.currentTimeMillis();
        System.out.println(finish - start);
        System.out.println(bytesToHex(multiThreadCheckSum.getMD5Hash()));
    }
}
