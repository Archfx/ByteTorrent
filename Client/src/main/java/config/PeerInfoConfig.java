import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class PeerInfoConfig {
    public static final String FILE_NAME = "PeerInfo.cfg";
    public ArrayList<RemotePeerInfo> peerInfoList = new ArrayList<RemotePeerInfo>();

    public PeerInfoConfig() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(FILE_NAME));
            // TODO: Hasini read config
            for (String line; (line = in.readLine()) != null;) {
            }
            }
        catch (Exception e){}

        // Hardcode for now
        peerInfoList.add(new RemotePeerInfo(1001, "lin114-00.cise.ufl.edu", 6001, true));
        peerInfoList.add(new RemotePeerInfo(1002, "lin114-01.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1003, "lin114-02.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1004, "lin114-03.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1005, "lin114-04.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1006, "lin114-05.cise.ufl.edu", 6001, true));
        peerInfoList.add(new RemotePeerInfo(1007, "lin114-06.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1008, "lin114-07.cise.ufl.edu", 6001, false));
        peerInfoList.add(new RemotePeerInfo(1009, "lin114-08.cise.ufl.edu", 6001, false));
    }
}