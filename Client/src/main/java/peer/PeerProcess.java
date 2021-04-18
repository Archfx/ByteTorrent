package peer;

import config.CommonConfig;
import config.PeerInfoConfig;
import util.LoggerUtil;

import java.util.ArrayList;

public class PeerProcess {
    public static void main(String[] args) {

        LoggerUtil.LogInfoMessage("Process Thread Started");
        // System.out.println(port);

        
        // Get the peer id from arguments
        // final int id = Integer.parseInt(args[0]);
        final int id = Integer.parseInt("1001");


        // Read Configurations from config files
        CommonConfig commonConfig = CommonConfig.getInstance();
        PeerInfoConfig peerInfo = PeerInfoConfig.getInstance();

        // Get my peer information
        Peer my_node = null;

        // Assign existing remote peers
        ArrayList<Peer> remotePeers = new ArrayList<Peer>();

        ChokeManager chokeM = new ChokeManager();


        for (Peer peer : peerInfo.getPeerInfoList()) {
            if (id == peer.getId()) {
                my_node = peer;
            }
            else {
                // Add all other peers
                remotePeers.add(peer);
            }
        }

        PeerManagerPlatform peerProcess = new PeerManagerPlatform(my_node, commonConfig);
		peerProcess.init();

        // peerStateManagerPlatform = new PeerStateManagerPlatform(my_node, remotePeers);
        System.out.println(" Starting choke "+ chokeM.choke(remotePeers));
        // peerStateManagerPlatform.init();
        

    }
}
