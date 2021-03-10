package peer;

import config.CommonConfig;
import config.PeerInfoConfig;
import config.RemotePeerInfo;

import java.util.ArrayList;

public class PeerProcess {
    public static void main(String[] args) {
        // System.out.println(port);
        
        final int id = Integer.parseInt(args[0]);
        String address = "127.0.0.1";
        int port = 6008;
        boolean hasFile = false;


        // This will read the config files (need to be in the correct directories at some point)
        CommonConfig commonConfig = CommonConfig.getInstance();
        PeerInfoConfig peerInfo = PeerInfoConfig.getInstance();

        // We need to figure out existing remote peers
        ArrayList<RemotePeerInfo> remotePeers = new ArrayList<RemotePeerInfo>();

        for (RemotePeerInfo peer : peerInfo.getPeerInfoList()) {
            if (id == peer.getId()) {
                address = peer.getAddress();
                port = peer.getPort();
                hasFile = peer.isHasFile();
            }
            else {
                // Add all other peers
                remotePeers.add(peer);
            }
        }

        
    }
}
