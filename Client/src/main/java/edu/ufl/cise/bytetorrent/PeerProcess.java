package edu.ufl.cise.bytetorrent;

import edu.ufl.cise.bytetorrent.config.CommonConfig;
import edu.ufl.cise.bytetorrent.config.PeerInfoConfig;
import edu.ufl.cise.bytetorrent.model.Peer;
import edu.ufl.cise.bytetorrent.service.ChokeManagementService;
import edu.ufl.cise.bytetorrent.util.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

public class PeerProcess {
    public static void main(String[] args) {

        LoggerUtil.LogInfoMessage("Process Thread Started");
        // System.out.println(port);

        
        // Get the peer id from arguments
         final int id = Integer.parseInt(args[0]);


        // Read Configurations from edu.ufl.cise.bytetorrent.config files
        CommonConfig commonConfig = CommonConfig.getInstance();
        PeerInfoConfig peerInfo = PeerInfoConfig.getInstance();

        // Get my peer information
        Peer my_node = null;

        // Assign existing remote peers
        Map<Integer,Peer> remotePeers = new HashMap<>();

        ChokeManagementService chokeM = new ChokeManagementService();


        for (Peer peer : peerInfo.getPeerInfoList()) {
            if (id == peer.getPeerId()) {
                my_node = peer;
            }
            else {
                // Add all other peers
                remotePeers.put(peer.getPeerId(), peer);
            }
        }

        PeerManagerPlatform peerProcess = new PeerManagerPlatform(my_node, remotePeers, commonConfig);
		peerProcess.init();

        // peerStateManagerPlatform = new PeerStateManagerPlatform(my_node, remotePeers);
//        System.out.println(" Starting choke "+ chokeM.choke(remotePeers));
        // peerStateManagerPlatform.init();
        

    }
}
