package ru.spbau.solikov.ftp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * Class that represents server which can handle some request from
 * clients about files stored on that server.
 */
public class FTPServer {

    private int port;
    private ServerSocket serverSocket;

    /**
     * To start the server run that method.
     *
     * @param args number of port to be used
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.print("Usage: java FTPServer <port number>");
            exit(1);
        }

        int givenPort = Integer.parseInt(args[0]);
        System.out.println("norm");
        try {
            new FTPServer(givenPort);
        } catch (IOException e) {
            System.out.println("Can't create server");
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Creates the server's instance bind with given port and starts it.
     *
     * @param port to be used for incoming requests
     */
    public FTPServer(int port) throws IOException {
        this(new ServerSocket(port));
        this.port = port;
    }

    /**
     * Creates the server's instance with given serverSocket.
     *
     * @param serverSocket to get connection from
     */
    public FTPServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        start();
    }

    /**
     * Closes the server socket.
     *
     * @throws IOException if can't close
     */
    public void close() throws IOException {
        serverSocket.close();
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
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(() -> {
                    try (DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                         DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());) {
                        int query = 0;

                        while (clientSocket.isConnected()) {
                            try {
                                query = dataInputStream.readInt();
                            } catch (IOException e) {
                                System.out.println("Can't read. Try again.");
                            }
                            if ((query != Request.LIST.getID()) && (query != Request.GET.getID())) break;
                            String path = null;
                            try {
                                path = dataInputStream.readUTF();
                            } catch (IOException e) {
                                System.out.println("Can't read. Try again.");
                            }
                            System.out.println("Just read : " + query + " " + path);
                            if (query == Request.LIST.getID()) {
                                System.out.println("LIST");
                                list(path, dataOutputStream);
                                continue;
                            }
                            get(path, dataOutputStream);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            } catch (IOException e) {
                return;
            }
        }
    }
}