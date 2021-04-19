package edu.ufl.cise.bytetorrent;

import edu.ufl.cise.bytetorrent.config.CommonConfig;
import edu.ufl.cise.bytetorrent.config.PeerInfoConfig;
import edu.ufl.cise.bytetorrent.model.Peer;
import edu.ufl.cise.bytetorrent.util.LoggerUtil;

import java.util.HashMap;
import java.util.Map;

public class PeerProcess {
    public static void main(String[] args) {

        LoggerUtil.LogInfoMessage("Process Thread Started");
        final int id = Integer.parseInt(args[0]);
        PeerInfoConfig peerInfo = PeerInfoConfig.getInstance();
        Peer my_node = null;

        Map<Integer,Peer> remotePeers = new HashMap<>();

        for (Peer peer : peerInfo.getPeerInfoList()) {
            if (id == peer.getPeerId()) {
                my_node = peer;
            }
            else {
                remotePeers.put(peer.getPeerId(), peer);
            }
        }

        PeerManagerPlatform peerProcess = new PeerManagerPlatform(my_node, remotePeers);
		peerProcess.init();

    }
}
