package ru.spbau.solikov.ftp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;
import static java.lang.System.in;

/**
 * A client part of FTP communication. Provides listing and downloading from the server.
 */
public class FTPClient {

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    /**
     * Creates a channel between server and client.
     *
     * @param port    that server is using for monitoring
     * @param address of the server
     */
    public FTPClient(int port, String address) {
        try {
            socket = new Socket(address, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.print("Can't establish connection. Try again.");
            e.printStackTrace();
            exit(1);
        }
    }

    /**
     * Creates a channel between server and client from given socket.
     *
     * @param s socket to be connected
     */
    public FTPClient(Socket s) {
        socket = s;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.print("Can't establish connection. Try again.");
            e.printStackTrace();
            exit(1);
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
        writeType(Request.LIST, path);
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
        writeType(Request.GET, path);
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

    private void writeType(Request request, String path) throws IOException {
        switch (request) {
            case LIST:
                dataOutputStream.writeInt(1);
                dataOutputStream.writeUTF(path);
                break;
            case GET:
                dataOutputStream.writeInt(2);
                dataOutputStream.writeUTF(path);
                break;
        }
    }

    /**
     * To start the client run that method.
     *
     * @param args host name and port number
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java FTPClient <host name> <port number>");
            exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        FTPClient ftpClient = new FTPClient(portNumber, hostName);

        while (true) {
            System.out.println();
            Scanner sc = new Scanner(System.in);
            System.out.println("To list files type \"list <path of dir>\"; " +
                    "to get file type \"get <path: String>\"; to stop the program type \"-1\".");
            String query = sc.next();
            if (query.equals("list")) {
                String path = sc.next();
                List<FTPFile> list = ftpClient.list(path);
                if (list.size() == 0) {
                    System.out.println("No files for such path");
                    continue;
                }
                for (FTPFile ftpFile : list) {
                    System.out.println("Name: \"" + ftpFile.getName() + "\", is dir: " + ftpFile.isDirectory());
                }
                continue;
            }

            if (query.equals("get")) {
                String path = sc.next();
                if (ftpClient.get(path) != null) System.out.println("Done!");
                else System.out.println("No file for such path");
                continue;
            }

            if (query.equals("-1")) {
                break;
            }

            System.out.println("Sorry, use the format:");
        }

        ftpClient.close();
    }
}
