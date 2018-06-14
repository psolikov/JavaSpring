package ru.spbau.solikov.ftp;

/**
 * Enumeration that represents details of server-client protocol.
 */
public enum Request {
    LIST(1), GET(2);

    private int ID;

    public int getID() {
        return ID;
    }

    Request(int i) {
        ID = i;
    }
}
