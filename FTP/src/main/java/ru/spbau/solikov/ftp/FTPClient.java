package ru.spbau.solikov.ftp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A client part of FTP communication. Provides listing and downloading from the server.
 */
public class FTPClient {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    /**
     * Creates channel between server and client.
     *
     * @param port that server is using for monitoring
     * @param address of the server
     */
    public FTPClient(int port, String address) {
        try {
            socket = new Socket(address, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.print("Can't establish connection. Try again.");
        }
    }

    /**
     * Closes the connection with server.
     * At first, writes "-1" to the server to signal that there won't be any queries.
     *
     * @throws IOException if can't close the connection
     */
    public void close() throws IOException {
        dataOutputStream.writeInt(-1);
        socket.close();
        dataOutputStream.close();
        dataInputStream.close();
    }

    /**
     * Listing of all files contained in given directory.
     * If path is invalid or is not a directory, returns empty list
     * as long as empty directory.
     *
     * @param path to be checked
     * @return list of all files in that directory
     * @throws IOException if cant't write to the socket due to connection problems
     */
    public List<FTPFile> list(String path) throws IOException {
        dataOutputStream.writeInt(1);
        dataOutputStream.writeUTF(path);
        int size = dataInputStream.readInt();
        List<FTPFile> ftpFiles = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            String name = dataInputStream.readUTF();
            boolean isDirectory = dataInputStream.readBoolean();
            ftpFiles.add(new FTPFile(name, isDirectory));
        }

        return ftpFiles;
    }

    /**
     * Getter of file by given path. Creates a copy in working directory on client.
     * If path is invalid, file is empty or it is a directory, returns null.
     *
     * @param path to be checked
     * @return byte array of file's content
     * @throws IOException if cant't write to the socket due to connection problems
     */
    public byte[] get(String path) throws IOException {
        dataOutputStream.writeInt(2);
        dataOutputStream.writeUTF(path);
        long size = dataInputStream.readLong();
        if (size == 0) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        long readData = 0;
        while (readData < size) {
            int read = dataInputStream.read(data);
            readData += read;
            output.write(data, 0, read);
        }
        output.flush();
        try (FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/" +
                path.substring(path.lastIndexOf("/") + 1))) {
            fos.write(output.toByteArray());
        }

        return output.toByteArray();
    }

    /**
     * To start the client run that method.
     *
     * @param args host name and port number
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java FTPClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        FTPClient ftpClient = new FTPClient(portNumber, hostName);
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("To list files type <1: Int> <path: String>; " +
                    "to get file type <2: Int> <path: String>; to stop the program type -1.");
            int query = sc.nextInt();
            if (query == 1){
                ftpClient.list(sc.next());
            }

            if (query == 2){
                ftpClient.get(sc.next());
            }

            if (query == -1){
                break;
            }
        }

        ftpClient.close();
    }
}
