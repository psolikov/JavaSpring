package ru.spbau.solikov.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that represents server which can handle some request from
 * clients about files stored on that server.
 */
public class FTPServer {

    private int port;

    /**
     * To start the server run that method.
     *
     * @param args number of port to be used
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.print("Usage: java FTPServer <port number>");
            System.exit(1);
        }

        int givenPort = Integer.parseInt(args[0]);

        new FTPServer(givenPort);
    }

    /**
     * Creates the server's instance bind with given port and starts it.
     *
     * @param port to be used for incoming requests
     */
    public FTPServer(int port) {
        this.port = port;
        start();
    }

    private void list(String path, DataOutputStream dataOutputStream) {
        File directory = new File(path);

        try {
            if (!directory.exists()) {
                dataOutputStream.writeInt(0);
                return;
            }

            File[] files = directory.listFiles();
            int length = files != null ? files.length : 0;
            dataOutputStream.writeInt(length);
            if (length != 0) {
                for (File file : files) {
                    dataOutputStream.writeUTF(file.getName());
                    dataOutputStream.writeBoolean(file.isDirectory());
                }
            }
        } catch (IOException e) {
            System.out.print("Problems with connection. Try again.");
        }
    }

    private void get(String path, DataOutputStream dataOutputStream) {
        File file = new File(path);

        try {
            if (!file.exists() || !file.isFile()) {
                dataOutputStream.writeLong(0);
                return;
            }
            dataOutputStream.writeLong(file.length());
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[1024];
            int len;
            while ((len = fis.read(data)) > 0) {
                dataOutputStream.write(data, 0, len);
            }
            fis.close();
        } catch (IOException e) {
            System.out.print("Problems with connection. Try again.");
        }
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket clientSocket = serverSocket.accept();
             DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            int query;

            while (clientSocket.isConnected()) {
                query = dataInputStream.readInt();
                if ((query != 1) && (query != 2)) break;
                String path = dataInputStream.readUTF();
                if (query == 1) {
                    list(path, dataOutputStream);
                    continue;
                }
                get(path, dataOutputStream);
            }
        } catch (IOException e) {
            System.out.print("Can't establish connection.");
        }
    }
}