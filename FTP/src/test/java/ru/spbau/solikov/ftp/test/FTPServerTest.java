package ru.spbau.solikov.ftp.test;

import org.junit.Test;
import ru.spbau.solikov.ftp.FTPFile;
import ru.spbau.solikov.ftp.FTPServer;
import ru.spbau.solikov.ftp.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class for testing server part of application.
 */
public class FTPServerTest {

    private InputStream writeHead(int type, String path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(type);
        dos.writeUTF(path);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Test
    public void listTest() throws Exception {
        String path = "src/test/resources/Test";
        InputStream is = writeHead(Request.LIST.getID(), path);
        ServerSocket serverSocket = mock(ServerSocket.class);
        Socket clientSocket = mock(Socket.class);
        ByteArrayOutputStream answer = new ByteArrayOutputStream();
        when(serverSocket.accept()).thenReturn(clientSocket).thenThrow(new IOException());
        when(clientSocket.isConnected()).thenReturn(true).thenReturn(false);
        when(clientSocket.getOutputStream()).thenReturn(answer);
        when(clientSocket.getInputStream()).thenReturn(is);
        FTPServer server = new FTPServer(serverSocket);
        sleep(200);
        List<FTPFile> list = new ArrayList<>();
        list.add(new FTPFile("1", false));
        list.add(new FTPFile("2", false));
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(answer.toByteArray()));
        assertEquals(list.size(), dis.readInt());
        for (FTPFile file : list) {
            assertEquals(file.getName(), dis.readUTF());
            assertEquals(file.getDir(), dis.readBoolean());
        }
        server.close();
    }

    @Test
    public void listTestFolder() throws Exception {
        String path = "src/test/resources/Test";
        InputStream is = writeHead(Request.LIST.getID(), path);
        Socket clientSocket = mock(Socket.class);
        ServerSocket serverSocket = mock(ServerSocket.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(serverSocket.accept()).thenReturn(clientSocket).thenThrow(new IOException());
        when(clientSocket.getOutputStream()).thenReturn(baos);
        when(clientSocket.getInputStream()).thenReturn(is);
        when(clientSocket.isConnected()).thenReturn(true).thenReturn(false);
        FTPServer server = new FTPServer(serverSocket);
        sleep(200);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        List<FTPFile> list = new ArrayList<>();
        list.add(new FTPFile("1", false));
        list.add(new FTPFile("2", false));
        assertEquals(list.size(), in.readInt());
        for (FTPFile file : list) {
            assertEquals(file.getName(), in.readUTF());
            assertEquals(file.getDir(), in.readBoolean());
        }
    }

    @Test
    public void getTest() throws Exception {
        String path = "src/test/resources/Test/1";
        String s = "wfe;wo";
        Socket clientSocket = mock(Socket.class);
        ServerSocket serverSocket = mock(ServerSocket.class);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        when(serverSocket.accept()).thenReturn(clientSocket).thenThrow(new IOException());
        when(clientSocket.getOutputStream()).thenReturn(baos);
        when(clientSocket.getInputStream()).thenReturn(writeHead(Request.GET.getID(), path));
        when(clientSocket.isConnected()).thenReturn(true).thenReturn(false);
        FTPServer server = new FTPServer(serverSocket);
        sleep(200);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        long size = dis.readLong();
        byte[] data = new byte[Math.toIntExact(size)];
        int read = dis.read(data);
        assertEquals(read, size);
        assertArrayEquals(s.getBytes(), data);
    }
}
