package ru.spbau.solikov.spring.test1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of MD5 hashing of given path single threaded.
 */
public class SingleThreadCheckSum {

    private Path path;
    private byte[] MD5Hash;

    public byte[] getMD5Hash() {
        return MD5Hash;
    }

    public SingleThreadCheckSum(Path path) throws IOException, NoSuchAlgorithmException {
        this.path = path;
        MD5Hash = computeHash(path);
    }

    private byte[] computeHash(Path path) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        if (Files.isDirectory(path)) {
            messageDigest.update(path.getFileName().toString().getBytes());

            List<Path> paths = Files.list(path).collect(Collectors.toList());
            for (Path p : paths) {
                messageDigest.update(computeHash(p));
            }
        } else {
            try (InputStream is = Files.newInputStream(path);
                 DigestInputStream dis = new DigestInputStream(is, messageDigest)) {
                while (dis.read() != -1) {
                }
            }
        }
        return messageDigest.digest();
    }

}
