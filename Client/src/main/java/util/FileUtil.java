package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {

    static String PEER_PREFIX = "peer_";
    static String FULL_FILE_NAME = "full";


    public byte[] readPartFile( int peerId, int index ) throws IOException {
        return readFile(PEER_PREFIX + peerId + File.separator + index);
    }

    public byte[] readFullFile( int peerId ) throws IOException {
        return readFile(PEER_PREFIX + peerId + File.separator + FULL_FILE_NAME);
    }

    public boolean generateAndWriteFullFile(){
        // TODO -: implement recreation of full file from partial file
//        return writeToFile();
        return true;
    }

    public boolean writePartialFile(int peerId, int index, byte[] payload) throws IOException {
        return writeToFile(PEER_PREFIX + peerId + File.separator + index, payload);
    }

    private byte[] readFile( String pathStr ) throws IOException {
        Path path = Paths.get(pathStr);
        return Files.readAllBytes(path);
    }

    private boolean writeToFile( String pathStr, byte[] payload ) throws IOException {
        Path path = Paths.get(pathStr);
        Files.write(path, payload);
        return true;
    }
}
