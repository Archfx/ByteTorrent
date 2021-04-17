package peer;

import config.RemotePeerInfo;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerStateManagerPlatform {

    private List<Peer> kNeighborPeers;

    private ExecutorService executorService ;

    public PeerStateManagerPlatform(ArrayList<RemotePeerInfo> remotePeers) {
        executorService = Executors.newFixedThreadPool(remotePeers.size());
		//TODO  -: convert remote peers and initialize kNeighborPeers list
	}


    public void init() {
        kNeighborPeers.forEach(neighborPeer-> {
            executorService.submit(() -> {
                System.out.println(" Starting thread for peer :" + neighborPeer.getId());
                PeerStateManger2 peerStateManager = new PeerStateManger2(neighborPeer, kNeighborPeers);
                peerStateManager.start();
            });
        });
    }
}

