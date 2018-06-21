package ru.spbau.solikov.ftp;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Class for storing some characteristics of file that is being transferred.
 */
public class FTPFile {
    private SimpleStringProperty name;
    private SimpleBooleanProperty dir;

    public boolean getDir() {
        return dir.get();
    }


    public String getName() {
        return name.get();
    }



    public FTPFile(String name, boolean isDirectory) {
        this.name = new SimpleStringProperty(name);
        this.dir = new SimpleBooleanProperty(isDirectory);
    }
}