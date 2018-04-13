package ru.spbau.solikov.spring.test1;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

/**
 * Implementation of MD5 hashing of given path multi threaded.
 */
public class MultiThreadCheckSum {

    private byte[] MD5Hash;

    public byte[] getMD5Hash() {
        return MD5Hash;
    }

    private static byte[] getFileMD5(Path path) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(path);
             DigestInputStream dis = new DigestInputStream(is, messageDigest)) {
            while (dis.read() != -1) {
            }
        }
        return messageDigest.digest();
    }

    public MultiThreadCheckSum(Path path){
        MD5Task md5Task = new MD5Task(path);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MD5Hash = forkJoinPool.invoke(md5Task);
    }

    /**
     * Recursive task to be done by forkjoinpool.
     */
    private class MD5Task extends RecursiveTask<byte[]>{

        private Path path;

        public MD5Task(Path p){
            path = p;
        }

        @Override
        protected byte[] compute() {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                if(Files.isDirectory(path)){
                    messageDigest.update(path.getFileName().toString().getBytes());

                    List<Path> paths = Files.list(path).collect(Collectors.toList());
                    List<MD5Task> tasks = new ArrayList<>();

                    for (Path p : paths){
                        tasks.add(new MD5Task(p));
                    }

                    for (MD5Task task : tasks){
                        task.fork();
                    }
                    for (MD5Task task : tasks){
                        messageDigest.update(task.join());
                    }

                    return messageDigest.digest();
                } else {
                    try {
                        return getFileMD5(path);
                    } catch (IOException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }

            return new byte[0];
        }
    }
}
