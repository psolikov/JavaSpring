package ru.spbau.solikov.ftp;

/**
 * Class for storing some characteristics of file that is being transferred.
 */
public class FTPFile {
    private String name;
    private boolean isDirectory;

    public String getName() {
        return name;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public FTPFile(String name, boolean isDirectory) {
        this.name = name;
        this.isDirectory = isDirectory;
    }
}