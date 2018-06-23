package ru.spbau.solikov.ftp.test;

import org.junit.Test;
import ru.spbau.solikov.ftp.FTPClient;
import ru.spbau.solikov.ftp.FTPFile;
import ru.spbau.solikov.ftp.Request;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * Class for testing client part of application.
 */
public class FTPClientTest {

    @Test
    public void listTest() throws Exception {
        String path = "src/test/resources/Test";
        ByteArrayOutputStream answer = new ByteArrayOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        Socket clientSocket = mock(Socket.class);
        List<FTPFile> ftpFiles = new ArrayList<>();
        ftpFiles.add(new FTPFile("1", false));
        ftpFiles.add(new FTPFile("2", false));
        out.writeInt(ftpFiles.size());
        for (FTPFile file : ftpFiles) {
            out.writeUTF(file.getName());
            out.writeBoolean(file.getDir());
        }
        when(clientSocket.getOutputStream()).thenReturn(answer);
        when(clientSocket.getInputStream()).thenReturn(new ByteArrayInputStream(baos.toByteArray()));
        List<FTPFile> list = new FTPClient(clientSocket).list(path);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(answer.toByteArray()));
        assertEquals(Request.LIST.getID(), dis.readInt());
        assertEquals(path, dis.readUTF());
        assertEquals(ftpFiles.size(), list.size());
        for (int i = 0; i < ftpFiles.size(); i++){
            assertEquals(ftpFiles.get(i).getName(), list.get(i).getName());
            assertEquals(ftpFiles.get(i).getDir(), list.get(i).getDir());
        }
    }

    @Test
    public void getTest() throws Exception {
        String test = "wopEGMWPEMF";
        String path = "e,fl42e9w";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Socket clientSocket = mock(Socket.class);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(test.length());
        dos.write(test.getBytes());
        when(clientSocket.getOutputStream()).thenReturn(baos);
        when(clientSocket.getInputStream()).thenReturn(new ByteArrayInputStream(bos.toByteArray()));
        new FTPClient(clientSocket).get(path);
        File file = new File(path);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        assertEquals(Request.GET.getID(), dis.readInt());
        assertEquals(path, dis.readUTF());
        assertEquals("wopEGMWPEMF", Files.lines(Paths.get(file.getPath())).collect(Collectors.joining()));
    }
}
