package peer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PeerUtil {

    public byte[] readFile( String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        return Files.readAllBytes(path);
    }

    public boolean writeToFile(String pathStr, byte[] payload) throws IOException {
        Path path = Paths.get(pathStr);
        Files.write(path, payload);
        return true;
    }
}
