package ru.spbau.solikov.spring.test1.test;

import static org.junit.Assert.*;

import org.junit.Test;
import ru.spbau.solikov.spring.test1.MultiThreadCheckSum;
import ru.spbau.solikov.spring.test1.SingleThreadCheckSum;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;

/**
 * Class for testing both single and multi thread MD5 hashing.
 */
public class MD5Test {

    @Test
    public void testSingleThreadOnFile() throws IOException, NoSuchAlgorithmException {
        SingleThreadCheckSum singleThreadCheckSum = new SingleThreadCheckSum(Paths.get("file.txt"));
        assertEquals(ru.spbau.solikov.spring.test1.Main.bytesToHex(
                singleThreadCheckSum.getMD5Hash()).toLowerCase(),
                "91dc20b3cb7f407f92df6dc7bf04b4d8");
    }

    @Test
    public void testMultiThreadOnFile(){
        MultiThreadCheckSum multiThreadCheckSum = new MultiThreadCheckSum(Paths.get("file.txt"));
        assertEquals(ru.spbau.solikov.spring.test1.Main.bytesToHex(
                multiThreadCheckSum.getMD5Hash()).toLowerCase(),
                "91dc20b3cb7f407f92df6dc7bf04b4d8");
    }

    @Test
    public void testSingleThreadOnDirectory() throws IOException, NoSuchAlgorithmException {
        SingleThreadCheckSum singleThreadCheckSum = new SingleThreadCheckSum(Paths.get("TestMD5"));
        assertEquals(ru.spbau.solikov.spring.test1.Main.bytesToHex(
                singleThreadCheckSum.getMD5Hash()).toLowerCase(),
                "b465634291e59ba7bb2b882d9d0367a3");
    }

    @Test
    public void testMultiThreadOnDirectory() throws NoSuchAlgorithmException {
        MultiThreadCheckSum multiThreadCheckSum = new MultiThreadCheckSum(Paths.get("TestMD5"));
        assertEquals(ru.spbau.solikov.spring.test1.Main.bytesToHex(
                multiThreadCheckSum.getMD5Hash()).toLowerCase(),
                "b465634291e59ba7bb2b882d9d0367a3");
    }
}
