import java.util.ArrayList;

public class PeerProcess {
    public static void main(String[] args) {
        // System.out.println(port);
        
        final int id = Integer.parseInt(args[0]);
        String address = "127.0.0.1";
        int port = 6008;
        boolean hasFile = false;


        // This will read the config files (need to be in the correct directories at some point)
        CommonConfig commonConfig = new CommonConfig();
        PeerInfoConfig peerInfo = new PeerInfoConfig();

        // We need to figure out existing remote peers
        ArrayList<RemotePeerInfo> remotePeers = new ArrayList<RemotePeerInfo>();

        for (RemotePeerInfo peer : peerInfo.peerInfoList) {
            if (id == peer.id) {
                address = peer.address;
                port = peer.port;
                hasFile = peer.hasFile;
            }
            else {
                // Add all other peers
                remotePeers.add(peer);
            }
        }

        
    }
}
