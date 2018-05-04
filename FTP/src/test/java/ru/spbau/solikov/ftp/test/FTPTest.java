package ru.spbau.solikov.ftp.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.spbau.solikov.ftp.FTPClient;
import ru.spbau.solikov.ftp.FTPFile;
import ru.spbau.solikov.ftp.FTPServer;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class for testing features of client-server FTP protocol.
 */
public class FTPTest {

    private FTPClient client;

    @Before
    public void setUp() {
        Thread server = new Thread(() -> FTPServer.main(new String[]{"4444"}));
        server.start();
        client = new FTPClient(4444, "localhost");
    }

    @Test
    public void testListOnNonExistentDirectory() throws IOException {
        assertEquals(client.list("src/test/resources/dqofpko").size(), 0);
    }

    @Test
    public void testListOnFile() throws IOException {
        assertEquals(client.list("src/test/resources/1").size(), 0);
    }

    @Test
    public void testListOnDirectory() throws IOException {
        assertEquals(client.list("src/test/resources/DirWith4Files").size(), 4);
        String[] expected = new String[]{"1", "2", "3", "dir"};
        boolean[] expected2 = new boolean[]{false, false, false, true};
        List<FTPFile> answer = client.list("src/test/resources/DirWith4Files");
        answer.sort(Comparator.comparing(FTPFile::getName));
        for (int i = 0; i < answer.size(); i++) {
            assertEquals(answer.get(i).isDirectory(), expected2[i]);
            assertEquals(answer.get(i).getName(), expected[i]);
        }
    }

    @Test
    public void testGetOnNonExistentFile() throws IOException {
        assertEquals(client.list("src/test/resources/dqofpko").size(), 0);
    }

    @Test
    public void testGetOnDirectory() throws IOException {
        assertEquals(client.list("src/test/resources/DirWith4Files").size(), 4);
    }

    @Test
    public void testGetOnGoodFile() throws IOException {
        assertArrayEquals(client.get("src/test/resources/1"), "dfwekopwef".getBytes());
    }

    @After
    public void shutDown() throws IOException {
        client.close();
    }
}
